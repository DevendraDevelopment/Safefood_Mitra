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
import com.safefoodmitra.safefoodmitra.Adapter.LocationAdapter;
import com.safefoodmitra.safefoodmitra.Apis.ApiClients;
import com.safefoodmitra.safefoodmitra.Apis.RetApis;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.LocationModals;
import com.safefoodmitra.safefoodmitra.Modals.UnitModals;
import com.safefoodmitra.safefoodmitra.Modals.Validation_custome;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.ActivityLocationBinding;

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
import retrofit2.http.Path;

import static com.safefoodmitra.safefoodmitra.Activities.AdminMainActivity.respoid;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Addlocations;

public class LocationActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityLocationBinding locationBinding;
    public LocationAdapter locationAdapter;
    public List<LocationModals> locationModals;
    RetApis apiInterface;
    ArrayList<Validation_custome> fileds;

    EditText locationnnme;
    String locations,unitid,parentid;
    Spinner fobunits,parents;

    ArrayList<String> fobpart;
    List<UnitModals>unitModals;
    ArrayList<String> parentpart;
    Animation topanim;
    public static RecyclerView recyclerView;
    public static TextView norecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationBinding= DataBindingUtil.setContentView(this,R.layout.activity_location);
        click();
        recyclerView=locationBinding.locationlist;
        norecord=locationBinding.noreords;

        if (Utlity.is_online(this)){
            locationdata();
        }
        else {
            Utlity.show_toast(LocationActivity.this,getResources().getString(R.string.nointernet));

        }
        locationBinding.searching.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                locationAdapter.getFilter().filter(charSequence);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
    public void click(){
        locationBinding.back.setOnClickListener(this::onClick);
        locationBinding.addlocation.setOnClickListener(this::onClick);
        locationBinding.serchlocation.setOnClickListener(this::onClick);
        locationBinding.cancle.setOnClickListener(this::onClick);

    }

    @SuppressLint("ResourceType")
    @Override
    public void onClick(View view) {
        topanim= AnimationUtils.loadAnimation(this,R.xml.top_animation);

        if (view.getId()==R.id.back){
            onBackPressed();
        }

        else if (view.getId()==R.id.addlocation){
            locationAleart();
        }
        else if (view.getId()==R.id.serchlocation){
            locationBinding.searclayout.setAnimation(topanim);
            locationBinding.searclayout.setVisibility(View.VISIBLE);
            locationBinding.serchlocation.setVisibility(View.GONE);
            locationBinding.titles.setVisibility(View.GONE);
        }
        else if (view.getId()==R.id.cancle){
            locationBinding.searclayout.setVisibility(View.GONE);
            locationBinding.serchlocation.setVisibility(View.VISIBLE);
            locationBinding.titles.setVisibility(View.VISIBLE);
        }


    }


    private void locationdata() {
        Utlity.show_progress(this);
        apiInterface = ApiClients.getClient().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Locations(respoid,"Bearer "+ Utlity.get_user(this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        Utlity.dismiss_dilog(LocationActivity.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        locationModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<LocationModals>>() {}.getType());
                        if(locationModals.size()!=0) {
                            locationBinding.noreords.setVisibility(View.GONE);
                            locationBinding.locationlist.setVisibility(View.VISIBLE);
                            locationBinding.locationlist.setLayoutManager(new LinearLayoutManager(LocationActivity.this, RecyclerView.VERTICAL, false));
                            locationAdapter = new LocationAdapter(LocationActivity.this, locationModals);
                            locationBinding.locationlist.setAdapter(locationAdapter);
                        }
                        else
                        {
                            locationBinding.noreords.setVisibility(View.VISIBLE);
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog(LocationActivity.this);

            }
        });

    }


    public void locationAleart() {

        final Dialog dialog = new Dialog(LocationActivity.this);
        final TextView save;
        final ImageButton cancle;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.addlocation);
        dialog.getWindow().setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
        locationnnme=dialog.findViewById(R.id.addlocationnme);
        save=dialog.findViewById(R.id.addlocations);
        cancle=dialog.findViewById(R.id.cancle);
       // fobunits=dialog.findViewById(R.id.fobunits);
        parents=dialog.findViewById(R.id.parentlists);

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        /*fobunits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0){
                    unitid=unitModals.get(position-1).getId();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

         */



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locations=locationnnme.getText().toString();
                fileds = new ArrayList<>();
                fileds.add(new Validation_custome("text", locationnnme));
                if (Utlity.validation(LocationActivity.this, fileds)) {
                    if (Utlity.is_online(LocationActivity.this)){
                        addlocation(parentid,locations);
                        dialog.dismiss();
                    }
                    else {
                        Utlity.show_toast(LocationActivity.this, getResources().getString(R.string.nointernet));

                    }

                }

            }
        });


        dialog.show();
        parentstype();
    }

    private void parentstype() {
        Utlity.show_progress(this);
        apiInterface = ApiClients.getClient().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Locationstype(respoid,"Bearer "+ Utlity.get_user(this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        Utlity.dismiss_dilog(LocationActivity.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        locationModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<LocationModals>>() {}.getType());
                        parentpart=new ArrayList<>();
                        parentpart.add("Select ParentId's");
                        for (LocationModals locationModal:locationModals){
                            parentpart.add(locationModal.getLoc_name());
                        }

                        ArrayAdapter<String> adapter =
                                new ArrayAdapter<String>(LocationActivity.this,  R.layout.spinneritem, parentpart);
                        adapter.setDropDownViewResource( R.layout.spinneritem);
                        parents.setAdapter(adapter);
                        parents.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position!=0){
                                    parentid=locationModals.get(position-1).getId();
                                //    Utlity.show_toast(LocationActivity.this,parentid);

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
                Utlity.dismiss_dilog(LocationActivity.this);
                Utlity.show_toast(LocationActivity.this, "Not founded Data");

            }
        });

    }


    private void addlocation(final String parent_id,final String loc_name) {
        Utlity.show_progress(this);
        HashMap<String, String> keys = new HashMap<>();
        keys.put("fobunits_id", respoid);
        keys.put("parent_id", parent_id);
        keys.put("loc_name", loc_name);
        Request result= post( keys, Addlocations);
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Utlity.dismiss_dilog(LocationActivity.this);
            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Utlity.dismiss_dilog(LocationActivity.this);
                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            if (response.isSuccessful()){
                                Utlity.show_toast(LocationActivity.this,object.getInt("success")+" Added Sucessfully");
                                locationdata();
                            }
                            else {
                                Utlity.show_toast(LocationActivity.this,"Unauthorised");
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