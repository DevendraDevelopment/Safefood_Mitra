package com.safefoodmitra.safefoodmitra.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
import com.safefoodmitra.safefoodmitra.Adapter.InspectRespoAdapter;
import com.safefoodmitra.safefoodmitra.Adapter.InspectResponsibilityAdapter;
import com.safefoodmitra.safefoodmitra.Apis.ApiClients;
import com.safefoodmitra.safefoodmitra.Apis.RetApis;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.DepartItem;
import com.safefoodmitra.safefoodmitra.Modals.InspectrespoModals;
import com.safefoodmitra.safefoodmitra.Modals.InspectrespoModals2;
import com.safefoodmitra.safefoodmitra.Modals.Item;
import com.safefoodmitra.safefoodmitra.Modals.ListItem;
import com.safefoodmitra.safefoodmitra.Modals.RecordModals;
import com.safefoodmitra.safefoodmitra.Modals.RespoModal;
import com.safefoodmitra.safefoodmitra.Modals.UserItem;
import com.safefoodmitra.safefoodmitra.Modals.UserModals;
import com.safefoodmitra.safefoodmitra.Modals.Validation_custome;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.ActivityInspectionResponsibilityBinding;

import org.json.JSONArray;
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
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Addinspetrespo;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Addrecords;

public class InspectionResponsibility extends AppCompatActivity implements View.OnClickListener {
    ActivityInspectionResponsibilityBinding responsibilityBinding;
    RetApis apiInterface;
    Spinner department,user;
    InspectRespoAdapter inspectRespoAdapter;
    List<InspectrespoModals>inspectrespoModals;
    List<ListItem> consolidatedList = new ArrayList<>();
    ArrayList<String> departpart;
    ArrayList<String> userpart;
    List<RespoModal>respoModals;
    List<UserModals>userModals;
    String dptid,userid;
    Animation topanim;
    InspectResponsibilityAdapter inspectResponsibilityAdapter;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        responsibilityBinding= DataBindingUtil.setContentView(this,R.layout.activity_inspection_responsibility);
        pref = this.getSharedPreferences("MyG9", MODE_PRIVATE);
        click();
        if (Utlity.is_online(this)){
            inrspodata();
        }
        else Utlity.show_toast(InspectionResponsibility.this,getResources().getString(R.string.nointernet));


            inspectrespoModals=new ArrayList<>();
            responsibilityBinding.inspectionlist.setVisibility(View.VISIBLE);
            responsibilityBinding.inspectionlist.setLayoutManager(new LinearLayoutManager(InspectionResponsibility.this, RecyclerView.VERTICAL, false));
            inspectResponsibilityAdapter = new InspectResponsibilityAdapter(InspectionResponsibility.this, inspectrespoModals);
            responsibilityBinding.inspectionlist.setAdapter(inspectResponsibilityAdapter);

        responsibilityBinding.searching.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchedVal = charSequence.toString();

                String categoryString = "";
                List<InspectrespoModals> cacheMenuResList = new ArrayList<>();
                List<InspectrespoModals2> ms = new ArrayList<>();

