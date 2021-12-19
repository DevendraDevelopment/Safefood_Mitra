package com.safefoodmitra.safefoodmitra.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.media.MediaPlayer;
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

import com.google.gson.reflect.TypeToken;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;
import com.safefoodmitra.safefoodmitra.Adapter.ZoneAdapter;
import com.safefoodmitra.safefoodmitra.Apis.ApiClients;
import com.safefoodmitra.safefoodmitra.Apis.RetApis;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.UnitModals;
import com.safefoodmitra.safefoodmitra.Modals.Validation_custome;
import com.safefoodmitra.safefoodmitra.Modals.ZoneModals;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.ActivityZonesBinding;

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
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Addzones;

public class ZoneActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityZonesBinding zonesBinding;
    public ZoneAdapter zoneAdapter;
    public List<ZoneModals>zoneModals;
    ArrayList<Validation_custome> fileds;
    RetApis apiInterface;
    String zonenme,unitid;
    EditText zonenmae;
    Animation topanim;
    public static RecyclerView recyclerView;
    public static TextView norecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        zonesBinding= DataBindingUtil.setContentView(this,R.layout.activity_zones);
        click();
        recyclerView=zonesBinding.zonelist;
        norecord=zonesBinding.noreords;

        if (Utlity.is_online(this)){
            zonedata();
        }
        else {
            Utlity.show_toast(ZoneActivity.this,getResources().getString(R.string.nointernet));

        }


        zonesBinding.searching.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                zoneAdapter.getFilter().filter(charSequence);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void click(){
        zonesBinding.back.setOnClickListener(this::onClick);
        zonesBinding.addzones.setOnClickListener(this::onClick);
        zonesBinding.search.setOnClickListener(this::onClick);
        zonesBinding.cancle.setOnClickListener(this::onClick);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onClick(View view) {
        topanim= AnimationUtils.loadAnimation(this,R.xml.top_animation);

        if (view.getId()==R.id.back){
            onBackPressed();
        }
        else if (view.getId()==R.id.addzones){
            zoneAleart();
        }
        else if (view.getId()==R.id.search){

            zonesBinding.searclayout.setAnimation(topanim);
            zonesBinding.searclayout.setVisibility(View.VISIBLE);
            zonesBinding.search.setVisibility(View.GONE);
            zonesBinding.titles.setVisibility(View.GONE);

        }
        else if (view.getId()==R.id.cancle){
            zonesBinding.searclayout.setVisibility(View.GONE);
            zonesBinding.search.setVisibility(View.VISIBLE);
            zonesBinding.titles.setVisibility(View.VISIBLE);
        }
    }

    private void zonedata() {
        Utlity.show_progress(this);
        apiInterface = ApiClients.getClient().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Zones(respoid,"Bearer "+ Utlity.get_user(this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        Utlity.dismiss_dilog(ZoneActivity.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        zoneModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<ZoneModals>>() {}.getType());
                        //zoneid=zoneModals.get(Integer.parseInt(object.getJSONObject("success").toString())).getId();

                        if(zoneModals.size()!=0) {
                            zonesBinding.noreords.setVisibility(View.GONE);
                            zonesBinding.zonelist.setVisibility(View.VISIBLE);
                            zonesBinding.zonelist.setLayoutManager(new LinearLayoutManager(ZoneActivity.this, RecyclerView.VERTICAL, false));
                            zoneAdapter = new ZoneAdapter(ZoneActivity.this, zoneModals);
                            zonesBinding.zonelist.setAdapter(zoneAdapter);
                        }
                        else
                        {
                            zonesBinding.noreords.setVisibility(View.VISIBLE);
                            zonesBinding.zonelist.setVisibility(View.GONE);
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog(ZoneActivity.this);

            }
        });

    }

    public void zoneAleart() {
        final Dialog dialog = new Dialog(ZoneActivity.this);
        final ImageButton cancle;
        final TextView save;

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.addzone);
        dialog.getWindow().setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
        zonenmae=dialog.findViewById(R.id.addzonenme);
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
                zonenme=zonenmae.getText().toString();
                fileds = new ArrayList<>();
                fileds.add(new Validation_custome("text", zonenmae));
                if (Utlity.validation(ZoneActivity.this, fileds)) {
                    if (Utlity.is_online(ZoneActivity.this)){
                            addzone(zonenme);
                            dialog.dismiss();
                    }
                    else {
                        Utlity.show_toast(ZoneActivity.this, getResources().getString(R.string.nointernet));
                    }

                }

                }
        });

        dialog.show();

    }

    private void addzone(final String zonenme) {
        Utlity.show_progress(this);
        HashMap<String, String> keys = new HashMap<>();
        keys.put("fobunits_id", respoid);
        keys.put("zone_name", zonenme);
        Request result= post( keys, Addzones);
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Utlity.dismiss_dilog(ZoneActivity.this);
            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Utlity.dismiss_dilog(ZoneActivity.this);
                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            if (response.isSuccessful()){
                                Utlity.show_toast(ZoneActivity.this,object.getInt("success")+" Added Sucessfully");
                                zonedata();
                            }
                            else {
                                Utlity.show_toast(ZoneActivity.this,"Unauthorised");
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

    @Override
    public void onBackPressed() {
        //hideKeyboard();
        super.onBackPressed();
    }
   /* private void hideKeyboard() {
        zonesBinding.searclayout.setVisibility(View.GONE);
        zonesBinding.search.setVisibility(View.VISIBLE);
        zonesBinding.titles.setVisibility(View.VISIBLE);

        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(zonesBinding.searclayout.getWindowToken(), 0);


    }

    */

}