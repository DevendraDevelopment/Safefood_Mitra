package com.safefoodmitra.safefoodmitra.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.safefoodmitra.safefoodmitra.Adapter.CleaningMaintenanceAdapter;
import com.safefoodmitra.safefoodmitra.Adapter.CustomArrayAdapter;
import com.safefoodmitra.safefoodmitra.Adapter.SentsAdapter;
import com.safefoodmitra.safefoodmitra.Apis.ApiClients;
import com.safefoodmitra.safefoodmitra.Apis.RetApis;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.CleaningMaintModel;
import com.safefoodmitra.safefoodmitra.Modals.ConcernModal;
import com.safefoodmitra.safefoodmitra.Modals.EqupmentModals;
import com.safefoodmitra.safefoodmitra.Modals.LocationModal;
import com.safefoodmitra.safefoodmitra.Modals.LocationModals;
import com.safefoodmitra.safefoodmitra.Modals.RespoModal;
import com.safefoodmitra.safefoodmitra.Modals.SentsModals;
import com.safefoodmitra.safefoodmitra.Modals.ZoneModals;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.ActivityCMFFiltterBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.ResponseBody;

import static com.safefoodmitra.safefoodmitra.Fragments.SentFragment.norecord;
import static com.safefoodmitra.safefoodmitra.Fragments.SentFragment.sentlist;