                if (TextUtils.isEmpty(searchedVal)) {
                    inspectResponsibilityAdapter.setCacheMenuRes(inspectrespoModals);

                } else {
                    for (InspectrespoModals c : inspectrespoModals) {
                        List<InspectrespoModals2> menuList = c.getInspectrespoModals2s();

                        for (InspectrespoModals2 m : menuList) {
                            if (m.getRecord_name().toLowerCase().contains(searchedVal)) {
                                categoryString = c.getDept_name();
                                ms.add(m);
                            }
                        }
                    }
                    cacheMenuResList.add(new InspectrespoModals(categoryString, ms));
                    inspectResponsibilityAdapter.setCacheMenuRes(cacheMenuResList);

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
    public void click(){
        responsibilityBinding.back.setOnClickListener(this);
        responsibilityBinding.addinspectrespo.setOnClickListener(this);
        responsibilityBinding.searchrecord.setOnClickListener(this);
        responsibilityBinding.cancle.setOnClickListener(this);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onClick(View view) {
        topanim= AnimationUtils.loadAnimation(this,R.xml.top_animation);
        if (view.getId()==R.id.back){
            onBackPressed();
        }
        else if (view.getId()==R.id.addinspectrespo){
            locationAleart();
        }
        else if (view.getId()==R.id.searchrecord){
            responsibilityBinding.searclayout.setAnimation(topanim);
            responsibilityBinding.searclayout.setVisibility(View.VISIBLE);
            responsibilityBinding.searchrecord.setVisibility(View.GONE);
            responsibilityBinding.titles.setVisibility(View.GONE);
        }
        else if (view.getId()==R.id.cancle){
            responsibilityBinding.searclayout.setVisibility(View.GONE);
            responsibilityBinding.searchrecord.setVisibility(View.VISIBLE);
            responsibilityBinding.titles.setVisibility(View.VISIBLE);
        }

    }

    public void locationAleart() {

        final Dialog dialog = new Dialog(InspectionResponsibility.this);
        final TextView save;
        final ImageButton cancle;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.addinspetrespo);
        dialog.getWindow().setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
        save=dialog.findViewById(R.id.addlocations);
        cancle=dialog.findViewById(R.id.cancle);
        department=dialog.findViewById(R.id.departlist);
        user=dialog.findViewById(R.id.userlist);

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (department.getSelectedItemPosition() == 0) {
                    Utlity.show_toast(InspectionResponsibility.this, "please Select Departments");

                } else if (user.getSelectedItemPosition() == 0) {
                    Utlity.show_toast(InspectionResponsibility.this, "please Select Users");

                } else {
                    if (Utlity.is_online(InspectionResponsibility.this)){
                        addinspectrespo();
                        dialog.dismiss();
                    }
                    else {
                        Utlity.show_toast(InspectionResponsibility.this, getResources().getString(R.string.nointernet));
                    }
                }
            }
        });

        dialog.show();
        departments();
        users();
    }

    private void addinspectrespo() {
        Utlity.show_progress(this);
        HashMap<String, String> keys = new HashMap<>();
        keys.put("departments_id", dptid);
        keys.put("users_id", userid);
        Request result= post( keys, Addinspetrespo);
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Utlity.dismiss_dilog(InspectionResponsibility.this);
            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Utlity.dismiss_dilog(InspectionResponsibility.this);
                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            if (response.isSuccessful()){
                                Utlity.show_toast(InspectionResponsibility.this,object.getInt("success")+" Added Sucessfully");
                                Intent intent= getIntent();
                                finish();
                                startActivity(intent);
                            }
                            else {
                                Utlity.show_toast(InspectionResponsibility.this,"Unauthorised");
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
    private void inrspodata() {
        Utlity.show_progress(this);
        final HashMap<String,List<InspectrespoModals2>> map = new HashMap<>();
        apiInterface = ApiClients.getClient().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.InspectRespo(respoid,"Bearer "+ Utlity.get_user(this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        Utlity.dismiss_dilog(InspectionResponsibility.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        //inspectrespoModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<InspectrespoModals>>() {}.getType());
                        JSONArray jsonArray = object.getJSONArray("success");

                        for(int i=0 ;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject =jsonArray.getJSONObject(i);
                            if(map.containsKey(jsonObject.getString("dept_name")))
                            {
                                List<InspectrespoModals2> menu = map.get(jsonObject.getString("dept_name"));
                                menu.add(new InspectrespoModals2(jsonObject.getString("id"),jsonObject.getString("record_name"),jsonObject.getString("dept_name")));
                                map.put(jsonObject.getString("dept_name"),menu);
                            }
                            else
                            {
                                List<InspectrespoModals2> menus = new ArrayList<>();
                                menus.add(new InspectrespoModals2(jsonObject.getString("id"),jsonObject.getString("record_name"),jsonObject.getString("dept_name")));
                                map.put(jsonObject.getString("dept_name"),menus);
                            }

                        }
                        for(String catname : map.keySet())
                        {
                            inspectrespoModals.add(new InspectrespoModals(catname,map.get(catname)));
                        }
                        Utlity.dismiss_dilog(InspectionResponsibility.this);
                        inspectResponsibilityAdapter.notifyDataSetChanged();

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog(InspectionResponsibility.this);

            }
        });

    }

    private void departments() {
        Utlity.show_progress(InspectionResponsibility.this);
        String respoid1 = pref.getString("respoid",null);
        apiInterface = ApiClients.getClient1().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Depatlist(respoid1,"Bearer "+ Utlity.get_user(InspectionResponsibility.this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        Utlity.dismiss_dilog(InspectionResponsibility.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        respoModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<RespoModal>>() {}.getType());
                        departpart=new ArrayList<>();
                        departpart.add("Select Deaprtment Id");
                        for (RespoModal respoModal:respoModals){
                            departpart.add(respoModal.getDept_name());
                        }

                        ArrayAdapter<String> adapter1 =
                                new ArrayAdapter<String>(InspectionResponsibility.this,  R.layout.spinneritem, departpart);
                        adapter1.setDropDownViewResource( R.layout.spinneritem);
                        department.setAdapter(adapter1);

                       /* if (dptnme != null) {
                            int spinnerPosition = adapter1.getPosition(dptnme);
                            departments.setSelection(spinnerPosition);
                        }

                        */

                        department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position!=0){
                                    dptid=respoModals.get(position-1).getId();
                                  //  Utlity.show_toast(InspectionResponsibility.this, dptid);

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
                Utlity.dismiss_dilog(InspectionResponsibility.this);
                Utlity.show_toast(InspectionResponsibility.this, "Not Founded Data");

            }
        });

    }
    private void users() {
        apiInterface = ApiClients.getClient().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Users(respoid,"Bearer "+ Utlity.get_user(InspectionResponsibility.this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        userModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<UserModals>>() {}.getType());
                        userpart=new ArrayList<>();
                        userpart.add("Select Users Id");
                        for (UserModals userModals:userModals){
                            userpart.add(userModals.getFirst_name()+ " "+ userModals.getLast_name());
                        }

                        ArrayAdapter<String> adapter1 =
                                new ArrayAdapter<String>(InspectionResponsibility.this,  R.layout.spinneritem, userpart);
                        adapter1.setDropDownViewResource( R.layout.spinneritem);
                        user.setAdapter(adapter1);

                        /*if (usernme != null) {
                            int spinnerPosition = adapter1.getPosition(usernme);
                            users.setSelection(spinnerPosition);
                        }

                         */

                        user.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position!=0){
                                    userid=userModals.get(position-1).getId();
                                   // Utlity.show_toast(InspectionResponsibility.this, userid);

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
                Utlity.show_toast(InspectionResponsibility.this, "Not Founded Data");

            }
        });

    }

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