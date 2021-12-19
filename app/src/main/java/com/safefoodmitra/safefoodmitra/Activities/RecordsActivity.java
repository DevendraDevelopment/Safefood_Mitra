
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

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.reflect.TypeToken;
import com.safefoodmitra.safefoodmitra.Adapter.InspectResponsibilityAdapter;
import com.safefoodmitra.safefoodmitra.Adapter.RecordAdapter;
import com.safefoodmitra.safefoodmitra.Adapter.RecordAdapter1;
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
import com.safefoodmitra.safefoodmitra.Modals.UnitModals;
import com.safefoodmitra.safefoodmitra.Modals.Validation_custome;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.ActivityRecordsBinding;

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
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Addrecords;

public class RecordsActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityRecordsBinding recordsBinding;
    RetApis apiInterface;
    ArrayList<Validation_custome> fileds;
    public RecordAdapter recordAdapter;
    public List<RecordModals> recordModals;
    List<ListItem> consolidatedList = new ArrayList<>();

    EditText recordnnme;
    String records,deptid;
    Spinner departments;
    ArrayList<String> fobpart;
    List<RespoModal>respoModals;
    Animation topanim;
    public static RecyclerView recyclerView;
    public static TextView norecord;
    List<InspectrespoModals>inspectrespoModals;
    RecordAdapter1 recordAdapter1;
    SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recordsBinding= DataBindingUtil.setContentView(this,R.layout.activity_records);
        recyclerView=recordsBinding.recordlist;
        norecord=recordsBinding.noreords;
        pref = this.getSharedPreferences("MyG9", MODE_PRIVATE);
        click();
        if (Utlity.is_online(this)){
            recorddata();
        }
        else Utlity.show_toast(RecordsActivity.this,getResources().getString(R.string.nointernet));

        inspectrespoModals=new ArrayList<>();
        recordsBinding.recordlist.setVisibility(View.VISIBLE);
        recordsBinding.recordlist.setLayoutManager(new LinearLayoutManager(RecordsActivity.this, RecyclerView.VERTICAL, false));
        recordAdapter1 = new RecordAdapter1(RecordsActivity.this, inspectrespoModals);
        recordsBinding.recordlist.setAdapter(recordAdapter1);

        recordsBinding.searching.addTextChangedListener(new TextWatcher() {
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
                    recordAdapter1.setCacheMenuRes(inspectrespoModals);
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
                    recordAdapter1.setCacheMenuRes(cacheMenuResList);

                }
              /*  if (recordAdapter != null) {
                    recordAdapter.getFilter().filter(charSequence);
                } else {
                    Log.d("filter", "no filter availible");
                }

               */


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void click(){
        recordsBinding.back.setOnClickListener(this::onClick);
        recordsBinding.addrecord.setOnClickListener(this::onClick);
        recordsBinding.searchrecord.setOnClickListener(this::onClick);
        recordsBinding.cancle.setOnClickListener(this::onClick);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onClick(View view) {
        topanim= AnimationUtils.loadAnimation(this,R.xml.top_animation);


        if (view.getId()==R.id.back){
            onBackPressed();
        }
        else if (view.getId()==R.id.addrecord){
            recordAleart();
        }
        else if (view.getId()==R.id.searchrecord){
            recordsBinding.searclayout.setAnimation(topanim);
            recordsBinding.searclayout.setVisibility(View.VISIBLE);
            recordsBinding.searchrecord.setVisibility(View.GONE);
            recordsBinding.titles.setVisibility(View.GONE);
        }
        else if (view.getId()==R.id.cancle){
            recordsBinding.searclayout.setVisibility(View.GONE);
            recordsBinding.searchrecord.setVisibility(View.VISIBLE);
            recordsBinding.titles.setVisibility(View.VISIBLE);
        }

    }

    private void recorddata() {
        Utlity.show_progress(this);
        final HashMap<String,List<InspectrespoModals2>> map = new HashMap<>();
        apiInterface = ApiClients.getClient().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Records(respoid,"Bearer "+ Utlity.get_user(this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        Utlity.dismiss_dilog(RecordsActivity.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
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
                        Utlity.dismiss_dilog(RecordsActivity.this);
                        recordAdapter1.notifyDataSetChanged();
                        /*recordModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<RecordModals>>() {}.getType());
                        if(recordModals.size()!=0) {
                            HashMap<String, List<RecordModals>> groupedHashMap = groupDataIntoHashMap(recordModals);

                            for (String date : groupedHashMap.keySet()) {
                                DepartItem dateItem = new DepartItem();
                                dateItem.setDept_name(date);
                                consolidatedList.add(dateItem);

                                for (RecordModals record : groupedHashMap.get(date)) {
                                    Item generalItem = new Item();
                                    generalItem.setPojoOfJsonArray(record);//setBookingDataTabs(bookingDataTabs);
                                    consolidatedList.add(generalItem);
                                }
                            }

                            recordsBinding.recordlist.setHasFixedSize(true);
                            recordsBinding.recordlist.setLayoutManager(new LinearLayoutManager(RecordsActivity.this, RecyclerView.VERTICAL,false));
                            recordAdapter = new RecordAdapter(RecordsActivity.this, consolidatedList,recordModals);
                            recordsBinding.recordlist.setAdapter(recordAdapter);
                           // recordsBinding.recordlist.setNestedScrollingEnabled(false);

                        }

                        else
                        {
                            recordsBinding.noreords.setVisibility(View.VISIBLE);
                        }

                         */


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog(RecordsActivity.this);

            }
        });

    }

    public void recordAleart() {
        final Dialog dialog = new Dialog(RecordsActivity.this);
        final TextView save;
        final ImageButton cancle;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.addrecords);
        dialog.getWindow().setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
        recordnnme=dialog.findViewById(R.id.addrecordnme);
        save=dialog.findViewById(R.id.addrecords);
        cancle=dialog.findViewById(R.id.cancle);
        departments=dialog.findViewById(R.id.departmentid);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                records=recordnnme.getText().toString();
                fileds = new ArrayList<>();
                fileds.add(new Validation_custome("text", recordnnme));
                if (Utlity.validation(RecordsActivity.this, fileds)) {
                    if (Utlity.is_online(RecordsActivity.this)){
                        addrecord(records);
                        dialog.dismiss();
                    }
                    else {
                        Utlity.show_toast(RecordsActivity.this, getResources().getString(R.string.nointernet));

                    }

                }

            }
        });


        dialog.show();
        departments();
    }

    private void departments() {
        Utlity.show_progress(this);
        String respoid1 = pref.getString("respoid",null);
        apiInterface = ApiClients.getClient1().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Depatlist(respoid1,"Bearer "+ Utlity.get_user(this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        Utlity.dismiss_dilog(RecordsActivity.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        respoModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<RespoModal>>() {}.getType());
                        fobpart=new ArrayList<>();
                        fobpart.add("Select Deaprtment Id");
                        for (RespoModal respoModal:respoModals){
                            fobpart.add(respoModal.getDept_name());
                        }

                        ArrayAdapter<String> adapter1 =
                                new ArrayAdapter<String>(RecordsActivity.this,  R.layout.spinneritem, fobpart);
                        adapter1.setDropDownViewResource( R.layout.spinneritem);
                        departments.setAdapter(adapter1);

                        departments.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position!=0){
                                    deptid=respoModals.get(position-1).getId();
                                    Utlity.show_toast(RecordsActivity.this, deptid);

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
                Utlity.dismiss_dilog(RecordsActivity.this);
                Utlity.show_toast(RecordsActivity.this, "Not Founded Data");

            }
        });

    }

    private void addrecord(final String record_name) {
        Utlity.show_progress(this);
        HashMap<String, String> keys = new HashMap<>();
        keys.put("fobunits_id", respoid);
        keys.put("record_name", record_name);
        keys.put("departments_id", deptid);
        Request result= post( keys, Addrecords);
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Utlity.dismiss_dilog(RecordsActivity.this);
            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Utlity.dismiss_dilog(RecordsActivity.this);
                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            if (response.isSuccessful()){
                                Utlity.show_toast(RecordsActivity.this,object.getInt("success")+" Added Sucessfully");
                               // finish();
                               // recorddata();
                                Intent intent= getIntent();
                                finish();
                                startActivity(intent);
                            }
                            else {
                                Utlity.show_toast(RecordsActivity.this,"Unauthorised");
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

    private HashMap<String, List<RecordModals>> groupDataIntoHashMap(List<RecordModals> listOfPojosOfJsonArray) {

        HashMap<String, List<RecordModals>> groupedHashMap = new HashMap<>();

        for (RecordModals pojoOfJsonArray : listOfPojosOfJsonArray) {

            String hashMapKey = pojoOfJsonArray.getDept_name();

            if (groupedHashMap.containsKey(hashMapKey)) {
                // The key is already in the HashMap; add the pojo object
                // against the existing key.
                groupedHashMap.get(hashMapKey).add(pojoOfJsonArray);
            } else {
                // The key is not there in the HashMap; create a new key-value pair
                List<RecordModals> list = new ArrayList<>();
                list.add(pojoOfJsonArray);
                groupedHashMap.put(hashMapKey, list);
            }
        }


        return groupedHashMap;
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
        super.onBackPressed();
    }
}