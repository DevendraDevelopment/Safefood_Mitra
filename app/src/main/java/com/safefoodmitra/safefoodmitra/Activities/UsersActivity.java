package com.safefoodmitra.safefoodmitra.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.safefoodmitra.safefoodmitra.Adapter.UsersAdapter;
import com.safefoodmitra.safefoodmitra.Apis.ApiClients;
import com.safefoodmitra.safefoodmitra.Apis.RetApis;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.UnitModals;
import com.safefoodmitra.safefoodmitra.Modals.UserModals;
import com.safefoodmitra.safefoodmitra.Modals.Validation_custome;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.ActivityUsersBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.safefoodmitra.safefoodmitra.Activities.AdminMainActivity.respoid;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Addusers;

public class UsersActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityUsersBinding usersBinding;
    public List<UserModals>userModals;
    public UsersAdapter usersAdapter;
    RetApis apiInterface;
    ArrayList<Validation_custome> fileds;
    EditText firstnme,lastnme,usrmobile,usrmail;
    Spinner fbounit;
    String fobid;
    ArrayList<String> fobpart;
    List<UnitModals>unitModals;
    Animation topanim;
    public static RecyclerView recyclerView;
    public static TextView norecord;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usersBinding= DataBindingUtil.setContentView(this,R.layout.activity_users);
        pref = this.getSharedPreferences("MyG9", MODE_PRIVATE);
        if (Utlity.is_online(this)) usersdata();
        else Utlity.show_toast(UsersActivity.this, getResources().getString(R.string.nointernet));
        click();
        recyclerView=usersBinding.userlist;
        norecord=usersBinding.noreords;

        usersBinding.searching.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                usersAdapter.getFilter().filter(charSequence);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void usersdata() {
        Utlity.show_progress(this);
        apiInterface = ApiClients.getClient().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Users(respoid,"Bearer "+ Utlity.get_user(this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        Utlity.dismiss_dilog(UsersActivity.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        userModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<UserModals>>() {}.getType());

                        if(userModals.size()!=0) {
                            usersBinding.noreords.setVisibility(View.GONE);
                            usersBinding.userlist.setLayoutManager(new LinearLayoutManager(UsersActivity.this, RecyclerView.VERTICAL, false));
                            usersAdapter = new UsersAdapter(UsersActivity.this, userModals);
                            usersBinding.userlist.setAdapter(usersAdapter);
                        }
                        else
                        {
                            usersBinding.noreords.setVisibility(View.VISIBLE);

                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog(UsersActivity.this);

            }
        });

    }

    public void click(){
        usersBinding.back.setOnClickListener(this::onClick);
        usersBinding.adduser.setOnClickListener(this::onClick);
        usersBinding.searchuser.setOnClickListener(this::onClick);
        usersBinding.cancle.setOnClickListener(this::onClick);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onClick(View view) {
        topanim= AnimationUtils.loadAnimation(this,R.xml.top_animation);
        if (view.getId()==R.id.back){
            onBackPressed();
        }
        else if (view.getId()==R.id.adduser){
            usersAleart();
        }
        else if (view.getId()==R.id.searchuser){
            usersBinding.searclayout.setAnimation(topanim);
            usersBinding.searclayout.setVisibility(View.VISIBLE);
            usersBinding.searchuser.setVisibility(View.GONE);
            usersBinding.titles.setVisibility(View.GONE);
        }
        else if (view.getId()==R.id.cancle){
            usersBinding.searclayout.setVisibility(View.GONE);
            usersBinding.searchuser.setVisibility(View.VISIBLE);
            usersBinding.titles.setVisibility(View.VISIBLE);
        }

    }

    public void usersAleart() {
        final Dialog dialog = new Dialog(UsersActivity.this);
        final TextView save;
        final ImageButton cancle;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.adduser);
        dialog.getWindow().setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
        firstnme=dialog.findViewById(R.id.firstnme);
        lastnme=dialog.findViewById(R.id.lastnme);
        usrmobile=dialog.findViewById(R.id.mobilenum);
        usrmail=dialog.findViewById(R.id.emailid);
       // fbounit=dialog.findViewById(R.id.fobunits);
        save=dialog.findViewById(R.id.addzones);
        cancle=dialog.findViewById(R.id.cancle);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstnmes=firstnme.getText().toString();
                String lastnmes=lastnme.getText().toString();
                String usrnumbers=usrmobile.getText().toString();
                String usrmails=usrmail.getText().toString();
                fileds = new ArrayList<>();
                fileds.add(new Validation_custome("text", firstnme));
                fileds.add(new Validation_custome("text", lastnme));
                fileds.add(new Validation_custome("text", usrmobile));
                fileds.add(new Validation_custome("text", usrmail));
                if (Utlity.validation(UsersActivity.this, fileds)) {
                    if (Utlity.is_online(UsersActivity.this)){
                      //  if (fbounit.getSelectedItemPosition()==0){
                        //    Utlity.show_toast(UsersActivity.this, "Please Select Unit name");
                     //   }
                      //  else {
                            addusers(firstnmes,lastnmes,usrnumbers,usrmails);
                            dialog.dismiss();
                     //   }

                    }
                    else {
                        Utlity.show_toast(UsersActivity.this, getResources().getString(R.string.nointernet));

                    }

                }

            }
        });

        dialog.show();
     //   unitdata();
    }


    private void addusers(final String firstnmes,final String lastnmes,final String usrnumbers,final String usrmails) {
        Utlity.show_progress(this);
        String respoid1 = pref.getString("respoid",null);
        HashMap<String, String> keys = new HashMap<>();
        keys.put("first_name", firstnmes);
        keys.put("last_name", lastnmes);
        keys.put("mobile_no", usrnumbers);
        keys.put("email", usrmails);
        keys.put("fobunits_id", respoid1);
        Request result= post( keys, Addusers);
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Utlity.dismiss_dilog(UsersActivity.this);
            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Utlity.dismiss_dilog(UsersActivity.this);
                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            if (response.isSuccessful()){
                                Utlity.show_toast(UsersActivity.this,object.getInt("success")+" Added Sucessfully");
                                usersdata();
                            }
                            else {
                                Utlity.show_toast(UsersActivity.this,"Unauthorised");
                            }
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

        });

    }
   /* private void unitdata() {
        Utlity.show_progress(this);
        apiInterface = ApiClients.getClient().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Units("Bearer "+ Utlity.get_user(this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        Utlity.dismiss_dilog(UsersActivity.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        unitModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<UnitModals>>() {}.getType());
                        fobpart=new ArrayList<>();
                        fobpart.add("Select Unit");
                        for (UnitModals unitModal:unitModals){
                            fobpart.add(unitModal.getUnit_name());
                        }

                        ArrayAdapter<String> adapter1 =
                                new ArrayAdapter<String>(UsersActivity.this,  R.layout.spinneritem, fobpart);
                        adapter1.setDropDownViewResource( R.layout.spinneritem);
                        fbounit.setAdapter(adapter1);

                        fbounit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position!=0){
                                    fobid=unitModals.get(position-1).getId();
                                    Utlity.show_toast(UsersActivity.this,fobid);

                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }

                        });


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog(UsersActivity.this);
                Utlity.show_toast(UsersActivity.this, "Not found");

            }
        });

    }*/

    public Request post(HashMap<String, String> keys, String api_name) {
        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        FormBody.Builder body = new FormBody.Builder();
        for (Object key : keys.keySet()) {
            String value = keys.get(key);
            if(!TextUtils.isEmpty(value)) {
                body.add(key.toString(), value);
            }
            else {
                body.add(key.toString(), "");
            }
        }
        RequestBody parmetrs = body.build();
        return new Request.Builder()
                .url(api_name)
                .header("Authorization","Bearer "+ Utlity.get_user(this).getToken())
                .post(parmetrs)
                .build();
    }
}