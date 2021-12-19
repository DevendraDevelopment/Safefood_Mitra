package com.safefoodmitra.safefoodmitra.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;
import com.safefoodmitra.safefoodmitra.Apis.Apis;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.SentsModals;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.ActivityInspectionDetailsBinding;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.safefoodmitra.safefoodmitra.Activities.InspectionActivity.valuesall;

public class InspectionDetails extends AppCompatActivity implements View.OnClickListener {
    ActivityInspectionDetailsBinding inspectionDetailsBinding;
    SentsModals sentsModals;
    SlidrInterface slidrInterface;
    String valuepos,creadtid,userid,closedby,id="",commentvalue="";
    int spinnervalue=0;
    File CapGalCompImg=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inspectionDetailsBinding= DataBindingUtil.setContentView(this,R.layout.activity_inspection_details);
        sentsModals=new SentsModals();
        slidrInterface= Slidr.attach(this);
        slidrInterface.unlock();
        
        if (getIntent()!=null){
            sentsModals= Utlity.gson.fromJson(this.getIntent().getStringExtra("details"), SentsModals.class);
            inspectionDetailsBinding.dtevalue.setText(sentsModals.getCreated_at());
            inspectionDetailsBinding.status.setText(sentsModals.getStatus());
            inspectionDetailsBinding.department.setText(sentsModals.getDept_name());
            inspectionDetailsBinding.locations.setText(sentsModals.getLoc_name());
            inspectionDetailsBinding.sendrnme.setText(sentsModals.getCreated_by_name());
            inspectionDetailsBinding.consarea.setText(sentsModals.getArea_name());
            inspectionDetailsBinding.comment.setText(sentsModals.getBefore_comment());
            inspectionDetailsBinding.aftercomment.setText(sentsModals.getAfter_comment());
            inspectionDetailsBinding.closerdte.setText(sentsModals.getClosed_at());
            inspectionDetailsBinding.closrnme.setText(sentsModals.getClosed_by_name());
            inspectionDetailsBinding.verifydata.setText(sentsModals.getVerified_at());
            inspectionDetailsBinding.verifynme.setText(sentsModals.getVerified_by_name());
            inspectionDetailsBinding.cost.setText(sentsModals.getCost());
            Utlity.Set_image1(sentsModals.getBefore_img(),inspectionDetailsBinding.beforimg);
            Utlity.Set_image1(sentsModals.getAfter_img(),inspectionDetailsBinding.afterimg);
            click();
            creadtid=sentsModals.getCreated_by();
            closedby=sentsModals.getClosed_by();
            userid=Utlity.get_user(this).getId();
            id=sentsModals.getId();
           // Utlity.show_toast(InspectionDetails.this, id +"&&"+ spinnervalue);


            if (sentsModals.getIns_status().equals("1")){
                inspectionDetailsBinding.closeinspection.setVisibility(View.VISIBLE);
               /* if (creadtid.equals(userid)){
                    inspectionDetailsBinding.closelayout.setVisibility(View.VISIBLE);
                }
                else {
                    inspectionDetailsBinding.closelayout.setVisibility(View.GONE);

                }*/

            }
            else if (sentsModals.getIns_status().equals("2")){
                inspectionDetailsBinding.closeinspection.setVisibility(View.VISIBLE);
                if (creadtid.equals(userid)){
                    inspectionDetailsBinding.closelayout.setVisibility(View.VISIBLE);
                }
                else {
                    inspectionDetailsBinding.closelayout.setVisibility(View.GONE);
                    inspectionDetailsBinding.closeinspection.setVisibility(View.GONE);
                }
            }
            else if (sentsModals.getIns_status().equals("3")){
                inspectionDetailsBinding.closeinspection.setVisibility(View.GONE);
                inspectionDetailsBinding.closelayout.setVisibility(View.GONE);
            }else if (sentsModals.getIns_status().equals("4")){
                inspectionDetailsBinding.closeinspection.setVisibility(View.VISIBLE);
            }

            else {
                inspectionDetailsBinding.closeinspection.setVisibility(View.GONE);
                inspectionDetailsBinding.closelayout.setVisibility(View.GONE);
            }
        }


