package com.safefoodmitra.safefoodmitra.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.safefoodmitra.safefoodmitra.Adapter.CleaningMaintenanceAdapter;
import com.safefoodmitra.safefoodmitra.Adapter.CustomArrayAdapter;
import com.safefoodmitra.safefoodmitra.Adapter.EqupmentAdapter;
import com.safefoodmitra.safefoodmitra.Adapter.LocationAdapter;
import com.safefoodmitra.safefoodmitra.Adapter.ZoneAdapter;
import com.safefoodmitra.safefoodmitra.Apis.ApiClients;
import com.safefoodmitra.safefoodmitra.Apis.RetApis;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.CleaningMaintModel;
import com.safefoodmitra.safefoodmitra.Modals.EqupmentModals;
import com.safefoodmitra.safefoodmitra.Modals.InspectrespoModals;
import com.safefoodmitra.safefoodmitra.Modals.InspectrespoModals2;
import com.safefoodmitra.safefoodmitra.Modals.LocationModals;
import com.safefoodmitra.safefoodmitra.Modals.RespoModal;
import com.safefoodmitra.safefoodmitra.Modals.ZoneModals;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.ActivityCleaningMaintenanceBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.safefoodmitra.safefoodmitra.Activities.AdminMainActivity.respoid;
import static com.safefoodmitra.safefoodmitra.Adapter.CustomArrayAdapter.arraylistequpment;


