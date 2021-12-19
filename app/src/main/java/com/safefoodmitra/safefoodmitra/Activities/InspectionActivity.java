package com.safefoodmitra.safefoodmitra.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUriExposedException;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.transition.Explode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
//import com.opencsv.CSVWriter;
import com.safefoodmitra.safefoodmitra.Adapter.SentsAdapter;
import com.safefoodmitra.safefoodmitra.Adapter.Viewadapter;
import com.safefoodmitra.safefoodmitra.Apis.ApiClients;
import com.safefoodmitra.safefoodmitra.Apis.Apis;
import com.safefoodmitra.safefoodmitra.Apis.RetApis;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.ConcernModal;
import com.safefoodmitra.safefoodmitra.Modals.LocationModal;
import com.safefoodmitra.safefoodmitra.Modals.RespoModal;
import com.safefoodmitra.safefoodmitra.Modals.SentsModals;
import com.safefoodmitra.safefoodmitra.Modals.UnitModals;
import com.safefoodmitra.safefoodmitra.Modals.Validation_custome;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.ActivityInspectionBinding;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.zip.GZIPOutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.safefoodmitra.safefoodmitra.Activities.AdminMainActivity.respoid;
import static com.safefoodmitra.safefoodmitra.Fragments.RecivedFragment.norecord1;
import static com.safefoodmitra.safefoodmitra.Fragments.RecivedFragment.receivelist;
import static com.safefoodmitra.safefoodmitra.Fragments.SentFragment.norecord;
import static com.safefoodmitra.safefoodmitra.Fragments.SentFragment.sentlist;
import static org.apache.poi.ss.usermodel.CellStyle.ALIGN_CENTER;
import static org.apache.poi.ss.usermodel.CellStyle.ALIGN_JUSTIFY;

public class InspectionActivity extends AppCompatActivity implements View.OnClickListener  {
    private static final String TAG = "tag";

