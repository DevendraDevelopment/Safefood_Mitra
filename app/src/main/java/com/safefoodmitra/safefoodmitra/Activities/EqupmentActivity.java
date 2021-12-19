package com.safefoodmitra.safefoodmitra.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.reflect.TypeToken;
import com.safefoodmitra.safefoodmitra.Adapter.EqupmentAdapter;
import com.safefoodmitra.safefoodmitra.Apis.ApiClients;
import com.safefoodmitra.safefoodmitra.Apis.RetApis;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.EqupmentModals;
import com.safefoodmitra.safefoodmitra.Modals.UnitModals;
import com.safefoodmitra.safefoodmitra.Modals.Validation_custome;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.ActivityEqupmentBinding;

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
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Addequipments;

public class EqupmentActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityEqupmentBinding equpmentBinding;
    public EqupmentAdapter equpmentAdapter;
    public List<EqupmentModals> equpmentModals;
    RetApis apiInterface;
    ArrayList<Validation_custome> fileds;
    EditText equpmentnnme;
    String equpment,unitid;
    Spinner fobunits;
    ArrayList<String> fobpart;
    List<UnitModals>unitModals;
    Animation topanim;
    public static RecyclerView recyclerView;
    public static TextView norecord;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        equpmentBinding= DataBindingUtil.setContentView(this,R.layout.activity_equpment);
        recyclerView=equpmentBinding.equpmentlist;
        norecord=equpmentBinding.noreords;

        click();
        if (Utlity.is_online(this)){
            equpmenttdata();
        }
        else Utlity.show_toast(EqupmentActivity.this,getResources().getString(R.string.nointernet));

        equpmentBinding.searching.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                equpmentAdapter.getFilter().filter(charSequence);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void click(){
        equpmentBinding.back.setOnClickListener(this::onClick);
        equpmentBinding.addequpment.setOnClickListener(this::onClick);
        equpmentBinding.search.setOnClickListener(this::onClick);
        equpmentBinding.cancle.setOnClickListener(this::onClick);
    }
    @SuppressLint("ResourceType")
    @Override
    public void onClick(View view) {
        topanim= AnimationUtils.loadAnimation(this,R.xml.top_animation);

        if (view.getId()==R.id.back){
            onBackPressed();
        }
        else if (view.getId()==R.id.addequpment){
            equpmentAleart();
        }
        else if (view.getId()==R.id.search){
            equpmentBinding.searclayout.setAnimation(topanim);
            equpmentBinding.searclayout.setVisibility(View.VISIBLE);
            equpmentBinding.search.setVisibility(View.GONE);
            equpmentBinding.titles.setVisibility(View.GONE);
        }
        else if (view.getId()==R.id.cancle){
            equpmentBinding.searclayout.setVisibility(View.GONE);
            equpmentBinding.search.setVisibility(View.VISIBLE);
            equpmentBinding.titles.setVisibility(View.VISIBLE);
        }
    }

    private void equpmenttdata() {
        Utlity.show_progress(this);
        apiInterface = ApiClients.getClient().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Equpments(respoid,"Bearer "+ Utlity.get_user(this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        Utlity.dismiss_dilog(EqupmentActivity.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        equpmentModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<EqupmentModals>>() {}.getType());
                        if(equpmentModals.size()!=0) {
                            equpmentBinding.equpmentlist.setHasFixedSize(true);
                            equpmentBinding.equpmentlist.setVisibility(View.VISIBLE);
                            equpmentBinding.noreords.setVisibility(View.GONE);
                            equpmentBinding.equpmentlist.setLayoutManager(new LinearLayoutManager(EqupmentActivity.this, RecyclerView.VERTICAL,false));
                            equpmentAdapter = new EqupmentAdapter(EqupmentActivity.this, equpmentModals);
                            equpmentBinding.equpmentlist.setAdapter(equpmentAdapter);
                        }
                        else
                        {
                            equpmentBinding.noreords.setVisibility(View.VISIBLE);
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog(EqupmentActivity.this);

            }
        });

    }

    public void equpmentAleart() {
        final Dialog dialog = new Dialog(EqupmentActivity.this);
        final TextView save;
        final ImageButton cancle;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.addequpment);
        dialog.getWindow().setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
        equpmentnnme=dialog.findViewById(R.id.addequpmentnme);
        save=dialog.findViewById(R.id.addequpment);
        cancle=dialog.findViewById(R.id.cancle);
       // fobunits=dialog.findViewById(R.id.fobunits);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equpment=equpmentnnme.getText().toString();
                fileds = new ArrayList<>();
                fileds.add(new Validation_custome("text", equpmentnnme));
                if (Utlity.validation(EqupmentActivity.this, fileds)) {
                    if (Utlity.is_online(EqupmentActivity.this)){
                        adaequpment(equpment);
                        dialog.dismiss();
                    }
                    else {
                        Utlity.show_toast(EqupmentActivity.this, getResources().getString(R.string.nointernet));

                    }

                }

            }
        });

        dialog.show();
        //unitstypes();
    }


    private void adaequpment(final String equip_name) {
        Utlity.show_progress(this);
        HashMap<String, String> keys = new HashMap<>();
        keys.put("fobunits_id", respoid);
        keys.put("equip_name", equip_name);
        Request result= post( keys, Addequipments);
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Utlity.dismiss_dilog(EqupmentActivity.this);
            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Utlity.dismiss_dilog(EqupmentActivity.this);
                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            if (response.isSuccessful()){
                                Utlity.show_toast(EqupmentActivity.this,object.getInt("success")+" Added Sucessfully");
                                equpmenttdata();
                            }
                            else {
                                Utlity.show_toast(EqupmentActivity.this,"Unauthorised");
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