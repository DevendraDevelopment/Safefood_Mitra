package com.safefoodmitra.safefoodmitra.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.safefoodmitra.safefoodmitra.Adapter.CleaningMaintenanceAdapter;
import com.safefoodmitra.safefoodmitra.Apis.ApiClients;
import com.safefoodmitra.safefoodmitra.Apis.Apis;
import com.safefoodmitra.safefoodmitra.Apis.RetApis;
import com.safefoodmitra.safefoodmitra.CameraAll.EditImageActivity;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.CMFDetailsModel;
import com.safefoodmitra.safefoodmitra.Modals.CleaningMaintModel;
import com.safefoodmitra.safefoodmitra.Modals.DocumentsModel;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.ActivityCMFDetailsBinding;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import static com.safefoodmitra.safefoodmitra.Activities.InspectionActivity.valuesall;

public class CMFDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityCMFDetailsBinding cmfDetailsBinding;
    String taskid, removetaskid, tsktype, taskcomment,verifiedcomment;
    RetApis apiInterface;
    public CMFDetailsModel cmfDetailsModels;
    SharedPreferences pref;
    CleaningMaintModel.Datalist cleaningMaintModel;
    ArrayList<String> selecttypecfm;
    static final int RESULT_LOAD_IMAGE1 = 6;
    static final int RESULT_LOAD_IMAGE2 = 7;
    static final int RESULT_LOAD_IMAGE3 = 8;
    static final int RESULT_LOAD_IMAGE4 = 9;
    static final int RESULT_LOAD_IMAGE5 = 10;
    static final int REQUEST_IMAGE_CAPTURE1 = 1;
    static final int REQUEST_IMAGE_CAPTURE2 = 2;
    static final int REQUEST_IMAGE_CAPTURE3 = 3;
    static final int REQUEST_IMAGE_CAPTURE4 = 4;
    static final int REQUEST_IMAGE_CAPTURE5 = 5;
    File FirstImage,FirstFirstImage,SecondImage,SecondSecondImage,ThirdImage,ThirdThirdImage,ForthImage,ForthForthImage,FifthImage,FifthFifthImage= null,SignaImage,SignaSignaImage;
    private String mCurrentPhotoPath, spinnervalue = "";
    Bitmap firstimage, secondimage,thirdimage,forthimage,fifthimage,signatureimage;
    ArrayList<Uri> mArrayUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cmfDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_c_m_f_details);
        mArrayUri = new ArrayList<>();
        pref = this.getSharedPreferences("MyG9", MODE_PRIVATE);
        if (getIntent() != null) {
            cleaningMaintModel = Utlity.gson.fromJson(getIntent().getStringExtra("detail"), CleaningMaintModel.Datalist.class);
            taskid = cleaningMaintModel.getTaskid();
            removetaskid = cleaningMaintModel.getCmscheduleid();

        }

        if (Utlity.is_online(this)) {
            getcmfdata();
        } else {
            Utlity.show_toast(CMFDetailsActivity.this, getResources().getString(R.string.nointernet));
        }
        click();
        if (Utlity.get_user(this).getUserroles_id().equalsIgnoreCase("2")) {
            cmfDetailsBinding.linearverification.setVisibility(View.VISIBLE);
            if (cleaningMaintModel.getTaskstatus().equals("2") || cleaningMaintModel.getTaskstatus().equals("1")) {
                cmfDetailsBinding.removetask.setVisibility(View.GONE);
            }else {
                  cmfDetailsBinding.removetask.setVisibility(View.VISIBLE);
            }
           if (cleaningMaintModel.getIsverified().equals("1")){
               cmfDetailsBinding.linearverification.setVisibility(View.VISIBLE);
           }else {
               cmfDetailsBinding.linearverification.setVisibility(View.GONE);
           }


        } else if (Utlity.get_user(this).getUserroles_id().equalsIgnoreCase("3")) {
            cmfDetailsBinding.removetask.setVisibility(View.GONE);
            cmfDetailsBinding.linearverification.setVisibility(View.GONE);
            if (cleaningMaintModel.getIseditable().equals("1")) {
                cmfDetailsBinding.linearuser.setVisibility(View.VISIBLE);
            } else {
                cmfDetailsBinding.linearuser.setVisibility(View.GONE);
            }
        }
        selecttypecfm = new ArrayList<>();
        selecttypecfm.add("Completed");
        selecttypecfm.add("Cancelled");

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, R.layout.spinneritem, selecttypecfm);
        adapter.setDropDownViewResource(R.layout.spinneritem);
        cmfDetailsBinding.task.setAdapter(adapter);

        cmfDetailsBinding.task.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tsktype = cmfDetailsBinding.task.getItemAtPosition(position).toString();
                Utlity.show_toast(CMFDetailsActivity.this, tsktype);
                if (id==1){
                  cmfDetailsBinding.linearimagecap.setVisibility(View.GONE);
                }else {
                    cmfDetailsBinding.linearimagecap.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

    }

    private void getcmfdata() {
        Utlity.show_progress(this);
        apiInterface = ApiClients.getClient1().create(RetApis.class);
        retrofit2.Call<ResponseBody> call = apiInterface.CFMListDetails(taskid, "Bearer " + Utlity.get_user(this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        Utlity.dismiss_dilog(CMFDetailsActivity.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>", apidata);
                        JSONObject object = new JSONObject(apidata);
                        cmfDetailsModels = Utlity.gson.fromJson(object.getJSONObject("success").toString(), new TypeToken<CMFDetailsModel>() {
                        }.getType());
                        cmfDetailsBinding.tvcmftypes.setText(cmfDetailsModels.getCmtype());
                        cmfDetailsBinding.tvzone.setText(cmfDetailsModels.getZone());
                        cmfDetailsBinding.tvtaskdate.setText(cmfDetailsModels.getAssignedday()+ " "+cmfDetailsModels.getAssignedtime());
                        cmfDetailsBinding.tvlocation.setText(cmfDetailsModels.getLoc_name());
                        cmfDetailsBinding.tvsublocation.setText(cmfDetailsModels.getSubloc_name());
                        cmfDetailsBinding.tvequipement.setText(cmfDetailsModels.getEquip_name());
                        cmfDetailsBinding.tvdeparment.setText(cmfDetailsModels.getDept_name());
                        cmfDetailsBinding.tvtaskattend.setText(cmfDetailsModels.getVerifier_first_name());
                        cmfDetailsBinding.tvverifiedname.setText(cmfDetailsModels.getVerifier_first_name());
                        cmfDetailsBinding.tvverifieddate.setText(cmfDetailsModels.getVerified_at());
                        cmfDetailsBinding.tvverifiedcomment.setText(cmfDetailsModels.getVerify_comment());

                        cmfDetailsBinding.tvattend.setText(cmfDetailsModels.getAttended_first_name()+"  "+cmfDetailsModels.getAttended_last_name());
                        cmfDetailsBinding.tvattenddate.setText(cmfDetailsModels.getAttended_at());

                        cmfDetailsBinding.tvstartfrom.setText(cmfDetailsModels.getStartedfrom());
                        cmfDetailsBinding.tvtaskreptation.setText(cmfDetailsModels.getRepetitiondays());
                        cmfDetailsBinding.tvstatus.setText(cmfDetailsModels.getTaskstatusdisp());
                        Utlity.Set_imageGlideCmfDetils(CMFDetailsActivity.this,cmfDetailsModels.getVerify_image(),cmfDetailsBinding.tvsignimage);
                        if (!cmfDetailsModels.getTaskcomment().equals("")){
                          cmfDetailsBinding.linearplace.setVisibility(View.VISIBLE);
                        cmfDetailsBinding.tvcommentbox.setText(cmfDetailsModels.getTaskcomment());

                        }else {
                            cmfDetailsBinding.linearplace.setVisibility(View.GONE);
                        }

                        cmfDetailsBinding.tvcommentboxsecond.setText(cmfDetailsModels.getCmnote());
                        if (cmfDetailsModels.getFirstimage()!=null){
                            cmfDetailsBinding.firstimage.setVisibility(View.VISIBLE);

                           Utlity.Set_imageGlideCmfDetils(CMFDetailsActivity.this,cmfDetailsModels.getFirstimage(),cmfDetailsBinding.firstimage);

                        }else {
                            cmfDetailsBinding.firstimage.setVisibility(View.GONE);
                        }
                        if (cmfDetailsModels.getSecondimage()!=null){
                            cmfDetailsBinding.secondimage.setVisibility(View.VISIBLE);
                            Utlity.Set_imageGlideCmfDetils(CMFDetailsActivity.this,cmfDetailsModels.getSecondimage(),cmfDetailsBinding.secondimage);

                        }else {
                            cmfDetailsBinding.secondimage.setVisibility(View.GONE);
                        }
                        if (cmfDetailsModels.getThirdimage()!=null){
                            cmfDetailsBinding.thiredimage.setVisibility(View.VISIBLE);
                            Utlity.Set_imageGlideCmfDetils(CMFDetailsActivity.this,cmfDetailsModels.getThirdimage(),cmfDetailsBinding.thiredimage);

                        }else {
                            cmfDetailsBinding.thiredimage.setVisibility(View.GONE);
                        }
                        if (cmfDetailsModels.getForthimage()!=null){
                            cmfDetailsBinding.forthimage.setVisibility(View.VISIBLE);
                            Utlity.Set_imageGlideCmfDetils(CMFDetailsActivity.this,cmfDetailsModels.getForthimage(),cmfDetailsBinding.forthimage);

                        }else {
                            cmfDetailsBinding.forthimage.setVisibility(View.GONE);
                        }
                        if (cmfDetailsModels.getFifthimage()!=null){
                            cmfDetailsBinding.fifthimage.setVisibility(View.VISIBLE);
                            Utlity.Set_imageGlideCmfDetils(CMFDetailsActivity.this,cmfDetailsModels.getFifthimage(),cmfDetailsBinding.fifthimage);

                        }else {
                            cmfDetailsBinding.fifthimage.setVisibility(View.GONE);
                        }
                       /* if (Utlity.get_user(CMFDetailsActivity.this).getUserroles_id().equalsIgnoreCase("2")) {
                            cmfDetailsBinding.tvcommentbox.setText(cmfDetailsModels.getTaskcomment());

                        } else if (Utlity.get_user(CMFDetailsActivity.this).getUserroles_id().equalsIgnoreCase("3")) {
                            cmfDetailsBinding.tvcommentbox.setText(cmfDetailsModels.getCmnote());
                        }*/
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog(CMFDetailsActivity.this);

            }
        });

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        cmfDetailsBinding.signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                cmfDetailsBinding.signsave.setEnabled(true);
                cmfDetailsBinding.signclear.setEnabled(true);
            }

            @Override
            public void onClear() {
                cmfDetailsBinding.signsave.setEnabled(false);
                cmfDetailsBinding.signclear.setEnabled(false);
            }
        });

    }

    private void click() {
        cmfDetailsBinding.back.setOnClickListener(this);
        cmfDetailsBinding.removetask.setOnClickListener(this);
        cmfDetailsBinding.tvbeforimag.setOnClickListener(this);
        cmfDetailsBinding.tvafterimag.setOnClickListener(this);
        cmfDetailsBinding.savetask.setOnClickListener(this);
        cmfDetailsBinding.cancelfirstimg.setOnClickListener(this);
        cmfDetailsBinding.cancelsecondimg.setOnClickListener(this);
        cmfDetailsBinding.cancelthiredimg.setOnClickListener(this);
        cmfDetailsBinding.cancelforthimg.setOnClickListener(this);
        cmfDetailsBinding.cancelfifthimg.setOnClickListener(this);
        cmfDetailsBinding.clickimg.setOnClickListener(this);
        cmfDetailsBinding.firstimage.setOnClickListener(this);
        cmfDetailsBinding.secondimage.setOnClickListener(this);
        cmfDetailsBinding.thiredimage.setOnClickListener(this);
        cmfDetailsBinding.forthimage.setOnClickListener(this);
        cmfDetailsBinding.fifthimage.setOnClickListener(this);
        cmfDetailsBinding.linearsignpad.setOnClickListener(this);
        cmfDetailsBinding.padcancel.setOnClickListener(this);
        cmfDetailsBinding.signature.setOnClickListener(this);
        cmfDetailsBinding.signclear.setOnClickListener(this);
        cmfDetailsBinding.signsave.setOnClickListener(this);
        cmfDetailsBinding.verifiedtask.setOnClickListener(this);
    }
