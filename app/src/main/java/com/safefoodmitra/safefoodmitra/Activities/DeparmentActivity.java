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
import com.safefoodmitra.safefoodmitra.Adapter.DepartmentAdapter;
import com.safefoodmitra.safefoodmitra.Apis.ApiClients;
import com.safefoodmitra.safefoodmitra.Apis.RetApis;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.DeparmentModals;
import com.safefoodmitra.safefoodmitra.Modals.UnitModals;
import com.safefoodmitra.safefoodmitra.Modals.Validation_custome;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.ActivityDeparmentBinding;

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
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Adddeparments;

public class DeparmentActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityDeparmentBinding deparmentBinding;
    public DepartmentAdapter locationAdapter;
    public List<DeparmentModals> deparmentModals;
    RetApis apiInterface;
    ArrayList<Validation_custome> fileds;

    EditText departmentnme;
    String deparments,unitid,parentid;
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
        deparmentBinding= DataBindingUtil.setContentView(this,R.layout.activity_deparment);
        recyclerView=deparmentBinding.departmentlist;
        norecord=deparmentBinding.noreords;


        if (Utlity.is_online(this)){
            departmentdata();
        }
        else Utlity.show_toast(DeparmentActivity.this,getResources().getString(R.string.nointernet));
        click();

        deparmentBinding.searching.addTextChangedListener(new TextWatcher() {
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
        deparmentBinding.back.setOnClickListener(this::onClick);
        deparmentBinding.addepartment.setOnClickListener(this::onClick);
        deparmentBinding.searchdeparment.setOnClickListener(this::onClick);
        deparmentBinding.cancle.setOnClickListener(this::onClick);
    }
    @SuppressLint("ResourceType")
    @Override
    public void onClick(View view) {
        topanim= AnimationUtils.loadAnimation(this,R.xml.top_animation);

        if (view.getId()==R.id.back){
            onBackPressed();
        }
        else if (view.getId()==R.id.addepartment){
            departmentAleart();
        }

        else if (view.getId()==R.id.searchdeparment){
            deparmentBinding.searclayout.setAnimation(topanim);
            deparmentBinding.searclayout.setVisibility(View.VISIBLE);
            deparmentBinding.searchdeparment.setVisibility(View.GONE);
            deparmentBinding.titles.setVisibility(View.GONE);
        }
        else if (view.getId()==R.id.cancle){
            deparmentBinding.searclayout.setVisibility(View.GONE);
            deparmentBinding.searchdeparment.setVisibility(View.VISIBLE);
            deparmentBinding.titles.setVisibility(View.VISIBLE);
        }
    }

    private void departmentdata() {
        Utlity.show_progress(this);
        apiInterface = ApiClients.getClient().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Deparments(respoid,"Bearer "+ Utlity.get_user(this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        Utlity.dismiss_dilog(DeparmentActivity.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        deparmentModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<DeparmentModals>>() {}.getType());
                        if(deparmentModals.size()!=0) {
                            deparmentBinding.departmentlist.setHasFixedSize(true);
                            deparmentBinding.departmentlist.setVisibility(View.VISIBLE);
                            deparmentBinding.noreords.setVisibility(View.GONE);
                            deparmentBinding.departmentlist.setLayoutManager(new LinearLayoutManager(DeparmentActivity.this, RecyclerView.VERTICAL,false));
                            locationAdapter = new DepartmentAdapter(DeparmentActivity.this, deparmentModals);
                            deparmentBinding.departmentlist.setAdapter(locationAdapter);
                        }
                        else
                        {
                            deparmentBinding.noreords.setVisibility(View.VISIBLE);
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog(DeparmentActivity.this);

            }
        });

    }

    public void departmentAleart() {

        final Dialog dialog = new Dialog(DeparmentActivity.this);
        final TextView save;
        final ImageButton cancle;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.addeparment);
        dialog.getWindow().setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
        departmentnme=dialog.findViewById(R.id.adeparmentsnme);
       // fobunits=dialog.findViewById(R.id.fobunits);
        parents=dialog.findViewById(R.id.parentlists);
        save=dialog.findViewById(R.id.adeparments);
        cancle=dialog.findViewById(R.id.cancle);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

       /* fobunits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                deparments=departmentnme.getText().toString();
                fileds = new ArrayList<>();
                fileds.add(new Validation_custome("text", departmentnme));
                if (Utlity.validation(DeparmentActivity.this, fileds)) {
                    if (Utlity.is_online(DeparmentActivity.this)){
                        adeparment(parentid,deparments);
                        dialog.dismiss();
                    }
                    else {
                        Utlity.show_toast(DeparmentActivity.this, getResources().getString(R.string.nointernet));

                    }

                }

            }
        });


        dialog.show();
       // unitstypes();
        parentstype();
    }

    private void parentstype() {
        Utlity.show_progress(this);
        apiInterface = ApiClients.getClient().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Departmentype(respoid,"Bearer "+ Utlity.get_user(this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        Utlity.dismiss_dilog(DeparmentActivity.this);

                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        deparmentModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<DeparmentModals>>() {}.getType());
                        parentpart=new ArrayList<>();
                        parentpart.add("Select ParentId's");
                        for (DeparmentModals deparmentModal:deparmentModals){
                            parentpart.add(deparmentModal.getDept_name());
                        }

                        ArrayAdapter<String> adapter =
                                new ArrayAdapter<String>(DeparmentActivity.this,  R.layout.spinneritem, parentpart);
                        adapter.setDropDownViewResource( R.layout.spinneritem);
                        parents.setAdapter(adapter);

                        parents.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position!=0){
                                    parentid=deparmentModals.get(position-1).getId();
                                    Utlity.show_toast(DeparmentActivity.this,parentid);
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
                Utlity.dismiss_dilog(DeparmentActivity.this);
                Utlity.show_toast(DeparmentActivity.this, "Not founded Data");

            }
        });

    }


    private void adeparment(final String parent_id,final String dept_name) {
        Utlity.show_progress(this);
        HashMap<String, String> keys = new HashMap<>();
        keys.put("fobunits_id", respoid);
        keys.put("parent_id", parent_id);
        keys.put("dept_name", dept_name);
        Request result= post( keys, Adddeparments);
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Utlity.dismiss_dilog(DeparmentActivity.this);
            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Utlity.dismiss_dilog(DeparmentActivity.this);
                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            if (response.isSuccessful()){
                                Utlity.show_toast(DeparmentActivity.this,object.getInt("success")+" Added Sucessfully");
                                departmentdata();
                            }
                            else {
                                Utlity.show_toast(DeparmentActivity.this,"Unauthorised");
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