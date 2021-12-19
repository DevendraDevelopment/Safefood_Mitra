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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.reflect.TypeToken;
import com.safefoodmitra.safefoodmitra.Adapter.UnitAdapter;
import com.safefoodmitra.safefoodmitra.Apis.ApiClients;
import com.safefoodmitra.safefoodmitra.Apis.RetApis;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.FobUnitTypesModals;
import com.safefoodmitra.safefoodmitra.Modals.UnitModals;
import com.safefoodmitra.safefoodmitra.Modals.Validation_custome;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.ActivityUnitsBinding;

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
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Addfobunits;

public class UnitsActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityUnitsBinding unitsBinding;
    public UnitAdapter unitAdapter;
    public List<UnitModals> unitModals;
    RetApis apiInterface;
    ArrayList<Validation_custome> fileds;
    public List<FobUnitTypesModals> fobUnitTypesModals;
    ArrayList<String> fobpart;
    Spinner fobunits;
    EditText unitname,citynme,stanme,zipcode;
    String fobtypeid,unitnme,citynmes,stnme,zpcode;
    Animation topanim;
    public static RecyclerView recyclerView;
    public static TextView norecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unitsBinding= DataBindingUtil.setContentView(this,R.layout.activity_units);
        recyclerView=unitsBinding.unitslist;
        norecord=unitsBinding.noreords;

        click();
        if (Utlity.is_online(this)){
            unitdata();
        }
        else {
            Utlity.show_toast(UnitsActivity.this,getResources().getString(R.string.nointernet));

        }

        unitsBinding.searching.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                unitAdapter.getFilter().filter(charSequence);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void click(){
        unitsBinding.back.setOnClickListener(this::onClick);
        unitsBinding.addunit.setOnClickListener(this::onClick);
        unitsBinding.searchunit.setOnClickListener(this::onClick);
        unitsBinding.cancle.setOnClickListener(this::onClick);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onClick(View view) {
        topanim= AnimationUtils.loadAnimation(this,R.xml.top_animation);

        if (view.getId()==R.id.back){
            onBackPressed();
        }
        else if (view.getId()==R.id.addunit){
            unitAleart();
        }
        else if (view.getId()==R.id.searchunit){
            unitsBinding.searclayout.setAnimation(topanim);
            unitsBinding.searclayout.setVisibility(View.VISIBLE);
            unitsBinding.searchunit.setVisibility(View.GONE);
            unitsBinding.titles.setVisibility(View.GONE);
        }
        else if (view.getId()==R.id.cancle){
            unitsBinding.searclayout.setVisibility(View.GONE);
            unitsBinding.searchunit.setVisibility(View.VISIBLE);
            unitsBinding.titles.setVisibility(View.VISIBLE);
        }


    }


    private void unitdata() {
        Utlity.show_progress(this);
        apiInterface = ApiClients.getClient().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Units("Bearer "+ Utlity.get_user(this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        Utlity.dismiss_dilog(UnitsActivity.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        unitModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<UnitModals>>() {}.getType());

                        if(unitModals.size()!=0) {
                            unitsBinding.noreords.setVisibility(View.GONE);
                            unitsBinding.unitslist.setVisibility(View.VISIBLE);
                            unitsBinding.unitslist.setLayoutManager(new LinearLayoutManager(UnitsActivity.this, RecyclerView.VERTICAL, false));
                            unitAdapter = new UnitAdapter(UnitsActivity.this, unitModals);
                            unitsBinding.unitslist.setAdapter(unitAdapter);
                        }
                        else
                        {
                            unitsBinding.noreords.setVisibility(View.VISIBLE);
                            unitsBinding.unitslist.setVisibility(View.GONE);
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog(UnitsActivity.this);

            }
        });

    }


    public void unitAleart() {
        final Dialog dialog = new Dialog(UnitsActivity.this);
        final TextView save;
        final ImageButton cancle;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.addunit);
        dialog.getWindow().setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
        unitname=dialog.findViewById(R.id.unitnme);
        citynme=dialog.findViewById(R.id.citynme);
        stanme=dialog.findViewById(R.id.statenme);
        zipcode=dialog.findViewById(R.id.zipcode);
        save=dialog.findViewById(R.id.addunits);
        cancle=dialog.findViewById(R.id.cancle);
        fobunits=dialog.findViewById(R.id.fobtype);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        fobunits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0){
                    fobtypeid=fobUnitTypesModals.get(position-1).getId();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unitnme=unitname.getText().toString();
                citynmes=citynme.getText().toString();
                stnme=stanme.getText().toString();
                zpcode=zipcode.getText().toString();
                fileds = new ArrayList<>();
                fileds.add(new Validation_custome("text", unitname));
                fileds.add(new Validation_custome("text", citynme));
                fileds.add(new Validation_custome("text", stanme));
                fileds.add(new Validation_custome("text", zipcode));
                if (Utlity.validation(UnitsActivity.this, fileds)) {
                    if (Utlity.is_online(UnitsActivity.this)){
                        addunits(fobtypeid,unitnme,citynmes,stnme,zpcode);
                        dialog.dismiss();
                    }
                    else {
                        Utlity.show_toast(UnitsActivity.this, getResources().getString(R.string.nointernet));

                    }

                }

            }
        });

        dialog.show();
        fobtypes();
    }

    private void fobtypes() {
        Utlity.show_progress(this);
        apiInterface = ApiClients.getClient().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.FobType("Bearer "+ Utlity.get_user(this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        Utlity.dismiss_dilog(UnitsActivity.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        fobUnitTypesModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<FobUnitTypesModals>>() {}.getType());
                        fobpart=new ArrayList<>();
                        fobpart.add("Select FobTypes");
                        for (FobUnitTypesModals fobUnitTypesModal:fobUnitTypesModals){
                            fobpart.add(fobUnitTypesModal.getFobtype());
                        }

                        ArrayAdapter<String> adapter1 =
                                new ArrayAdapter<String>(UnitsActivity.this,  R.layout.spinneritem, fobpart);
                        adapter1.setDropDownViewResource( R.layout.spinneritem);
                        fobunits.setAdapter(adapter1);


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog(UnitsActivity.this);
                Utlity.show_toast(UnitsActivity.this, "Not found");

            }
        });

    }

    private void addunits(final String fobtypeid,final String unitnme,final String citynme,final String stnme,final String zpcode) {
        Utlity.show_progress(this);
        HashMap<String, String> keys = new HashMap<>();
        keys.put("fobtypes_id", fobtypeid);
        keys.put("unit_name", unitnme);
        keys.put("city_name", citynme);
        keys.put("state_name", stnme);
        keys.put("zipcode", zpcode);
        Request result= post( keys, Addfobunits);
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Utlity.dismiss_dilog(UnitsActivity.this);
            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Utlity.dismiss_dilog(UnitsActivity.this);
                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            if (response.isSuccessful()){
                                Utlity.show_toast(UnitsActivity.this,object.getInt("success")+" Added Sucessfully");
                                unitdata();
                            }
                            else {
                                Utlity.show_toast(UnitsActivity.this,"Unauthorised");
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