public class CMFFiltterActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityCMFFiltterBinding cmfFiltterBinding;
    ArrayList<String> respolist;
    public List<LocationModals> locationModals;
    ArrayList<String> locationlist;
    List<RespoModal> respoModals;
    ArrayList<String> selecttypecfm;
    ArrayList<String> equpmentlist;
    ArrayList<String> zonelist;
    List<ZoneModals> zoneModals;
    public List<LocationModals> sublocationModals;
    ArrayList<String> sublocationlist;
    public ArrayList<EqupmentModals> equpmentModals;
    String respoid1, cmflocid, cmfzoneid, cmfsublocid, cmfrespoid,cmftype,cmfequid,searchdata;
    SharedPreferences pref;

    RetApis apiInterface;
    List<CleaningMaintModel> cleaningMaintModels;
    CleaningMaintenanceAdapter cleaningMaintenanceAdapter;
    Spinner status, selcttypecfm, zonecfm, sublocationcfm, equipmentcfm, responsiblitycfm, locationcfm;
    public static String locatioid = "", responseid = "", converid = "", valuepos, onefromdate, twofromdate, spinnervalue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cmfFiltterBinding = DataBindingUtil.setContentView(this, R.layout.activity_c_m_f_filtter);
        status = cmfFiltterBinding.status;
        selcttypecfm = cmfFiltterBinding.selecttype;
        zonecfm = cmfFiltterBinding.zonecfm;
        sublocationcfm = cmfFiltterBinding.subloctioncfm;
        responsiblitycfm = cmfFiltterBinding.responsbility;
        equipmentcfm = cmfFiltterBinding.equipmentcfm;
        locationcfm = cmfFiltterBinding.locations;
        pref = this.getSharedPreferences("MyG9", MODE_PRIVATE);
        respoid1 = pref.getString("respoid", null);
        if (Utlity.is_online(this)) {
            selecttype();
            seletzone();
            seletloction();
            seletsubloction();
            seletequipment();
            seletresponsibility();
        } else {
            Utlity.show_toast(CMFFiltterActivity.this, getResources().getString(R.string.nointernet));
        }

        List<String> categories = new ArrayList<String>();
        categories.add("All Status");
        categories.add("Missed");
        categories.add("Active");
        categories.add("Inactive");
        categories.add("Verified");
        categories.add("Completed");
       categories.add("Cancelled");
        ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinneritem, categories);
        aa.setDropDownViewResource(R.layout.spinneritem);
        status.setAdapter(aa);
        status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // if (position!=0){
                valuepos = categories.get(position);
                //Utlity.show_toast(InspectionActivity.this,valuepos);

                if (valuepos.equals("Missed")) {
                    spinnervalue = "Missed";
                    // Utlity.show_toast(InspectionActivity.this, spinnervalue);
                } else if (valuepos.equals("Active")) {
                    spinnervalue = "Active";
                    // Utlity.show_toast(InspectionActivity.this, spinnervalue);
                } else if (valuepos.equals("Inactive")) {
                    spinnervalue = "Inactive";
                    // Utlity.show_toast(InspectionActivity.this, spinnervalue);

                } else if (valuepos.equals("Verified")) {
                    spinnervalue = "Verified";
                    // Utlity.show_toast(InspectionActivity.this, spinnervalue);

                } else if (valuepos.equals("Completed")) {
                    spinnervalue = "Completed";
                    // Utlity.show_toast(InspectionActivity.this, spinnervalue);

                }
                else if (valuepos.equals("Cancelled")) {
                    spinnervalue = "Cancelled";
                    // Utlity.show_toast(InspectionActivity.this, spinnervalue);
                }else if (valuepos.equals("All Status")) {
                    spinnervalue = null;
                }
            }
            //  }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        click();
    }
    private void selecttype() {
        selecttypecfm = new ArrayList<>();
        selecttypecfm.add("Select Type");
        selecttypecfm.add("Cleaning");
        selecttypecfm.add("Fumigation");
        selecttypecfm.add("Maintenance");

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, R.layout.spinneritem, selecttypecfm);
        adapter.setDropDownViewResource(R.layout.spinneritem);
        selcttypecfm.setAdapter(adapter);

        selcttypecfm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cmftype = selcttypecfm.getItemAtPosition(position).toString();
                if (position == 0){
                    cmfFiltterBinding.linearequipment.setVisibility(View.GONE);
                    cmfequid = null;
                    cmftype = null;
                }else if (position == 2){
                    cmfFiltterBinding.linearequipment.setVisibility(View.GONE);
                    cmfequid = null;
                }else {
                    cmfFiltterBinding.linearequipment.setVisibility(View.VISIBLE);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }
    private void click() {
        cmfFiltterBinding.fromdtebtn.setOnClickListener(this);
        cmfFiltterBinding.todtebtn.setOnClickListener(this);
        cmfFiltterBinding.btnFilter.setOnClickListener(this);
        cmfFiltterBinding.filter.setOnClickListener(this);
        cmfFiltterBinding.back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fromdtebtn) {
             show_date_picker(this,cmfFiltterBinding.firstdateshow);
        } else if (v.getId() == R.id.todtebtn) {
            show_date_picker1(this,cmfFiltterBinding.seconddateshow);
        }else if (v.getId()==R.id.btnFilter){
            if (Utlity.is_online(this)) {
                if (onefromdate!=null || twofromdate!=null || spinnervalue!=null || cmftype!=null || cmfzoneid!=null || cmflocid!=null || cmfsublocid!=null || cmfrespoid!=null || cmfequid!=null ){
                    cleaningMaintModels = null;
                    searchdata = "1";
                    getCMFList(onefromdate,twofromdate,spinnervalue,cmftype,cmfzoneid,cmflocid,cmfsublocid,cmfrespoid,cmfequid,searchdata);
                }

            } else {
                Utlity.show_toast(CMFFiltterActivity.this, getResources().getString(R.string.nointernet));
            }

        }else if (v.getId()==R.id.filter){
            startActivity(new Intent(CMFFiltterActivity.this,CleaningMaintenanceActivity.class));
        }else if (v.getId()==R.id.back){
            if (Utlity.get_user(this).getUserroles_id().equalsIgnoreCase("2")){
                startActivity(new Intent(this,AdminMainActivity.class));
                finishAffinity();

            }
            else if (Utlity.get_user(this).getUserroles_id().equalsIgnoreCase("3")){
                startActivity(new Intent(this,UserMainActivity.class));
                finishAffinity();

            }
        }
    }

    private void getCMFList(String firstdate,String seconddate,String status,String type,String zoneid,String locid,String sublocid,String respoid,String equid,String searchdata) {
        Utlity.show_progress(this);
        String respoid1 = pref.getString("respoid",null);
        apiInterface = ApiClients.getClient1().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.CMFFillterList(respoid1,"Bearer "+ Utlity.get_user(this).getToken(),firstdate,seconddate,status,type,zoneid,locid,sublocid,respoid,equid,searchdata);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        Utlity.dismiss_dilog(CMFFiltterActivity.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        cleaningMaintModels = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<CleaningMaintModel>>() {}.getType());
                        if(cleaningMaintModels.size()!=0) {

                            cmfFiltterBinding.cmfflitterlist.setVisibility(View.VISIBLE);
                            cmfFiltterBinding.cmfflitterdata.setVisibility(View.GONE);
                            cmfFiltterBinding.noreords.setVisibility(View.GONE);

                            cmfFiltterBinding.ceaninglist.setHasFixedSize(true);
                            cmfFiltterBinding.ceaninglist.setLayoutManager(new LinearLayoutManager(CMFFiltterActivity.this, RecyclerView.VERTICAL,false));
                            cleaningMaintenanceAdapter = new CleaningMaintenanceAdapter(CMFFiltterActivity.this,cleaningMaintModels);
                            cmfFiltterBinding.ceaninglist.setAdapter(cleaningMaintenanceAdapter);
                            onefromdate=null;
                            twofromdate = null;
                            spinnervalue = null;
                            cmftype = null;
                            cmfzoneid = null;
                            cmflocid = null;
                            cmfsublocid = null;
                            cmfrespoid = null;
                            cmfequid = null;
                        }else if (cleaningMaintModels.size()==0){
                            cmfFiltterBinding.ceaninglist.setVisibility(View.VISIBLE);
                            cmfFiltterBinding.noreords.setVisibility(View.VISIBLE);
                            cmfFiltterBinding.cmfflitterlist.setVisibility(View.VISIBLE);
                            cmfFiltterBinding.cmfflitterdata.setVisibility(View.GONE);
                            cmfFiltterBinding.ceaninglist.setHasFixedSize(true);
                            cmfFiltterBinding.ceaninglist.setLayoutManager(new LinearLayoutManager(CMFFiltterActivity.this, RecyclerView.VERTICAL,false));
                            cleaningMaintenanceAdapter = new CleaningMaintenanceAdapter(CMFFiltterActivity.this,cleaningMaintModels);
                            cmfFiltterBinding.ceaninglist.setAdapter(cleaningMaintenanceAdapter);
                            onefromdate=null;
                            twofromdate = null;
                            spinnervalue = null;
                            cmftype = null;
                            cmfzoneid = null;
                            cmflocid = null;
                            cmfsublocid = null;
                            cmfrespoid = null;
                            cmfequid = null;
                        }
                        else
                        {
                            cmfFiltterBinding.noreords.setVisibility(View.VISIBLE);
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog(CMFFiltterActivity.this);

            }
        });
    }

    private void seletloction() {
        String respoid1 = pref.getString("respoid", null);
        apiInterface = ApiClients.getClient().create(RetApis.class);
        retrofit2.Call<ResponseBody> call = apiInterface.Locationstype(respoid1, "Bearer " + Utlity.get_user(this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        //  Utlity.dismiss_dilog(CleaningMaintenanceActivity.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>", apidata);
                        JSONObject object = new JSONObject(apidata);
                        locationModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<LocationModals>>() {
                        }.getType());
                        locationlist = new ArrayList<>();
                        locationlist.add("Select Location");
                        for (LocationModals locationModals1 : locationModals) {
                            locationlist.add(locationModals1.getLoc_name());
                        }
                        ArrayAdapter<String> adapter =
                                new ArrayAdapter<String>(CMFFiltterActivity.this, R.layout.spinneritem, locationlist);
                        adapter.setDropDownViewResource(R.layout.spinneritem);
                        locationcfm.setAdapter(adapter);

                        locationcfm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position != 0) {
                                    cmflocid = locationModals.get(position - 1).getId();
                                    cmfFiltterBinding.linearsublocation.setVisibility(View.VISIBLE);
                                    seletsubloction();
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

    private void seletresponsibility() {
       Utlity.show_progress(this);
        String respoid1 = pref.getString("respoid", null);
        apiInterface = ApiClients.getClient1().create(RetApis.class);
        retrofit2.Call<ResponseBody> call = apiInterface.Depatlist(respoid1, "Bearer " + Utlity.get_user(this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        Utlity.dismiss_dilog(CMFFiltterActivity.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>", apidata);
                        JSONObject object = new JSONObject(apidata);
                        respoModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<RespoModal>>() {
                        }.getType());
                        respolist = new ArrayList<>();
                        respolist.add("Select Reponsibility");
                        for (RespoModal respoModal : respoModals) {
                            respolist.add(respoModal.getDept_name());
                        }
                        ArrayAdapter<String> adapter =
                                new ArrayAdapter<String>(CMFFiltterActivity.this, R.layout.spinneritem, respolist);
                        adapter.setDropDownViewResource(R.layout.spinneritem);
                        responsiblitycfm.setAdapter(adapter);
                        responsiblitycfm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position != 0) {
                                    cmfrespoid = respoModals.get(position - 1).getId();
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
                Utlity.dismiss_dilog(CMFFiltterActivity.this);

            }
        });

    }

    private void seletequipment() {

        String respoid1 = pref.getString("respoid", null);
        apiInterface = ApiClients.getClient().create(RetApis.class);
        retrofit2.Call<ResponseBody> call = apiInterface.Equpments(respoid1, "Bearer " + Utlity.get_user(this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {

                        //   Utlity.dismiss_dilog(CleaningMaintenanceActivity.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>", apidata);
                        JSONObject object = new JSONObject(apidata);
                        equpmentModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<EqupmentModals>>() {
                        }.getType());

                        equpmentlist = new ArrayList<>();
                        equpmentlist.add("Select Equipment");
                        for (EqupmentModals equpmentModals1 : equpmentModals) {
                            equpmentlist.add(equpmentModals1.getEquip_name());
                        }

                        ArrayAdapter<String> adapter =
                                new ArrayAdapter<String>(CMFFiltterActivity.this, R.layout.spinneritem, equpmentlist);
                        adapter.setDropDownViewResource(R.layout.spinneritem);
                        equipmentcfm.setAdapter(adapter);
                        equipmentcfm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position != 0) {
                                     cmfequid = equpmentModals.get(position-1).getId();

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

                // Utlity.dismiss_dilog(CleaningMaintenanceActivity.this);

            }
        });
    }

    private void seletsubloction() {
        String respoid1 = pref.getString("respoid", null);
        apiInterface = ApiClients.getClient().create(RetApis.class);
        retrofit2.Call<ResponseBody> call = apiInterface.SubLocations(respoid1, cmflocid, "Bearer " + Utlity.get_user(this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {

                        apidata = response.body().string();
                        Log.d("responce>>>>>", apidata);
                        JSONObject object = new JSONObject(apidata);
                        sublocationModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<LocationModals>>() {
                        }.getType());
                        sublocationlist = new ArrayList<>();
                        sublocationlist.add("Select SubLocation");
                        for (LocationModals locationModals1 : sublocationModals) {
                            sublocationlist.add(locationModals1.getLoc_name());
                        }
                        ArrayAdapter<String> adapter =
                                new ArrayAdapter<String>(CMFFiltterActivity.this, R.layout.spinneritem, sublocationlist);
                        adapter.setDropDownViewResource(R.layout.spinneritem);
                        sublocationcfm.setAdapter(adapter);

                        sublocationcfm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position != 0) {
                                    cmfsublocid = sublocationModals.get(position - 1).getId();
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


            }
        });

    }

    private void seletzone() {
        String respoid1 = pref.getString("respoid", null);
        apiInterface = ApiClients.getClient().create(RetApis.class);
        retrofit2.Call<ResponseBody> call = apiInterface.Zones(respoid1, "Bearer " + Utlity.get_user(this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        // Utlity.dismiss_dilog(CleaningMaintenanceActivity.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>", apidata);
                        JSONObject object = new JSONObject(apidata);
                        zoneModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<ZoneModals>>() {
                        }.getType());
                        zonelist = new ArrayList<>();
                        zonelist.add("Select Zone");
                        for (ZoneModals zoneModals1 : zoneModals) {
                            zonelist.add(zoneModals1.getZone_name());
                        }
                        ArrayAdapter<String> adapter =
                                new ArrayAdapter<String>(CMFFiltterActivity.this, R.layout.spinneritem, zonelist);
                        adapter.setDropDownViewResource(R.layout.spinneritem);
                        zonecfm.setAdapter(adapter);
                        zonecfm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position != 0) {
                                    cmfzoneid = zoneModals.get(position - 1).getId();
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



    public void show_date_picker(Activity activity, final TextView tv) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker arg0, int year, int month, int day_of_month) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, (month));
                calendar.set(Calendar.DAY_OF_MONTH, day_of_month);
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                tv.setText(sdf.format(calendar.getTime()));
                onefromdate = String.valueOf(tv.getText());


            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        calendar.add(Calendar.YEAR, 0);
        //dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());// TODO: used to hide future date,month and year
       // dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());// TODO: used to hide past date,month and year
        dialog.show();
    }

    public void show_date_picker1(Activity activity, final TextView tv) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker arg0, int year, int month, int day_of_month) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, (month));
                calendar.set(Calendar.DAY_OF_MONTH, day_of_month);
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                tv.setText(sdf.format(calendar.getTime()));
                twofromdate = String.valueOf(tv.getText());

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        calendar.add(Calendar.YEAR, 0);
       // dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());// TODO: used to hide future date,month and year
       // dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());// TODO: used to hide past date,month and year
        dialog.show();
    }
}