public class CleaningMaintenanceActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityCleaningMaintenanceBinding cleaningMaintenanceBinding;
    CleaningMaintenanceAdapter cleaningMaintenanceAdapter;
    Spinner selcttypecfm,zonecfm,locationcfm,sublocationcfm,equipmentcfm,responsiblitycfm;
    ArrayList<String> selecttypecfm;
    List<CleaningMaintModel> cleaningMaintModels;
    RetApis apiInterface;
    String displedate,currentedate,prevdate,enddate,comment;
    SharedPreferences pref;
    ArrayList<String> zonelist;
    List<ZoneModals> zoneModals;
    public List<LocationModals> locationModals;
    ArrayList<String> locationlist;
    public List<LocationModals> sublocationModals;
    ArrayList<String> sublocationlist;
    public ArrayList<EqupmentModals> equpmentModals;
    List<RespoModal>respoModals;
    ArrayList<String> respolist;
    LinearLayout linearsublocation,linearequipment,linearcommentbox;
    TextView tvsave,tvdate,tvtime,tvstartdate;
    EditText etrepeatedate,etcomment;
    String cmftype,cmfzoneid,cmflocid,cmfsublocid,cmfequid,cmfrespoid,cmfreponce,cmfoncedate,cmftime,cmfstartdate,cmfrepeateone;
    private static Dialog progressDialog = null;
    ImageView cancle;
    String respoid1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cleaningMaintenanceBinding = DataBindingUtil.setContentView(this,R.layout.activity_cleaning_maintenance);
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        progressDialog = new Dialog(CleaningMaintenanceActivity.this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        progressDialog.setContentView(R.layout.custom_progress);
        pref = this.getSharedPreferences("MyG9", MODE_PRIVATE);
        respoid1 = pref.getString("respoid",null);
        cmflocid="1";
        if (Utlity.is_online(this)){
            getdatacleaning();


        }
        else {
            Utlity.show_toast(CleaningMaintenanceActivity.this, getResources().getString(R.string.nointernet));
        }

        if (Utlity.get_user(this).getUserroles_id().equalsIgnoreCase("2")){
            cleaningMaintenanceBinding.addcleaning.setVisibility(View.VISIBLE);

        }
        else if (Utlity.get_user(this).getUserroles_id().equalsIgnoreCase("3")){
            cleaningMaintenanceBinding.addcleaning.setVisibility(View.GONE);
        }
        click();


    }

    private void getdatacleaning() {
        Utlity.show_progress(this);
        String respoid1 = pref.getString("respoid",null);
        apiInterface = ApiClients.getClient1().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.CFMLisetget(respoid1,"Bearer "+ Utlity.get_user(this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        Utlity.dismiss_dilog(CleaningMaintenanceActivity.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        displedate = object.getString("disptitle");
                        currentedate = object.getString("currentdate");
                        prevdate = object.getString("prevdate");
                        enddate = object.getString("enddate");
                        cleaningMaintModels = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<CleaningMaintModel>>() {}.getType());
                        if(cleaningMaintModels.size()!=0) {
                            cleaningMaintenanceBinding.linearcmf.setVisibility(View.VISIBLE);
                            cleaningMaintenanceBinding.displedate.setText(displedate);
                            cleaningMaintenanceBinding.ceaninglist.setHasFixedSize(true);
                            cleaningMaintenanceBinding.ceaninglist.setLayoutManager(new LinearLayoutManager(CleaningMaintenanceActivity.this, RecyclerView.VERTICAL,false));
                            cleaningMaintenanceAdapter = new CleaningMaintenanceAdapter(CleaningMaintenanceActivity.this,cleaningMaintModels);
                            cleaningMaintenanceBinding.ceaninglist.setAdapter(cleaningMaintenanceAdapter);
                        }
                        else
                        {
                            cleaningMaintenanceBinding.noreords.setVisibility(View.VISIBLE);
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog(CleaningMaintenanceActivity.this);

            }
        });
    }


    private void click() {
        cleaningMaintenanceBinding.back.setOnClickListener(this::onClick);
        cleaningMaintenanceBinding.imgbackward.setOnClickListener(this);
        cleaningMaintenanceBinding.imgforward.setOnClickListener(this);
        cleaningMaintenanceBinding.addcleaning.setOnClickListener(this);
        cleaningMaintenanceBinding.refreshimage.setOnClickListener(this);
        cleaningMaintenanceBinding.filter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back){
            if (Utlity.get_user(this).getUserroles_id().equalsIgnoreCase("2")){
                startActivity(new Intent(this,AdminMainActivity.class));
                finishAffinity();

            }
            else if (Utlity.get_user(this).getUserroles_id().equalsIgnoreCase("3")){
                startActivity(new Intent(this,UserMainActivity.class));
                finishAffinity();

            }
        }else if (v.getId() == R.id.imgbackward){
            data(prevdate);
        }else if (v.getId() == R.id.imgforward){
            data(enddate);
        }else if (v.getId() == R.id.addcleaning){
            showdata();
            selecttype();
            seletzone();
            seletloction();
            seletsubloction(cmflocid);
            seletequipment();
            seletresponsibility();

        }else if (v.getId() == R.id.refreshimage){
            getdatacleaning();
        }else if (v.getId() == R.id.filter){
            startActivity(new Intent(CleaningMaintenanceActivity.this,CMFFiltterActivity.class));
        }
    }

    private void seletresponsibility() {
        progressDialog.show();
        String respoid1 = pref.getString("respoid",null);
        apiInterface = ApiClients.getClient1().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Depatlist(respoid1,"Bearer "+ Utlity.get_user(this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                       progressDialog.cancel();
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        respoModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<RespoModal>>() {}.getType());
                        respolist=new ArrayList<>();
                        respolist.add("Select Reponsibility");
                        for (RespoModal respoModal:respoModals){
                            respolist.add(respoModal.getDept_name());
                        }
                        ArrayAdapter<String> adapter =
                                new ArrayAdapter<String>(CleaningMaintenanceActivity.this,  R.layout.spinneritem, respolist);
                        adapter.setDropDownViewResource( R.layout.spinneritem);
                        responsiblitycfm.setAdapter(adapter);
                        responsiblitycfm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position !=0){
                                    cmfrespoid = respoModals.get(position-1).getId();
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
                progressDialog.cancel();

            }
        });

    }

    private void seletequipment() {
        progressDialog.show();
        String respoid1 = pref.getString("respoid",null);
        apiInterface = ApiClients.getClient().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Equpments(respoid1,"Bearer "+ Utlity.get_user(this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        progressDialog.cancel();
                        //   Utlity.dismiss_dilog(CleaningMaintenanceActivity.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        equpmentModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<EqupmentModals>>() {}.getType());
                       //  equpmentModals.add(new EqupmentModals("Select Equipment"));

                       // equpmentlist=new ArrayList<>();
                      //  equpmentlist.add("Select Equipment");
                      //  for (EqupmentModals equpmentModals1:equpmentModals){
                       //     equpmentlist.add(equpmentModals1.getEquip_name());
                       // }
                        String headerText = "Select Equipment";
                        CustomArrayAdapter myAdapter = new CustomArrayAdapter(CleaningMaintenanceActivity.this,headerText, 0,
                                equpmentModals);
                     //   ArrayAdapter<String> adapter =
                         //       new ArrayAdapter<String>(CleaningMaintenanceActivity.this,  R.layout.spinneritem, equpmentlist);
                       // adapter.setDropDownViewResource( R.layout.spinneritem);
                        equipmentcfm.setAdapter(myAdapter);
                      /*  equipmentcfm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position !=0){
                                    cmfequid = equpmentModals.get(position-1).getId();
                                    Utlity.show_toast(CleaningMaintenanceActivity.this,cmfequid);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });*/

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                progressDialog.cancel();
                // Utlity.dismiss_dilog(CleaningMaintenanceActivity.this);

            }
        });
    }

    private void seletsubloction(String cmflocid) {

        progressDialog.show();
        String respoid1 = pref.getString("respoid",null);
        apiInterface = ApiClients.getClient().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.SubLocations(respoid1,cmflocid,"Bearer "+ Utlity.get_user(this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                       progressDialog.cancel();
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        sublocationModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<LocationModals>>() {}.getType());
                        sublocationlist=new ArrayList<>();
                        sublocationlist.add("Select SubLocation");
                        for (LocationModals locationModals1:sublocationModals){
                            sublocationlist.add(locationModals1.getLoc_name());
                        }
                        ArrayAdapter<String> adapter =
                                new ArrayAdapter<String>(CleaningMaintenanceActivity.this,  R.layout.spinneritem, sublocationlist);
                        adapter.setDropDownViewResource( R.layout.spinneritem);
                        sublocationcfm.setAdapter(adapter);

                        sublocationcfm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position !=0){
                                    cmfsublocid = sublocationModals.get(position-1).getId();
                                  //  Utlity.show_toast(CleaningMaintenanceActivity.this,cmfsublocid);
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
                progressDialog.cancel();

            }
        });

    }

    private void seletloction() {
        String respoid1 = pref.getString("respoid",null);
        apiInterface = ApiClients.getClient().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Locationstype(respoid1,"Bearer "+ Utlity.get_user(this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        //  Utlity.dismiss_dilog(CleaningMaintenanceActivity.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        locationModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<LocationModals>>() {}.getType());
                        locationlist=new ArrayList<>();
                        locationlist.add("Select Location");
                        for (LocationModals locationModals1:locationModals){
                            locationlist.add(locationModals1.getLoc_name());
                        }
                        ArrayAdapter<String> adapter =
                                new ArrayAdapter<String>(CleaningMaintenanceActivity.this,  R.layout.spinneritem, locationlist);
                        adapter.setDropDownViewResource( R.layout.spinneritem);
                        locationcfm.setAdapter(adapter);

                        locationcfm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position !=0){
                                    cmflocid = locationModals.get(position-1).getId();
                                    seletsubloction(cmflocid);
                                   // Utlity.show_toast(CleaningMaintenanceActivity.this,cmflocid);
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
                //    Utlity.dismiss_dilog(CleaningMaintenanceActivity.this);

            }
        });

    }

    private void seletzone() {
        String respoid1 = pref.getString("respoid",null);
        apiInterface = ApiClients.getClient().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Zones(respoid1,"Bearer "+ Utlity.get_user(this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        // Utlity.dismiss_dilog(CleaningMaintenanceActivity.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        zoneModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<ZoneModals>>() {}.getType());
                        zonelist=new ArrayList<>();
                        zonelist.add("Select Zone");
                        for (ZoneModals zoneModals1:zoneModals){
                            zonelist.add(zoneModals1.getZone_name());
                        }
                        ArrayAdapter<String> adapter =
                                new ArrayAdapter<String>(CleaningMaintenanceActivity.this,  R.layout.spinneritem, zonelist);
                        adapter.setDropDownViewResource( R.layout.spinneritem);
                        zonecfm.setAdapter(adapter);
                        zonecfm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position !=0){
                                    cmfzoneid = zoneModals.get(position-1).getId();
                                    //Utlity.show_toast(CleaningMaintenanceActivity.this,cmfzoneid);
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
                //  Utlity.dismiss_dilog(CleaningMaintenanceActivity.this);

            }
        });

    }
    private void selecttype() {
        selecttypecfm=new ArrayList<>();
        selecttypecfm.add("Select Type");
        selecttypecfm.add("Cleaning");
        selecttypecfm.add("Fumigation");
        selecttypecfm.add("Maintenance");

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this,  R.layout.spinneritem, selecttypecfm);
        adapter.setDropDownViewResource( R.layout.spinneritem);
        selcttypecfm.setAdapter(adapter);

        selcttypecfm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cmftype = selcttypecfm.getItemAtPosition(position).toString();
                if (position == 0){
                    linearequipment.setVisibility(View.GONE);
                   // linearsublocation.setVisibility(View.GONE);
                    cmfequid = "";
                    linearcommentbox.setVisibility(View.GONE);
                   // cmfsublocid = "";
                }else if (position == 2){
                    linearequipment.setVisibility(View.GONE);
                    linearcommentbox.setVisibility(View.VISIBLE);
                    cmfequid = "";
                }else {
                    linearequipment.setVisibility(View.VISIBLE);
                    linearcommentbox.setVisibility(View.GONE);
                  //  linearsublocation.setVisibility(View.GONE);
                 //   cmfsublocid = "";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }
    private void data(String cerrdate) {
        Utlity.show_progress(this);
        String respoid1 = pref.getString("respoid",null);
        apiInterface = ApiClients.getClient1().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.CFMLisetpost(respoid1,"Bearer "+ Utlity.get_user(this).getToken(),cerrdate);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        Utlity.dismiss_dilog(CleaningMaintenanceActivity.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>", apidata);
                        JSONObject object = new JSONObject(apidata);
                        displedate = object.getString("disptitle");
                        currentedate = object.getString("currentdate");
                        prevdate = object.getString("prevdate");
                        enddate = object.getString("enddate");

                        cleaningMaintModels = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<CleaningMaintModel>>() {
                        }.getType());
                        if (cleaningMaintModels.size() != 0) {
                            cleaningMaintenanceBinding.linearcmf.setVisibility(View.VISIBLE);
                            cleaningMaintenanceBinding.displedate.setText(displedate);
                            cleaningMaintenanceBinding.noreords.setVisibility(View.GONE);
                            cleaningMaintenanceBinding.ceaninglist.setHasFixedSize(true);
                            cleaningMaintenanceBinding.ceaninglist.setLayoutManager(new LinearLayoutManager(CleaningMaintenanceActivity.this, RecyclerView.VERTICAL, false));
                            cleaningMaintenanceAdapter = new CleaningMaintenanceAdapter(CleaningMaintenanceActivity.this, cleaningMaintModels);
                            cleaningMaintenanceBinding.ceaninglist.setAdapter(cleaningMaintenanceAdapter);
                        } else {
                            cleaningMaintenanceBinding.noreords.setVisibility(View.VISIBLE);
                        }


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog(CleaningMaintenanceActivity.this);

            }
        });
    }

    private void showdata(){
        final Dialog dialog = new Dialog(CleaningMaintenanceActivity.this);
        final ImageButton cancle;

        RadioButton rbrepeate,rbonce;
        ImageView imgdate,imgtime,imgdatestart;
        LinearLayout lineardate,linearrepeatedate;

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.cleaninglist);
        dialog.getWindow().setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
        cancle=dialog.findViewById(R.id.cancle);
        selcttypecfm = dialog.findViewById(R.id.selecttype);
        zonecfm = dialog.findViewById(R.id.zonecfm);
        locationcfm = dialog.findViewById(R.id.locationscfm);
        sublocationcfm = dialog.findViewById(R.id.subloctioncfm);
        equipmentcfm = dialog.findViewById(R.id.equipmentcfm);
        responsiblitycfm = dialog.findViewById(R.id.resposibilitycfm);
        linearequipment = dialog.findViewById(R.id.linearequipment);
        linearsublocation = dialog.findViewById(R.id.linearsublocation);
        etrepeatedate = dialog.findViewById(R.id.etrepeatedate);
        etcomment = dialog.findViewById(R.id.commentbox);
        linearcommentbox = dialog.findViewById(R.id.linearcommentbox);
        lineardate = dialog.findViewById(R.id.lineardate);
        imgdate = dialog.findViewById(R.id.imgdate);
        linearrepeatedate = dialog.findViewById(R.id.linearrepeateday);
        imgtime = dialog.findViewById(R.id.imgtime);
        tvdate = dialog.findViewById(R.id.tvdate);
        tvtime = dialog.findViewById(R.id.tvtime);
        rbrepeate = dialog.findViewById(R.id.radiorepeate);
        rbonce = dialog.findViewById(R.id.radioonce);
        tvstartdate = dialog.findViewById(R.id.tvstartdate);
        imgdatestart = dialog.findViewById(R.id.imgdatestart);
        cmfrepeateone = rbrepeate.getText().toString();
        imgdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utlity.show_date_picker(CleaningMaintenanceActivity.this,tvdate);
            }
        });
        imgdatestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utlity.show_date_picker(CleaningMaintenanceActivity.this,tvstartdate);
            }
        });
        imgtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utlity.show_time_picker(CleaningMaintenanceActivity.this,tvtime);
            }
        });
        rbrepeate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    rbonce.setChecked(false);
                    lineardate.setVisibility(View.GONE);
                    linearrepeatedate.setVisibility(View.VISIBLE);
                    cmfrepeateone = rbrepeate.getText().toString();
                }
            }
        });
        rbonce.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    rbrepeate.setChecked(false);
                    lineardate.setVisibility(View.VISIBLE);
                    linearrepeatedate.setVisibility(View.GONE);
                    cmfrepeateone = rbonce.getText().toString();
                }
            }
        });
        tvsave = dialog.findViewById(R.id.tvsave);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        tvsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selcttypecfm.getSelectedItemPosition() == 0){
                    Utlity.show_toast(CleaningMaintenanceActivity.this,"Please Select Types");
                }else if (zonecfm.getSelectedItemPosition() == 0){
                    Utlity.show_toast(CleaningMaintenanceActivity.this,"Please Select Zone");
                }else if (locationcfm.getSelectedItemPosition() == 0){
                    Utlity.show_toast(CleaningMaintenanceActivity.this,"Please Select Location");
                }else if (sublocationcfm.getSelectedItemPosition() == 0 && equipmentcfm.getSelectedItemPosition() == 0){
                    if (cmftype.equals("Fumigation")){
                        Utlity.show_toast(CleaningMaintenanceActivity.this,"Please Select SubLocation");
                    }else {
                        Utlity.show_toast(CleaningMaintenanceActivity.this,"Please Select Equipment");
                    }

                }else if (responsiblitycfm.getSelectedItemPosition() == 0){
                    Utlity.show_toast(CleaningMaintenanceActivity.this,"Please Select Responsibility");
                }else if (linearrepeatedate.getVisibility()==View.VISIBLE && etrepeatedate.getText().toString().equals("")){
                    Utlity.show_toast(CleaningMaintenanceActivity.this,"Please Select Repeatedate");
                }
               /* else if (tvtime.getText().toString().equals("")){
                    Utlity.show_toast(CleaningMaintenanceActivity.this,"Please Select Tme");
                }*/
                else if (tvstartdate.getText().toString().equals("")){
                    Utlity.show_toast(CleaningMaintenanceActivity.this,"Please Select StartDate");
                }else if ( tvdate.getText().toString().equals("") && lineardate.getVisibility()==View.VISIBLE){
                    Utlity.show_toast(CleaningMaintenanceActivity.this,"Please Select Date");
                }
                else {
                    savedata();
                    dialog.cancel();
                }

            }
        });
        dialog.show();
    }

    private void savedata() {
        Utlity.show_progress(this);
        Gson gson = new Gson();
        Log.d("responce>>>>>",gson.toJson(arraylistequpment));
        cmfreponce = etrepeatedate.getText().toString();
        cmfoncedate = tvdate.getText().toString();
        cmftime = tvtime.getText().toString();
        cmfstartdate = tvstartdate.getText().toString();
        comment = etcomment.getText().toString();
        HashMap<String,String> hmap = new HashMap<String, String>();
        hmap.put("cmtype",cmftype);
        hmap.put("zoneid",cmfzoneid);
        hmap.put("locatinid",cmflocid);
        hmap.put("sublocatinid",cmfsublocid);
        hmap.put("equipmentid",gson.toJson(arraylistequpment));
        hmap.put("responsibilityid",cmfrespoid);
        hmap.put("taskrepetition",cmfrepeateone);
        hmap.put("repetitiondays",cmfreponce);
        hmap.put("dateassigned",cmfoncedate);
        hmap.put("assigntime",cmftime);
        hmap.put("startedfrom",cmfstartdate);
        hmap.put("taskcomment",comment);

        String respoid1 = pref.getString("respoid",null);
        apiInterface = ApiClients.getClient1().create(RetApis.class);
        Call<ResponseBody> call=apiInterface.CFMSaveLiset(respoid1,"Bearer "+ Utlity.get_user(this).getToken(),hmap);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Utlity.dismiss_dilog(CleaningMaintenanceActivity.this);
                    try {
                        String apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        arraylistequpment.clear();
                        Utlity.show_toast(CleaningMaintenanceActivity.this,response.body().string());
                        Intent intent= getIntent();
                        finish();
                        startActivity(intent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    Utlity.show_toast(CleaningMaintenanceActivity.this,"responce");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog(CleaningMaintenanceActivity.this);
                Utlity.show_toast(CleaningMaintenanceActivity.this,t.getMessage());
            }
        });
    }



}