/*
     private void open_iamge_picker1() {
         Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
         startActivityForResult(i, RESULT_LOAD_IMAGE1);
     }

     private void open_iamge_picker2() {
         Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
         startActivityForResult(i, RESULT_LOAD_IMAGE2);
     }
    private void open_iamge_picker3() {
        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, RESULT_LOAD_IMAGE3);
    }

    private void open_iamge_picker4() {
        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, RESULT_LOAD_IMAGE4);
    }
    private void open_iamge_picker5() {
        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, RESULT_LOAD_IMAGE5);
    }*/
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back) {
            if (Utlity.get_user(this).getUserroles_id().equalsIgnoreCase("2")) {
                startActivity(new Intent(this, AdminMainActivity.class));
                finishAffinity();
            } else if (Utlity.get_user(this).getUserroles_id().equalsIgnoreCase("3")) {
                startActivity(new Intent(
                        this, UserMainActivity.class));
                finishAffinity();
            }
        } else if (v.getId() == R.id.removetask) {

            AlertDialog.Builder builder = new AlertDialog.Builder(CMFDetailsActivity.this);
            builder.setTitle(R.string.removetask).
                    setMessage(R.string.sureremovetask);
            builder.setPositiveButton(R.string.yespopup,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            removeTaskData();
                        }

                    });
            builder.setNegativeButton(R.string.nopopup,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder.create();
            alert11.show();
        } else if (v.getId() == R.id.tvbeforimag) {
            // open_iamge_picker();
           // selectImageBefor(this);
            //cmfDetailsBinding.tvbeforimag.setVisibility(View.GONE);
        } else if (v.getId() == R.id.tvafterimag) {
            //  open_iamge_picker1();
          //  selectImageAfter(this);
            //cmfDetailsBinding.tvafterimag.setVisibility(View.GONE);
        } else if (v.getId() == R.id.savetask) {
          /*  if (cmfDetailsBinding.task.getSelectedItemPosition() == 0) {
                Utlity.show_toast(CMFDetailsActivity.this, "Please Select Types");
            } else*/ if (cmfDetailsBinding.commentbox.getText().toString().equals("")) {
                Utlity.show_toast(CMFDetailsActivity.this, "Please Type Comment");
            } else {
                savetaskdata();
            }

        } else if (v.getId() == R.id.cancelfirstimg) {
            cmfDetailsBinding.framelayoutfirst.setVisibility(View.GONE);
            FirstImage = null;
        } else if (v.getId() == R.id.cancelsecondimg) {
            cmfDetailsBinding.framelayoutsecond.setVisibility(View.GONE);
            SecondImage = null;
        } else if (v.getId() == R.id.cancelthiredimg) {
            cmfDetailsBinding.framelayoutthired.setVisibility(View.GONE);
            ThirdImage = null;
        } else if (v.getId() == R.id.cancelforthimg) {
            cmfDetailsBinding.framelayoutforth.setVisibility(View.GONE);
            ForthImage = null;
        } else if (v.getId() == R.id.cancelfifthimg) {
            cmfDetailsBinding.framelayoutfifth.setVisibility(View.GONE);
            FifthImage = null;
        } else if (v.getId() == R.id.clickimg) {
           if (FirstImage == null){
               selectImageFirst(this);
           }else if (SecondImage == null){
               selectImageSecond(this);
           }else if (ThirdImage == null){
               selectImageThird(this);
           }else if (ForthImage == null){
               selectImageForth(this);
           }else if (FifthImage == null) {
               selectImageFifth(this);
           }
        }else if (v.getId()==R.id.firstimage){
            Intent intent= new Intent(this, FullScreenImage.class);
            intent.putExtra("image_url", cmfDetailsModels.getFirstimage());
            intent.putExtra("cmimageid", "1");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
        }
        else if (v.getId()==R.id.secondimage){
            Intent intent= new Intent(this, FullScreenImage.class);
            intent.putExtra("image_url", cmfDetailsModels.getSecondimage());
            intent.putExtra("cmimageid", "1");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
        }
        else if (v.getId()==R.id.thiredimage){
            Intent intent= new Intent(this, FullScreenImage.class);
            intent.putExtra("image_url", cmfDetailsModels.getThirdimage());
            intent.putExtra("cmimageid", "1");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
        }
        else if (v.getId()==R.id.forthimage){
            Intent intent= new Intent(this, FullScreenImage.class);
            intent.putExtra("image_url", cmfDetailsModels.getForthimage());
            intent.putExtra("cmimageid", "1");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
        }
        else if (v.getId()==R.id.fifthimage){
            Intent intent= new Intent(this, FullScreenImage.class);
            intent.putExtra("image_url", cmfDetailsModels.getFifthimage());
            intent.putExtra("cmimageid", "1");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
        }else if (v.getId() ==R.id.signature){
            cmfDetailsBinding.linearsignpad.setVisibility(View.VISIBLE);
            cmfDetailsBinding.signature.setVisibility(View.GONE);
        }else if (v.getId() == R.id.padcancel){
            cmfDetailsBinding.linearsignpad.setVisibility(View.GONE);
            cmfDetailsBinding.signature.setVisibility(View.VISIBLE);
            SignaImage = null;
        }else if (v.getId() == R.id.signclear){
            cmfDetailsBinding.signaturePad.clear();
        }else if (v.getId() == R.id.signsave){
            SignaImage=bitmapToFile(cmfDetailsBinding.signaturePad.getSignatureBitmap());
            SignaSignaImage=saveBitmapToFile(SignaImage);
        }else if (v.getId() == R.id.verifiedtask){
            if (cmfDetailsBinding.verificationcommentbox.getText().toString().equals("")) {
                Utlity.show_toast(CMFDetailsActivity.this, "Please Type Verification Comment");
            }else if (SignaSignaImage == null){
                Utlity.show_toast(CMFDetailsActivity.this, "Please Create Signature");
            }else {
                saveverificationdata();
            }

        }
    }

    private void saveverificationdata() {
        Utlity.show_progress(this);
        verifiedcomment = cmfDetailsBinding.verificationcommentbox.getText().toString();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        if (SignaSignaImage != null) {
            builder.addFormDataPart("verify_image", SignaSignaImage.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), SignaSignaImage));
        } else {
            builder.addFormDataPart("verify_image", "", RequestBody.create(MediaType.parse("multipart/form-data"), ""));
        }

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Gson gson = new Gson();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("cmtaskid", taskid);
        builder.addFormDataPart("verify_comment", verifiedcomment);
        MultipartBody body = builder.build();

        final Request request = new Request.Builder()
                .url(Apis.Base_UrlVerification)
                .header("Authorization", "Bearer " + Utlity.get_user(this).getToken())
                .post(body)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.retryOnConnectionFailure();
        okHttpClient.connectTimeoutMillis();
        okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Utlity.dismiss_dilog(CMFDetailsActivity.this);
            }

            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Utlity.dismiss_dilog(CMFDetailsActivity.this);
                            String apidata = response.body().string();
                            Log.d("responce>>>>>", apidata);
                            JSONObject object = new JSONObject(apidata);
                            if (response.isSuccessful()) {
                                Utlity.show_toast(CMFDetailsActivity.this, object.getString("success"));
                                startActivity(new Intent(CMFDetailsActivity.this, CleaningMaintenanceActivity.class));
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    private void savetaskdata() {
        Utlity.show_progress(this);
        taskcomment = cmfDetailsBinding.commentbox.getText().toString();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        if (FirstFirstImage != null) {
            builder.addFormDataPart("firstimage", FirstFirstImage.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), FirstFirstImage));
        } else {
            builder.addFormDataPart("firstimage", "", RequestBody.create(MediaType.parse("multipart/form-data"), ""));
        }
        if (SecondSecondImage != null) {
            builder.addFormDataPart("secondimage", SecondSecondImage.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), SecondSecondImage));
        } else {
            builder.addFormDataPart("secondimage", "", RequestBody.create(MediaType.parse("multipart/form-data"), ""));
        }
        if (ThirdThirdImage != null) {
            builder.addFormDataPart("thirdimage", ThirdThirdImage.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), ThirdThirdImage));
        } else {
            builder.addFormDataPart("thirdimage", "", RequestBody.create(MediaType.parse("multipart/form-data"), ""));
        }
        if (ForthForthImage != null) {
            builder.addFormDataPart("forthimage", ForthForthImage.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), ForthForthImage));
        } else {
            builder.addFormDataPart("forthimage", "", RequestBody.create(MediaType.parse("multipart/form-data"), ""));
        }
        if (FifthFifthImage != null) {
            builder.addFormDataPart("fifthimage", FifthFifthImage.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), FifthFifthImage));
        } else {
            builder.addFormDataPart("fifthimage", "", RequestBody.create(MediaType.parse("multipart/form-data"), ""));
        }

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Gson gson = new Gson();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("cmtaskid", taskid);
        builder.addFormDataPart("cmscheduleid", removetaskid);
        builder.addFormDataPart("cmnote", taskcomment);
        builder.addFormDataPart("status", tsktype);
        MultipartBody body = builder.build();

        final Request request = new Request.Builder()
                .url(Apis.closetask)
                .header("Authorization", "Bearer " + Utlity.get_user(this).getToken())
                .post(body)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.retryOnConnectionFailure();
        okHttpClient.connectTimeoutMillis();
        okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Utlity.dismiss_dilog(CMFDetailsActivity.this);
            }

            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Utlity.dismiss_dilog(CMFDetailsActivity.this);
                            String apidata = response.body().string();
                            Log.d("responce>>>>>", apidata);
                            JSONObject object = new JSONObject(apidata);
                            if (response.isSuccessful()) {
                                Utlity.show_toast(CMFDetailsActivity.this, object.getString("success"));
                                startActivity(new Intent(CMFDetailsActivity.this, CleaningMaintenanceActivity.class));
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    private void removeTaskData() {
        Utlity.show_progress(this);
        apiInterface = ApiClients.getClient1().create(RetApis.class);
        retrofit2.Call<ResponseBody> call = apiInterface.RemoveTask(removetaskid, "Bearer " + Utlity.get_user(this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        Utlity.dismiss_dilog(CMFDetailsActivity.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>", apidata);
                        JSONObject object = new JSONObject(apidata);
                        Utlity.show_toast(CMFDetailsActivity.this, object.getString("success"));
                        startActivity(new Intent(CMFDetailsActivity.this, CleaningMaintenanceActivity.class));

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog(CMFDetailsActivity.this);

            }
        });
    }

   /* protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == RESULT_LOAD_IMAGE1) && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            cmfDetailsBinding.framelayoutfirst.setVisibility(View.VISIBLE);
            cmfDetailsBinding.firstimg.setImageBitmap(bitmap);
            FirstImage = bitmapToFile(bitmap);
            FirstFirstImage = saveBitmapToFile(FirstImage);
        } else if (requestCode == RESULT_LOAD_IMAGE2 && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            cmfDetailsBinding.framelayoutsecond.setVisibility(View.VISIBLE);
            cmfDetailsBinding.secondimg.setImageBitmap(bitmap);
            SecondImage = bitmapToFile(bitmap);
            SecondSecondImage = saveBitmapToFile(SecondImage);
        } else if (requestCode == RESULT_LOAD_IMAGE3 && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            cmfDetailsBinding.framelayoutthired.setVisibility(View.VISIBLE);
            cmfDetailsBinding.thiredimg.setImageBitmap(bitmap);
            ThirdImage = bitmapToFile(bitmap);
            ThirdThirdImage = saveBitmapToFile(ThirdImage);
        } else if (requestCode == RESULT_LOAD_IMAGE4 && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            cmfDetailsBinding.framelayoutforth.setVisibility(View.VISIBLE);
            cmfDetailsBinding.forthimg.setImageBitmap(bitmap);
            ForthImage = bitmapToFile(bitmap);
            ForthForthImage = saveBitmapToFile(ForthImage);
        } else if (requestCode == RESULT_LOAD_IMAGE5 && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            cmfDetailsBinding.framelayoutfifth.setVisibility(View.VISIBLE);
            cmfDetailsBinding.fifthimg.setImageBitmap(bitmap);
            FifthImage = bitmapToFile(bitmap);
            FifthFifthImage = saveBitmapToFile(FifthImage);
        }

    }


    public File bitmapToFile(Bitmap bmp) {
        try {
            int size = 4000;
            String name = System.currentTimeMillis() + ".jpeg";
            ByteArrayOutputStream bos = new ByteArrayOutputStream(size);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] bArr = bos.toByteArray();
            bos.flush();
            bos.close();

            FileOutputStream fos = openFileOutput(name, Context.MODE_APPEND);
            fos.write(bArr);
            fos.flush();
            fos.close();

            File mFile = new File(getFilesDir().getAbsolutePath(), name);
            return mFile;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public File saveBitmapToFile(File file){
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE=75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }
*/



    private void selectImageFirst(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            private static final String TAG = "tag";

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePicture.resolveActivity(getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                            Log.i(TAG, "IOException");
                        }

                        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                        StrictMode.setVmPolicy(builder.build());
                        builder.detectFileUriExposure();
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                            startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE1);

                        }
                    }

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void selectImageSecond(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            private static final String TAG = "tag";

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePicture.resolveActivity(getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                            Log.i(TAG, "IOException");
                        }

                        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                        StrictMode.setVmPolicy(builder.build());
                        builder.detectFileUriExposure();
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                            startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE2);
                        }

                    }

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE2);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void selectImageThird(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            private static final String TAG = "tag";

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePicture.resolveActivity(getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                            Log.i(TAG, "IOException");
                        }

                        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                        StrictMode.setVmPolicy(builder.build());
                        builder.detectFileUriExposure();
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                            startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE3);
                        }
                    }

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE3);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void selectImageForth(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            private static final String TAG = "tag";

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePicture.resolveActivity(getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                            Log.i(TAG, "IOException");
                        }

                        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                        StrictMode.setVmPolicy(builder.build());
                        builder.detectFileUriExposure();
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                            startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE4);
                        }
                    }

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE4);
                    /*Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , RESULT_LOAD_IMAGE1);*/

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void selectImageFifth(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            private static final String TAG = "tag";

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePicture.resolveActivity(getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                            Log.i(TAG, "IOException");
                        }

                        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                        StrictMode.setVmPolicy(builder.build());
                        builder.detectFileUriExposure();
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                            startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE5);
                        }
                    }

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE5);
                  /*  Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , RESULT_LOAD_IMAGE1);*/

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode== REQUEST_IMAGE_CAPTURE1) {
                try {
                    firstimage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                    cmfDetailsBinding.framelayoutfirst.setVisibility(View.VISIBLE);
                    cmfDetailsBinding.firstimg.setImageBitmap(firstimage);
                    FirstImage=bitmapToFile(firstimage);
                    FirstFirstImage=saveBitmapToFile(FirstImage);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if (requestCode== RESULT_LOAD_IMAGE1) {
                mArrayUri.clear();
                ClipData mClipData = data.getClipData();
                if (mClipData != null){
                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri imageurl = item.getUri();
                        mArrayUri.add(imageurl);
                    }
                    try {
                        if (FirstImage == null){
                            firstimage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mArrayUri.get(0));
                            cmfDetailsBinding.framelayoutfirst.setVisibility(View.VISIBLE);
                            cmfDetailsBinding.firstimg.setImageBitmap(firstimage);
                            FirstImage=bitmapToFile(firstimage);
                            FirstFirstImage=saveBitmapToFile(FirstImage);
                            secondimage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mArrayUri.get(1));
                            cmfDetailsBinding.framelayoutsecond.setVisibility(View.VISIBLE);
                            cmfDetailsBinding.secondimg.setImageBitmap(secondimage);
                            SecondImage=bitmapToFile(secondimage);
                            SecondSecondImage=saveBitmapToFile(SecondImage);
                            thirdimage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mArrayUri.get(2));
                            cmfDetailsBinding.framelayoutthired.setVisibility(View.VISIBLE);
                            cmfDetailsBinding.thiredimg.setImageBitmap(thirdimage);
                            ThirdImage=bitmapToFile(thirdimage);
                            ThirdThirdImage=saveBitmapToFile(ThirdImage);
                            forthimage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mArrayUri.get(3));
                            cmfDetailsBinding.framelayoutforth.setVisibility(View.VISIBLE);
                            cmfDetailsBinding.forthimg.setImageBitmap(forthimage);
                            ForthImage=bitmapToFile(forthimage);
                            ForthForthImage=saveBitmapToFile(ForthImage);
                            fifthimage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mArrayUri.get(4));
                            cmfDetailsBinding.framelayoutfifth.setVisibility(View.VISIBLE);
                            cmfDetailsBinding.fifthimg.setImageBitmap(fifthimage);
                            FifthImage=bitmapToFile(fifthimage);
                            FifthFifthImage=saveBitmapToFile(FifthImage);
                        }

                    } catch (IOException | IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }else {
                    Utlity.show_toast(CMFDetailsActivity.this, "not selected");
                }



            } else  if (requestCode== REQUEST_IMAGE_CAPTURE2) {
                try {
                    secondimage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                    cmfDetailsBinding.framelayoutsecond.setVisibility(View.VISIBLE);
                    cmfDetailsBinding.secondimg.setImageBitmap(secondimage);
                    SecondImage=bitmapToFile(secondimage);
                    SecondSecondImage=saveBitmapToFile(SecondImage);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if (requestCode== RESULT_LOAD_IMAGE2) {
                mArrayUri.clear();
                ClipData mClipData = data.getClipData();
                if (mClipData != null){
                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri imageurl = item.getUri();
                        mArrayUri.add(imageurl);
                    }
                    try {
                     if (SecondImage == null){
                            secondimage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mArrayUri.get(0));
                            cmfDetailsBinding.framelayoutsecond.setVisibility(View.VISIBLE);
                            cmfDetailsBinding.secondimg.setImageBitmap(secondimage);
                            SecondImage=bitmapToFile(secondimage);
                            SecondSecondImage=saveBitmapToFile(SecondImage);
                            thirdimage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mArrayUri.get(1));
                            cmfDetailsBinding.framelayoutthired.setVisibility(View.VISIBLE);
                            cmfDetailsBinding.thiredimg.setImageBitmap(thirdimage);
                            ThirdImage=bitmapToFile(thirdimage);
                            ThirdThirdImage=saveBitmapToFile(ThirdImage);
                            forthimage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mArrayUri.get(2));
                            cmfDetailsBinding.framelayoutforth.setVisibility(View.VISIBLE);
                            cmfDetailsBinding.forthimg.setImageBitmap(forthimage);
                            ForthImage=bitmapToFile(forthimage);
                            ForthForthImage=saveBitmapToFile(ForthImage);
                            fifthimage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mArrayUri.get(3));
                            cmfDetailsBinding.framelayoutfifth.setVisibility(View.VISIBLE);
                            cmfDetailsBinding.fifthimg.setImageBitmap(fifthimage);
                            FifthImage=bitmapToFile(fifthimage);
                            FifthFifthImage=saveBitmapToFile(FifthImage);
                        }

                    } catch (IOException | IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }else {
                    Utlity.show_toast(CMFDetailsActivity.this, "not selected");
                }



            }
            /*else if (requestCode== RESULT_LOAD_IMAGE2) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };

                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);

                c.moveToFirst();

                int columnIndex = c.getColumnIndex(filePath[0]);

                String picturePath = c.getString(columnIndex);

                c.close();
                secondimage = BitmapFactory.decodeFile(picturePath);
                cmfDetailsBinding.framelayoutsecond.setVisibility(View.VISIBLE);
                cmfDetailsBinding.secondimg.setImageBitmap(secondimage);
                SecondImage=bitmapToFile(secondimage);
                SecondSecondImage=saveBitmapToFile(SecondImage);
            }*/

            else  if (requestCode== REQUEST_IMAGE_CAPTURE3) {
                try {
                    thirdimage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                    cmfDetailsBinding.framelayoutthired.setVisibility(View.VISIBLE);
                    cmfDetailsBinding.thiredimg.setImageBitmap(thirdimage);
                    ThirdImage=bitmapToFile(thirdimage);
                    ThirdThirdImage=saveBitmapToFile(ThirdImage);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if (requestCode== RESULT_LOAD_IMAGE3) {
                mArrayUri.clear();
                ClipData mClipData = data.getClipData();
                if (mClipData != null){
                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri imageurl = item.getUri();
                        mArrayUri.add(imageurl);
                    }
                    try {
                       if (ThirdImage == null){
                            thirdimage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mArrayUri.get(0));
                            cmfDetailsBinding.framelayoutthired.setVisibility(View.VISIBLE);
                            cmfDetailsBinding.thiredimg.setImageBitmap(thirdimage);
                            ThirdImage=bitmapToFile(thirdimage);
                            ThirdThirdImage=saveBitmapToFile(ThirdImage);
                            forthimage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mArrayUri.get(1));
                            cmfDetailsBinding.framelayoutforth.setVisibility(View.VISIBLE);
                            cmfDetailsBinding.forthimg.setImageBitmap(forthimage);
                            ForthImage=bitmapToFile(forthimage);
                            ForthForthImage=saveBitmapToFile(ForthImage);
                            fifthimage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mArrayUri.get(2));
                            cmfDetailsBinding.framelayoutfifth.setVisibility(View.VISIBLE);
                            cmfDetailsBinding.fifthimg.setImageBitmap(fifthimage);
                            FifthImage=bitmapToFile(fifthimage);
                            FifthFifthImage=saveBitmapToFile(FifthImage);
                        }

                    } catch (IOException | IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }else {
                    Utlity.show_toast(CMFDetailsActivity.this, "not selected");
                }



            }
            /*else if (requestCode== RESULT_LOAD_IMAGE3) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };

                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);

                c.moveToFirst();

                int columnIndex = c.getColumnIndex(filePath[0]);

                String picturePath = c.getString(columnIndex);

                c.close();
                thirdimage = BitmapFactory.decodeFile(picturePath);
                cmfDetailsBinding.framelayoutthired.setVisibility(View.VISIBLE);
                cmfDetailsBinding.thiredimg.setImageBitmap(thirdimage);
                ThirdImage=bitmapToFile(thirdimage);
                ThirdThirdImage=saveBitmapToFile(ThirdImage);
            }*/


            else  if (requestCode== REQUEST_IMAGE_CAPTURE4) {
                try {
                    forthimage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                    cmfDetailsBinding.framelayoutforth.setVisibility(View.VISIBLE);
                    cmfDetailsBinding.forthimg.setImageBitmap(forthimage);
                    ForthImage=bitmapToFile(forthimage);
                    ForthForthImage=saveBitmapToFile(ForthImage);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if (requestCode== RESULT_LOAD_IMAGE4) {
                mArrayUri.clear();
                ClipData mClipData = data.getClipData();
                if (mClipData != null){
                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri imageurl = item.getUri();
                        mArrayUri.add(imageurl);
                    }
                    try {
                        if (ForthImage == null){
                            forthimage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mArrayUri.get(0));
                            cmfDetailsBinding.framelayoutforth.setVisibility(View.VISIBLE);
                            cmfDetailsBinding.forthimg.setImageBitmap(forthimage);
                            ForthImage=bitmapToFile(forthimage);
                            ForthForthImage=saveBitmapToFile(ForthImage);
                            fifthimage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mArrayUri.get(1));
                            cmfDetailsBinding.framelayoutfifth.setVisibility(View.VISIBLE);
                            cmfDetailsBinding.fifthimg.setImageBitmap(fifthimage);
                            FifthImage=bitmapToFile(fifthimage);
                            FifthFifthImage=saveBitmapToFile(FifthImage);
                        }

                    } catch (IOException | IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }else {
                    Utlity.show_toast(CMFDetailsActivity.this, "not selected");
                }



            }

            /*else if (requestCode== RESULT_LOAD_IMAGE4) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };

                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);

                c.moveToFirst();

                int columnIndex = c.getColumnIndex(filePath[0]);

                String picturePath = c.getString(columnIndex);

                c.close();
                forthimage = BitmapFactory.decodeFile(picturePath);
                cmfDetailsBinding.framelayoutforth.setVisibility(View.VISIBLE);
                cmfDetailsBinding.forthimg.setImageBitmap(forthimage);
                ForthImage=bitmapToFile(forthimage);
                ForthForthImage=saveBitmapToFile(ForthImage);
            }*/
            else  if (requestCode== REQUEST_IMAGE_CAPTURE5) {
                try {
                    fifthimage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                    cmfDetailsBinding.framelayoutfifth.setVisibility(View.VISIBLE);
                    cmfDetailsBinding.fifthimg.setImageBitmap(fifthimage);
                    FifthImage=bitmapToFile(fifthimage);
                    FifthFifthImage=saveBitmapToFile(FifthImage);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if (requestCode== RESULT_LOAD_IMAGE5) {
                mArrayUri.clear();
                ClipData mClipData = data.getClipData();
                if (mClipData != null){
                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri imageurl = item.getUri();
                        mArrayUri.add(imageurl);
                    }
                    try {
                      if (FifthImage == null) {
                            fifthimage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mArrayUri.get(0));
                            cmfDetailsBinding.framelayoutfifth.setVisibility(View.VISIBLE);
                            cmfDetailsBinding.fifthimg.setImageBitmap(fifthimage);
                            FifthImage=bitmapToFile(fifthimage);
                            FifthFifthImage=saveBitmapToFile(FifthImage);
                        }

                    } catch (IOException | IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }else {
                    Utlity.show_toast(CMFDetailsActivity.this, "not selected");
                }



            }
            /*else if (requestCode== RESULT_LOAD_IMAGE5) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };

                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);

                c.moveToFirst();

                int columnIndex = c.getColumnIndex(filePath[0]);

                String picturePath = c.getString(columnIndex);

                c.close();
                fifthimage = BitmapFactory.decodeFile(picturePath);
                cmfDetailsBinding.framelayoutfifth.setVisibility(View.VISIBLE);
                cmfDetailsBinding.fifthimg.setImageBitmap(fifthimage);
                FifthImage=bitmapToFile(fifthimage);
                FifthFifthImage=saveBitmapToFile(FifthImage);
            }*/



        }

    }

    public File bitmapToFile(Bitmap bmp) {
        try {
            int size = 4000;
            String name = System.currentTimeMillis() + ".jpeg";
            ByteArrayOutputStream bos = new ByteArrayOutputStream(size);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] bArr = bos.toByteArray();
            bos.flush();
            bos.close();

            FileOutputStream fos = openFileOutput(name, Context.MODE_APPEND);
            fos.write(bArr);
            fos.flush();
            fos.close();

            File mFile = new File(getFilesDir().getAbsolutePath(), name);
            return mFile;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public File saveBitmapToFile(File file){
        try {
            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE=75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }
}