        List<String> categories = new ArrayList<String>();
        categories.add("Closed");
        categories.add("ReOpen");
        categories.add("Verify");
        ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinneritem1, categories);
        aa.setDropDownViewResource(R.layout.spinneritem1);
        inspectionDetailsBinding.closelist.setAdapter(aa);
        inspectionDetailsBinding.closelist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                valuepos=inspectionDetailsBinding.closelist.getSelectedItem().toString();
                Utlity.show_toast(InspectionDetails.this,valuepos);

                if (valuepos.equals("ReOpen")){
                    spinnervalue=1;
                    inspectionDetailsBinding.closeinspection.setText(getString(R.string.update));
                    Utlity.show_toast(InspectionDetails.this, String.valueOf(spinnervalue));
                }
                else if(valuepos.equals("Verify")){
                    spinnervalue=3;
                    inspectionDetailsBinding.closeinspection.setText(getString(R.string.update));
                    Utlity.show_toast(InspectionDetails.this, String.valueOf(spinnervalue));
                }
                else {
                    spinnervalue=2;
                    inspectionDetailsBinding.closeinspection.setText(getString(R.string.close));
                    Utlity.show_toast(InspectionDetails.this, String.valueOf(spinnervalue));

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void click(){
        inspectionDetailsBinding.back.setOnClickListener(this);
        inspectionDetailsBinding.closeinspection.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.back){
            if (Utlity.get_user(this).getUserroles_id().equalsIgnoreCase("2")){
                slidrInterface.lock();
                startActivity(new Intent(this,AdminMainActivity.class));
                finishAffinity();
            }
            else if (Utlity.get_user(this).getUserroles_id().equalsIgnoreCase("3")){
                slidrInterface.lock();
                startActivity(new Intent(this,UserMainActivity.class));
                finishAffinity();
            }
        }

        else if (view.getId()==R.id.closeinspection){
            if (sentsModals.getIns_status().equals("1")){
                startActivity(new Intent(this,CloseInspection.class).putExtra("details",Utlity.gson.toJson(sentsModals)));
            }
            else if (sentsModals.getIns_status().equals("2")){
                if (inspectionDetailsBinding.closelist.getSelectedItemPosition()==0){
                    Utlity.show_toast(InspectionDetails.this, "Please Select Status");
                }
                else {
                    closeall();
                    //startActivity(new Intent(this,CloseInspection.class).putExtra("details",Utlity.gson.toJson(sentsModals)).putExtra("spinnervalue",String.valueOf(spinnervalue)));
                }
            }else if (sentsModals.getIns_status().equals("4")){
                startActivity(new Intent(this,CloseInspection.class).putExtra("details",Utlity.gson.toJson(sentsModals)));
            }

        }
    }

    private void closeall() {
        Utlity.show_progress(this);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        if (CapGalCompImg!=null){
            builder.addFormDataPart("after_img", CapGalCompImg.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), CapGalCompImg));
        }
        else {
            builder.addFormDataPart("after_img","", RequestBody.create(MediaType.parse("multipart/form-data"),""));
        }

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Gson gson = new Gson();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("id",id);
        if (commentvalue!=null){
            builder.addFormDataPart("after_comment",commentvalue);
        }
        else {
            builder.addFormDataPart("after_comment","");
        }

        if (spinnervalue!=0){
            builder.addFormDataPart("ins_status", String.valueOf(spinnervalue));
        }
        else {
            builder.addFormDataPart("ins_status","");
        }


        MultipartBody body = builder.build();

        final Request request = new Request.Builder()
                .url(Apis.closeinspection)
                .header("Authorization", "Bearer "+Utlity.get_user(this).getToken())
                .post(body)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.retryOnConnectionFailure();
        okHttpClient.connectTimeoutMillis();
        okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Utlity.dismiss_dilog(InspectionDetails.this);
            }

            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Utlity.dismiss_dilog(InspectionDetails.this);

                           /* StrictMode.ThreadPolicy policy =
                                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);

                            */
                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            if (response.isSuccessful()){
                                Utlity.show_toast(InspectionDetails.this,apidata+" Success");
                                if (valuesall==0){
                                    startActivity(new Intent(InspectionDetails.this,InspectionActivity.class));
                                    finish();
                                }
                                else if (valuesall==1){
                                    startActivity(new Intent(InspectionDetails.this,InspectionActivity.class));
                                    finish();
                                }

                            }
                            else {
                                Utlity.show_toast(InspectionDetails.this,"failed");
                            }
                        } catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

    }

}