    ActivityInspectionBinding inspectionBinding;
    Viewadapter viewAdapter;
    RetApis apiInterface;
    public List<SentsModals> sentsModals;
    public static List<SentsModals> sentsModals1;
    Spinner location,department,concern,status;
    TextView filtesave,firstdate,seconddate;
    ImageView cancle,todtebtn,fromdtebtn;
    public static String locatioid="",responseid="",converid="",valuepos,onefromdate,twofromdate,spinnervalue="";
    List<LocationModal>locationModals;
    List<RespoModal>respoModals;
    List<ConcernModal>concernModals;
    ArrayList<String> respolist;
    ArrayList<String> locationlist;
    ArrayList<String> concernlist;
    public SentsAdapter sentsAdapter;
    String path="";
    Workbook workbook;
    ProgressDialog progressBar;
    String respoiduser;
    SharedPreferences pref;
    Sheet sheet;
    public static int valuesall=0,filtercheckvalue=0;
    public static int tabvalue;
    public static TabLayout tabLayout;
    String respoid1;
    FileOutputStream fileOut;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Explode());
        }

        inspectionBinding= DataBindingUtil.setContentView(this,R.layout.activity_inspection);
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        pref = this.getSharedPreferences("MyG9", MODE_PRIVATE);
        respoid1 = pref.getString("respoid",null);
        viewAdapter = new Viewadapter(getSupportFragmentManager());
        inspectionBinding.viewpager.setAdapter(viewAdapter);
        inspectionBinding.inspectiontab.setupWithViewPager(inspectionBinding.viewpager);
        inspectionBinding.inspectiontab.setSelectedTabIndicatorColor(ContextCompat.getColor(this,R.color.black));
        inspectionBinding.inspectiontab.setTabTextColors(Color.parseColor("#9C9C99"), Color.parseColor("#056314"));
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setMessage(getResources().getString(R.string.loading));
        respoiduser=pref.getString("respoid",null);
       // Utlity.show_toast(InspectionActivity.this,respoiduser);
        click();
        tabLayout=inspectionBinding.inspectiontab;
        inspectionBinding.swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (inspectionBinding.inspectiontab.getTabAt(0).isSelected()){
                    if (Utlity.is_online(InspectionActivity.this)){
                        if (Utlity.get_user(InspectionActivity.this).getUserroles_id().equalsIgnoreCase("2")){
                            inspectionsdatasentadmin();
                            sentsModals1=null;
                            valuesall=0;
                            onefromdate=null;
                            twofromdate=null;
                        }
                        else if (Utlity.get_user(InspectionActivity.this).getUserroles_id().equalsIgnoreCase("3")){
                            inspectionsdatasentuser();
                            sentsModals1=null;
                            valuesall=0;
                            onefromdate=null;
                            twofromdate=null;
                        }

                    }
                    else {
                        Utlity.show_toast(InspectionActivity.this,getResources().getString(R.string.nointernet));

                    }

                }
                else if (inspectionBinding.inspectiontab.getTabAt(1).isSelected()){
                    if (Utlity.is_online(InspectionActivity.this)){
                        if (Utlity.get_user(InspectionActivity.this).getUserroles_id().equalsIgnoreCase("2")){
                            inspectionsdatarecivedadmin();
                            sentsModals1=null;
                            valuesall=0;
                            onefromdate=null;
                            twofromdate=null;
                        }
                        else if (Utlity.get_user(InspectionActivity.this).getUserroles_id().equalsIgnoreCase("3")){
                            inspectionsdatareciveduser();
                            sentsModals1=null;
                            valuesall=0;
                            onefromdate=null;
                            twofromdate=null;
                        }

                    }
                    else {
                        Utlity.show_toast(InspectionActivity.this,getResources().getString(R.string.nointernet));
                    }
                }
                inspectionBinding.swipeToRefresh.setRefreshing(false);
            }
        });


    }

    public void click(){
        inspectionBinding.back.setOnClickListener(this::onClick);
        inspectionBinding.addformat.setOnClickListener(this::onClick);
        inspectionBinding.filter.setOnClickListener(this::onClick);
        inspectionBinding.export.setOnClickListener(this::onClick);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.back){
            if (Utlity.get_user(this).getUserroles_id().equalsIgnoreCase("2")){
                onBackPressed();
                /*startActivity(new Intent(this,AdminMainActivity.class));
                finishAffinity();*/
                valuesall=0;
            }
           else if (Utlity.get_user(this).getUserroles_id().equalsIgnoreCase("3")){
                onBackPressed();
               /* startActivity(new Intent(this,UserMainActivity.class));
                finishAffinity();*/
                valuesall=0;
            }

        }
        else if (view.getId()==R.id.addformat){
            Intent intent=new Intent(this,AddInspect.class);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }
        else if (view.getId()==R.id.filter){
            filteraleart();
            valuesall=1;
        }
        else if (view.getId()==R.id.export){
            if (inspectionBinding.inspectiontab.getTabAt(0).isSelected()){
                if (Utlity.is_online(InspectionActivity.this)){
                    if (sentsModals1==null){
                        Utlity.show_toast(InspectionActivity.this,"You Don't Export data before filter any condition.");
                    }
                    else {
                        if (filtercheckvalue==1){
                            if (sentsModals1.size()!=0){
                                ExportDatabaseCSVTask task=new ExportDatabaseCSVTask();
                                task.execute();
                            }
                            else {
                                Utlity.show_toast(InspectionActivity.this,"You Don't Export data before filter any condition.");
                            }
                        }
                        else if (filtercheckvalue==2){
                            Utlity.show_toast(InspectionActivity.this,"You Don't Export data before filter any condition.");
                        }
                        else {
                            Utlity.show_toast(InspectionActivity.this,"You Don't Export data before filter any condition.");
                        }
                    }


                }
                else {
                    Utlity.show_toast(InspectionActivity.this,getResources().getString(R.string.nointernet));
                }
            }

            else if (inspectionBinding.inspectiontab.getTabAt(1).isSelected()){
                if (Utlity.is_online(InspectionActivity.this)){
                    if (sentsModals1==null){
                        Utlity.show_toast(InspectionActivity.this,"You Don't Export data before filter any condition.");
                    }
                    else {
                        if (filtercheckvalue==3){
                            if (sentsModals1.size()!=0){
                                ExportDatabaseCSVTask task=new ExportDatabaseCSVTask();
                                task.execute();
                            }
                            else {
                                Utlity.show_toast(InspectionActivity.this,"You Don't Export data before filter any condition.");
                            }
                        }
                        else if (filtercheckvalue==4){
                            Utlity.show_toast(InspectionActivity.this,"You Don't Export data before filter any condition.");
                        }
                        else {
                            Utlity.show_toast(InspectionActivity.this,"You Don't Export data before filter any condition.");
                        }
                    }

                }
                else {
                    Utlity.show_toast(InspectionActivity.this,getResources().getString(R.string.nointernet));
                }
            }

        }


    }



    private void inspectionsdatasentadmin() {
        respoid1 = pref.getString("respoid",null);
        Utlity.show_progress(InspectionActivity.this);
        apiInterface = ApiClients.getClient1().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Inspectionssend(respoid1,"Bearer "+ Utlity.get_user(InspectionActivity.this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        Utlity.dismiss_dilog(InspectionActivity.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        sentsModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SentsModals>>() {}.getType());
                        if(sentsModals.size()!=0) {
                            norecord.setVisibility(View.GONE);
                            sentlist.setVisibility(View.VISIBLE);
                            sentlist.setLayoutManager(new LinearLayoutManager(InspectionActivity.this, RecyclerView.VERTICAL, false));
                            sentsAdapter = new SentsAdapter(InspectionActivity.this, sentsModals);
                            sentlist.setAdapter(sentsAdapter);
                            inspectionBinding.firstdateshow.setText("");
                            inspectionBinding.seconddateshow.setText("");
                            onefromdate=null;
                           // closefliterfromdate=null;
                            twofromdate=null;
                            //closeflitertodate=null;
                        }
                        else
                        {
                            norecord.setVisibility(View.VISIBLE);
                            sentlist.setVisibility(View.GONE);
                        }


                        try {
                            Objects.requireNonNull(inspectionBinding.inspectiontab.getTabAt(0));

                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog(InspectionActivity.this);

            }
        });

    }
    private void inspectionsdatasentuser() {
        Utlity.show_progress(InspectionActivity.this);
        apiInterface = ApiClients.getClient1().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Inspectionssend(respoiduser,"Bearer "+ Utlity.get_user(InspectionActivity.this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        Utlity.dismiss_dilog(InspectionActivity.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        sentsModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SentsModals>>() {}.getType());
                        if(sentsModals.size()!=0) {
                            norecord.setVisibility(View.GONE);
                            sentlist.setVisibility(View.VISIBLE);
                            sentlist.setLayoutManager(new LinearLayoutManager(InspectionActivity.this, RecyclerView.VERTICAL, false));
                            sentsAdapter = new SentsAdapter(InspectionActivity.this, sentsModals);
                            sentlist.setAdapter(sentsAdapter);
                            inspectionBinding.firstdateshow.setText("");
                            inspectionBinding.seconddateshow.setText("");
                            onefromdate=null;
                           // closefliterfromdate=null;
                            twofromdate=null;
                           // closeflitertodate=null;
                        }
                        else
                        {
                            norecord.setVisibility(View.VISIBLE);
                            sentlist.setVisibility(View.GONE);
                        }


                        try {
                            Objects.requireNonNull(inspectionBinding.inspectiontab.getTabAt(0));

                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog(InspectionActivity.this);

            }
        });

    }

    private void inspectionsdatarecivedadmin() {
        respoid1 = pref.getString("respoid",null);
        Utlity.show_progress(InspectionActivity.this);
        apiInterface = ApiClients.getClient1().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Inspectionsrecive(respoid1,"Bearer "+ Utlity.get_user(InspectionActivity.this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        Utlity.dismiss_dilog(InspectionActivity.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        sentsModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SentsModals>>() {}.getType());
                        if(sentsModals.size()!=0) {
                            norecord1.setVisibility(View.GONE);
                            receivelist.setVisibility(View.VISIBLE);
                            receivelist.setLayoutManager(new LinearLayoutManager(InspectionActivity.this, RecyclerView.VERTICAL, false));
                            sentsAdapter = new SentsAdapter(InspectionActivity.this, sentsModals);
                            receivelist.setAdapter(sentsAdapter);

                            inspectionBinding.firstdateshow.setText("");
                            inspectionBinding.seconddateshow.setText("");
                            onefromdate=null;
                            //closefliterfromdate=null;
                            twofromdate=null;
                           // closeflitertodate=null;

                        }
                        else
                        {
                            norecord1.setVisibility(View.VISIBLE);
                            receivelist.setVisibility(View.GONE);
                        }


                        try {
                            Objects.requireNonNull(inspectionBinding.inspectiontab.getTabAt(0));

                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog(InspectionActivity.this);

            }
        });

    }
    private void inspectionsdatareciveduser() {
        Utlity.show_progress(InspectionActivity.this);
        apiInterface = ApiClients.getClient1().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Inspectionsrecive(respoiduser,"Bearer "+ Utlity.get_user(InspectionActivity.this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        Utlity.dismiss_dilog(InspectionActivity.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        sentsModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SentsModals>>() {}.getType());
                        if(sentsModals.size()!=0) {
                            norecord1.setVisibility(View.GONE);
                            receivelist.setVisibility(View.VISIBLE);
                            receivelist.setLayoutManager(new LinearLayoutManager(InspectionActivity.this, RecyclerView.VERTICAL, false));
                            sentsAdapter = new SentsAdapter(InspectionActivity.this, sentsModals);
                            receivelist.setAdapter(sentsAdapter);

                            inspectionBinding.firstdateshow.setText("");
                            inspectionBinding.seconddateshow.setText("");
                            onefromdate=null;
                           // closefliterfromdate=null;
                            twofromdate=null;
                           // closeflitertodate=null;

                        }
                        else
                        {
                            norecord1.setVisibility(View.VISIBLE);
                            receivelist.setVisibility(View.GONE);
                        }


                        try {
                            Objects.requireNonNull(inspectionBinding.inspectiontab.getTabAt(0));

                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog(InspectionActivity.this);

            }
        });

    }

    public void filteraleart(){
        final Dialog dialog = new Dialog(InspectionActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.filterdata);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
        location=dialog.findViewById(R.id.locations);
        status=dialog.findViewById(R.id.status);
        department=dialog.findViewById(R.id.responsbility);
        concern=dialog.findViewById(R.id.concernarea);
        filtesave=dialog.findViewById(R.id.btnFilter);
        cancle=dialog.findViewById(R.id.filtercancle);

        todtebtn=dialog.findViewById(R.id.todtebtn);
        fromdtebtn=dialog.findViewById(R.id.fromdtebtn);

        firstdate=dialog.findViewById(R.id.firstdateshow);
        seconddate=dialog.findViewById(R.id.seconddateshow);

        fromdtebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_date_picker(InspectionActivity.this,firstdate);
            }
        });

        todtebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_date_picker1(InspectionActivity.this,seconddate);
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                valuesall=0;
            }
        });

        filtesave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inspectionBinding.inspectiontab.getTabAt(0).isSelected()){
                    if (Utlity.is_online(InspectionActivity.this)){
                        if (onefromdate!=null || twofromdate!=null ||  locatioid!=null || responseid!=null || converid!=null || spinnervalue!=null){
                            inspectionsearcsents(onefromdate,twofromdate,locatioid,responseid,converid,spinnervalue);
                            filtercheckvalue=1;
                        }
                        else
                        {
                            inspectionsearcsents(onefromdate,twofromdate,locatioid,responseid,converid,spinnervalue);
                            filtercheckvalue=2;
                        }
                    }
                    else {
                        Utlity.show_toast(InspectionActivity.this,getResources().getString(R.string.nointernet));
                    }
                    dialog.dismiss();
                }
                else if (inspectionBinding.inspectiontab.getTabAt(1).isSelected()){
                    if (Utlity.is_online(InspectionActivity.this)){
                        if (onefromdate!=null || twofromdate!=null ||  locatioid!=null || responseid!=null || converid!=null || spinnervalue!=null) {
                            inspectionsearchrecived(onefromdate,twofromdate,locatioid,responseid,converid,spinnervalue);
                            filtercheckvalue=3;
                        }else {
                            inspectionsearchrecived(onefromdate,twofromdate,locatioid,responseid,converid,spinnervalue);
                            filtercheckvalue=4;
                        }
                    }
                    else {
                        Utlity.show_toast(InspectionActivity.this,getResources().getString(R.string.nointernet));
                    }
                    dialog.dismiss();
                }

            }
        });

        department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0){
                    responseid=respoModals.get(position-1).getId();
                }
                else {
                    responseid=null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0){
                    locatioid=locationModals.get(position-1).getId();
                }
                else {
                    locatioid=null;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        concern.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0){
                    converid=concernModals.get(position-1).getId();
                }else {converid=null;}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        List<String> categories = new ArrayList<String>();
        categories.add("All Status");
        categories.add("Open");
        categories.add("Close");
        categories.add("Verify");
        categories.add("In Process");
        categories.add("Created Date");
        ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinneritem, categories);
        aa.setDropDownViewResource(R.layout.spinneritem);
        status.setAdapter(aa);
        status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

               // if (position!=0){
                    valuepos=categories.get(position);
                    //Utlity.show_toast(InspectionActivity.this,valuepos);

                    if (valuepos.equals("Open")){
                        spinnervalue="1";
                       // Utlity.show_toast(InspectionActivity.this, spinnervalue);
                    }
                    else if(valuepos.equals("Close")){
                        spinnervalue="2";
                       // Utlity.show_toast(InspectionActivity.this, spinnervalue);
                    }

                    else if(valuepos.equals("Verify")) {
                        spinnervalue="3";
                       // Utlity.show_toast(InspectionActivity.this, spinnervalue);

                    }
                    else if(valuepos.equals("In Process")) {
                        spinnervalue="4";
                        // Utlity.show_toast(InspectionActivity.this, spinnervalue);

                    }
                    else if(valuepos.equals("Created Date")) {
                        spinnervalue="5";
                        // Utlity.show_toast(InspectionActivity.this, spinnervalue);
                    }
                    else if (valuepos.equals("All Status")){
                        spinnervalue=null;
                    }
                }
          //  }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        dialog.show();

        if (Utlity.is_online(this)){
            respolist();
            concernlist();
            locationlist();
        }
        else {
            Utlity.show_toast(InspectionActivity.this, getResources().getString(R.string.nointernet));
        }
    }

    private void inspectionsearcsents(String firstdate,String secondate,String locatioid1,String respoid, String converid1, String status) {

        Utlity.show_progress(InspectionActivity.this);
        respoid1 = pref.getString("respoid",null);
        apiInterface = ApiClients.getClient1().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Inspectionsearchdatasen(respoid1,"Bearer "+ Utlity.get_user(InspectionActivity.this).getToken(),firstdate,secondate,locatioid1,respoid,converid1,status);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        Utlity.dismiss_dilog(InspectionActivity.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);

                        JSONObject object = new JSONObject(apidata);
                        sentsModals1 = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SentsModals>>() {}.getType());
                        if(sentsModals1.size()!=0) {
                            norecord.setVisibility(View.GONE);
                            sentlist.setLayoutManager(new LinearLayoutManager(InspectionActivity.this, RecyclerView.VERTICAL, false));
                            sentsAdapter = new SentsAdapter(InspectionActivity.this, sentsModals1);
                            sentlist.setAdapter(sentsAdapter);
                       /*     locatioid="";
                            responseid="";
                            converid="";
                            valuepos=null;
                            onefromdate=null;
                           // closefliterfromdate=null;
                            twofromdate=null;
                           // closeflitertodate=null;
                            spinnervalue="";*/
                            /*inspectionBinding.firstdateshow.setText("");
                            inspectionBinding.seconddateshow.setText("");
                             */
                            /*onefromdate=null;
                            twofromdate=null;*/

                        }
                        else if (sentsModals1.size()==0){
                            norecord.setVisibility(View.VISIBLE);
                            sentlist.setLayoutManager(new LinearLayoutManager(InspectionActivity.this, RecyclerView.VERTICAL, false));
                            sentsAdapter = new SentsAdapter(InspectionActivity.this, sentsModals1);
                            sentlist.setAdapter(sentsAdapter);
                            /*locatioid="";
                            responseid="";
                            converid="";
                            valuepos=null;
                            onefromdate=null;
                           // closefliterfromdate=null;
                            twofromdate=null;
                            //closeflitertodate=null;
                            spinnervalue="";*/
                           /* inspectionBinding.firstdateshow.setText("");
                            inspectionBinding.seconddateshow.setText("");

                            */
                           /* onefromdate=null;
                            twofromdate=null;*/
                        }
                        else
                        {
                            norecord.setVisibility(View.VISIBLE);
                        }


                        try {
                            Objects.requireNonNull(inspectionBinding.inspectiontab.getTabAt(0));

                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog(InspectionActivity.this);

            }
        });
    }
    private void inspectionsearchrecived(String firstdate,String secondate,String locatioid1,String responseid1, String converid1,String status) {
        Utlity.show_progress(InspectionActivity.this);
        respoid1 = pref.getString("respoid",null);
        apiInterface = ApiClients.getClient1().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Inspectionsearchdatarecive(respoid1,"Bearer "+ Utlity.get_user(InspectionActivity.this).getToken(),firstdate,secondate,locatioid1,responseid1,converid1,status);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        Utlity.dismiss_dilog(InspectionActivity.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        sentsModals1 = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SentsModals>>() {}.getType());
                        if(sentsModals1.size()!=0) {
                            norecord1.setVisibility(View.GONE);
                            receivelist.setLayoutManager(new LinearLayoutManager(InspectionActivity.this, RecyclerView.VERTICAL, false));
                            sentsAdapter = new SentsAdapter(InspectionActivity.this, sentsModals1);
                            receivelist.setAdapter(sentsAdapter);
                            locatioid="";
                            responseid="";
                            converid="";
                            valuepos=null;
                            onefromdate=null;
                           // closefliterfromdate=null;
                            twofromdate=null;
                            //closeflitertodate=null;
                            spinnervalue="";
                            /*inspectionBinding.firstdateshow.setText("");
                            inspectionBinding.seconddateshow.setText("");
                             */
                            /*onefromdate=null;
                            twofromdate=null;
*/
                        }
                        else if (sentsModals1.size()==0){
                            norecord1.setVisibility(View.VISIBLE);
                            receivelist.setLayoutManager(new LinearLayoutManager(InspectionActivity.this, RecyclerView.VERTICAL, false));
                            sentsAdapter = new SentsAdapter(InspectionActivity.this, sentsModals1);
                            receivelist.setAdapter(sentsAdapter);
                            locatioid="";
                            responseid="";
                            converid="";
                            valuepos=null;
                            onefromdate=null;
                           // closefliterfromdate=null;
                            twofromdate=null;
                          //  closeflitertodate=null;
                            spinnervalue="";
                            /*inspectionBinding.firstdateshow.setText("");
                            inspectionBinding.seconddateshow.setText("");
                            */

                         /*   onefromdate=null;
                            twofromdate=null;*/
                        }
                        else
                        {
                            norecord1.setVisibility(View.VISIBLE);
                        }


                        try {
                            Objects.requireNonNull(inspectionBinding.inspectiontab.getTabAt(1));

                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog(InspectionActivity.this);

            }
        });

    }

    private void respolist() {
        Utlity.show_progress(this);
        respoid1 = pref.getString("respoid",null);
        apiInterface = ApiClients.getClient1().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Depatlist(respoid1,"Bearer "+ Utlity.get_user(this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        Utlity.dismiss_dilog(InspectionActivity.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        respoModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<RespoModal>>() {}.getType());
                        respolist=new ArrayList<>();
                        respolist.add("Select Responsibility");

                        for (RespoModal respoModal:respoModals){
                            respolist.add(respoModal.getDept_name());
                        }

                        ArrayAdapter<String> adapter1 =
                                new ArrayAdapter<String>(InspectionActivity.this,  R.layout.spinneritem, respolist);
                        adapter1.setDropDownViewResource( R.layout.spinneritem);
                        department.setAdapter(adapter1);


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog(InspectionActivity.this);
               // Utlity.show_toast(InspectionActivity.this, "Not found");

            }
        });

    }
    private void locationlist() {
        respoid1 = pref.getString("respoid",null);
        apiInterface = ApiClients.getClient1().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.LocationList(respoid1,"Bearer "+ Utlity.get_user(this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        locationModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<LocationModal>>() {}.getType());
                        locationlist=new ArrayList<>();
                        locationlist.add("Select Locations");
                        for (LocationModal locationModal:locationModals){
                            locationlist.add(locationModal.getLoc_name());
                        }

                        ArrayAdapter<String> adapter1 =
                                new ArrayAdapter<String>(InspectionActivity.this,  R.layout.spinneritem, locationlist);
                        adapter1.setDropDownViewResource( R.layout.spinneritem);
                        location.setAdapter(adapter1);


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
              //  Utlity.show_toast(InspectionActivity.this, "Not found");

            }
        });

    }

    private void concernlist() {
        apiInterface = ApiClients.getClient1().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.ConcernList("Bearer "+ Utlity.get_user(this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        concernModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<ConcernModal>>() {}.getType());
                        concernlist=new ArrayList<>();
                        concernlist.add("Select Concern area");
                        for (ConcernModal concernModal:concernModals){
                            concernlist.add(concernModal.getArea_name());
                        }


                        ArrayAdapter<String> adapter1 =
                                new ArrayAdapter<String>(InspectionActivity.this,  R.layout.spinneritem, concernlist);
                        adapter1.setDropDownViewResource( R.layout.spinneritem);
                        concern.setAdapter(adapter1);


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
               // Utlity.show_toast(InspectionActivity.this, "Not found");

            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        valuesall=0;
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
                onefromdate= String.valueOf(tv.getText());


            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        calendar.add(Calendar.YEAR, 0);
        dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());// TODO: used to hide future date,month and year
        //dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());// TODO: used to hide past date,month and year
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
                twofromdate= String.valueOf(tv.getText());

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        calendar.add(Calendar.YEAR, 0);
        dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());// TODO: used to hide future date,month and year
        //dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());// TODO: used to hide past date,month and year
        dialog.show();
    }


    public void ExcelWriter() {

        String[] columns = {"Observation Date", "Location", "Responsibility", "Concern Area", "Created By","Before Comment","Before Image","After Comment","After Image","Close Date","Status"};

        workbook = new XSSFWorkbook();
        CreationHelper createHelper = workbook.getCreationHelper();
        // Create a Sheet
        sheet = workbook.createSheet("Inspection");
        sheet.setAutobreaks(false);
        sheet.setColumnBreak(153);
        sheet.removeColumnBreak(78);
        sheet.setColumnWidth(0, 3500);
        sheet.setColumnWidth(1, 3500);
        sheet.setColumnWidth(2, 3500);
        sheet.setColumnWidth(3, 3500);
        sheet.setColumnWidth(4, 3500);
        sheet.setColumnWidth(5, 3500);
        sheet.setColumnWidth(6, 3500);
        sheet.setColumnWidth(7, 3500);
        sheet.setColumnWidth(8, 3500);
        sheet.setColumnWidth(9, 3500);
        sheet.setColumnWidth(10, 3500);

        PrintSetup printSetup = sheet.getPrintSetup();
        printSetup.setLandscape(true);
        printSetup.setFitWidth((short)2);
        printSetup.setFitHeight((short)2);
        printSetup.setPaperSize(PrintSetup.A4_PAPERSIZE);

        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        //headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 10);
        headerFont.setColor(IndexedColors.BLACK.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setWrapText(true);


        // Create a Row
        Row headerRow = sheet.createRow(0);
        headerRow.setHeight((short) 500);

        // Create cells
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);

        }



        // Create Other rows and cells with employees data
        int rowNum = 1;

        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));
        dateCellStyle.setAlignment(ALIGN_JUSTIFY);
        dateCellStyle.setWrapText(true);

        for (SentsModals sentsModals : sentsModals1) {
            Row row = sheet.createRow(rowNum++);
            row.setHeight((short) 1500);

            CellStyle roeCellStyle = workbook.createCellStyle();
            roeCellStyle.setWrapText(true);
            row.setRowStyle(roeCellStyle);




            Cell dateCreatedCell = row.createCell(0);
            dateCreatedCell.setCellValue(sentsModals.getCreated_at());
            dateCreatedCell.setCellStyle(dateCellStyle);

            CellStyle roLoc_name = workbook.createCellStyle();
            roLoc_name.setWrapText(true);
            Cell cellroLoc_name = row.createCell(1);
            cellroLoc_name.setCellStyle(roLoc_name);
            cellroLoc_name.setCellValue(sentsModals.getLoc_name());

            CellStyle roDept_name = workbook.createCellStyle();
            roDept_name.setWrapText(true);
            Cell cellroDept_name = row.createCell(2);
            cellroDept_name.setCellStyle(roDept_name);
            cellroDept_name.setCellValue(sentsModals.getDept_name());

            CellStyle roArea_name = workbook.createCellStyle();
            roArea_name.setWrapText(true);
            Cell cellroArea_name = row.createCell(3);
            cellroArea_name.setCellStyle(roArea_name);
            cellroArea_name.setCellValue(sentsModals.getArea_name());

            row.createCell(4).setCellValue(sentsModals.getCreated_by_name());

            CellStyle roecomment = workbook.createCellStyle();
            roecomment.setWrapText(true);
            Cell cell1 = row.createCell(5);
            cell1.setCellStyle(roecomment);
            cell1.setCellValue(sentsModals.getBefore_comment());

            if(sentsModals.getBefore_img()!=null) {
                InputStream befor_image = null;

                try {
                    befor_image = new URL(Apis.imageBase_Url + sentsModals.getBefore_img()).openStream();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] bytes = new byte[0];
                try {
                    bytes =readAllBytes(befor_image);
                    compress(bytes);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                int my_picture_id = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);

                try {
                    befor_image.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();


                XSSFClientAnchor my_anchor = new XSSFClientAnchor();

                my_anchor.setDx1(5 * XSSFShape.EMU_PER_PIXEL);
                my_anchor.setDy1(5 * XSSFShape.EMU_PER_PIXEL);

                my_anchor.setCol1(6); //Column B
                my_anchor.setRow1(rowNum - 1); //Row 3
                my_anchor.setCol2(7); //Column C
                my_anchor.setRow2(rowNum); //Row 4

                XSSFPicture my_picture = drawing.createPicture(my_anchor, my_picture_id);

                my_picture.setLineStyle(0);
                // rgb color code for black line
                my_picture.setLineStyleColor(1, 1, 1);
                // double number for line width
                my_picture.setLineWidth(1.5);


            }

            CellStyle aftercomment = workbook.createCellStyle();
            aftercomment.setWrapText(true);
            Cell aftercell1 = row.createCell(7);
            aftercell1.setCellStyle(aftercomment);
            aftercell1.setCellValue(sentsModals.getAfter_comment());



            if(sentsModals.getAfter_img()!=null) {
                InputStream afterimg = null;
                try {
                    afterimg = new URL(Apis.imageBase_Url + sentsModals.getAfter_img()).openStream();
                    //;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] afterbytes = new byte[0];
                try {
                    afterbytes = readAllBytes(afterimg);
                    compress(afterbytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int aftermy_picture_id = workbook.addPicture(afterbytes, Workbook.PICTURE_TYPE_JPEG);

                try {
                    afterimg.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                XSSFDrawing afterdrawing = (XSSFDrawing) sheet.createDrawingPatriarch();


                XSSFClientAnchor aftermy_anchor = new XSSFClientAnchor();

                aftermy_anchor.setDx1(5 * XSSFShape.EMU_PER_PIXEL);
                aftermy_anchor.setDy1(5 * XSSFShape.EMU_PER_PIXEL);



                aftermy_anchor.setCol1(8); //Column B
                aftermy_anchor.setRow1(rowNum - 1); //Row 3
                aftermy_anchor.setCol2(9); //Column C
                aftermy_anchor.setRow2(rowNum); //Row 4

                XSSFPicture aftermy_picture = afterdrawing.createPicture(aftermy_anchor, aftermy_picture_id);

                aftermy_picture.setLineStyle(0);
                // rgb color code for black line
                aftermy_picture.setLineStyleColor(1, 1, 1);
                // double number for line width
                aftermy_picture.setLineWidth(1.5);
            }

            Cell datecloserCell = row.createCell(9);
            datecloserCell.setCellValue(sentsModals.getClosed_at());
            datecloserCell.setCellStyle(dateCellStyle);

            row.createCell(10).setCellValue(sentsModals.getStatus());

        }

        File myDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/SafeFood/");
        myDir.mkdir();
       /* if (!myDir.exists()) {
            myDir.mkdirs();
        }*/
        String new_file= ((workbook instanceof HSSFWorkbook)?"inspection.xls":"inspection.xlsx");
        File files = new File (myDir, new_file);
        path= String.valueOf(files);
        try {
            fileOut = new FileOutputStream(files);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {

            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

}


    public static byte[] compress(byte[] bytes) throws IOException
    {
        InputStream is = new ByteArrayInputStream(bytes);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try (OutputStream os = new GZIPOutputStream(bout))
        {
            IOUtils.copy(is, os);
        }

        return bout.toByteArray();
    }

    public static byte[] readAllBytes(InputStream inputStream) throws IOException {
        final int bufLen = 4 * 0x400; // 4KB
        byte[] buf = new byte[bufLen];
        int readLen;
        IOException exception = null;

        try {
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                while ((readLen = inputStream.read(buf, 0, bufLen)) != -1)
                    outputStream.write(buf, 0, readLen);

                return outputStream.toByteArray();
            }
        } catch (IOException e) {
            exception = e;
            throw e;
        } finally {
            if (exception == null) inputStream.close();
            else try {
                inputStream.close();
            } catch (IOException e) {
                exception.addSuppressed(e);
            }
        }
    }

    public class ExportDatabaseCSVTask extends AsyncTask<String ,String, String> {

        private final ProgressDialog dialog = new ProgressDialog(InspectionActivity.this);
        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Exporting...");
            this.dialog.show();
            this.dialog.setCanceledOnTouchOutside(true);
        }

        @Override
        protected String doInBackground(String... strings) {

            ExcelWriter();

            return "";
        }

        @SuppressLint("NewApi")
        @Override
        protected void onPostExecute(final String success) {

            if (this.dialog.isShowing()){
                this.dialog.dismiss();
            }
            if (success.isEmpty()){
                Toast.makeText(InspectionActivity.this, "Export successful!", Toast.LENGTH_SHORT).show();
               try {
                   share(path);
               }catch (final IllegalArgumentException e){
                   e.printStackTrace();
               }


            }
            else {
                Toast.makeText(InspectionActivity.this, "Export failed!", Toast.LENGTH_SHORT).show();
            }

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void share(String sharePath){

        Uri uri =  Uri.parse(sharePath);
        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(intent);


    }

}


