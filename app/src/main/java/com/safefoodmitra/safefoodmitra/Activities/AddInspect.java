package com.safefoodmitra.safefoodmitra.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.transition.Explode;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.safefoodmitra.safefoodmitra.Apis.ApiClients;
import com.safefoodmitra.safefoodmitra.Apis.Apis;
import com.safefoodmitra.safefoodmitra.Apis.RetApis;
import com.safefoodmitra.safefoodmitra.CameraAll.EditImageActivity;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.ConcernModal;
import com.safefoodmitra.safefoodmitra.Modals.LocationModal;
import com.safefoodmitra.safefoodmitra.Modals.RespoModal;
import com.safefoodmitra.safefoodmitra.Modals.Validation_custome;
import com.safefoodmitra.safefoodmitra.Modals.ZoneModals;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.ActivityAddInspectBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import static com.safefoodmitra.safefoodmitra.Activities.AdminMainActivity.respoid;
import static java.security.AccessController.getContext;

public class AddInspect extends AppCompatActivity implements View.OnClickListener {
    ActivityAddInspectBinding addInspectBinding;
    File CapGalImg=null;
    ArrayList<Validation_custome> fileds;
    String responseid,locatioid,converid,commentvalue;
    RetApis apiInterface;
    ArrayList<String> respolist;
    ArrayList<String> locationlist;
    ArrayList<String> concernlist;
    List<RespoModal> respoModals;
    List<ConcernModal> concernModals;
    List<LocationModal> locationModals;
    Spinner responsibility,location,concerarea,subconcern;
    String bitimg,path;
    Uri imguri;
    Bitmap bitmaps,mImageBitmap;
    private String mCurrentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_CAPTURE2 = 2;
    String respoiduser;
    SharedPreferences pref;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        // set an exit transition
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Explode());
        }

        addInspectBinding= DataBindingUtil.setContentView(this,R.layout.activity_add_inspect);
        pref = this.getSharedPreferences("MyG9", MODE_PRIVATE);
        responsibility=addInspectBinding.responsbility;
        location=addInspectBinding.locations;
        concerarea=addInspectBinding.concernarea;
        subconcern = addInspectBinding.subconcernarea;
        responsibility.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0){
                    responseid=respoModals.get(position-1).getId();
                   // Utlity.show_toast(AddInspect.this, responseid);

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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        concerarea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0){
                    converid=concernModals.get(position-1).getId();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


        if (getIntent().getExtras()!=null){
            path = getIntent().getStringExtra("image");
            imguri= Uri.parse(path);
            bitmaps=loadBitmap(path);
            CapGalImg=bitmapToFile(bitmaps);

            if(imguri!=null) {
                addInspectBinding.beforeimg.setImageURI(imguri);

            }
            else {
                Utlity.show_toast(this,"null");
            }
        }


        click();

        if (Utlity.is_online(this)){
            respolist();
            concernlist();
            locationlist();
        }
        else {
            Utlity.show_toast(AddInspect.this, getResources().getString(R.string.nointernet));
        }

    }


    public void click(){
        addInspectBinding.back.setOnClickListener(this::onClick);
        addInspectBinding.selectedimg.setOnClickListener(this::onClick);
        addInspectBinding.send.setOnClickListener(this::onClick);
    }



    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.back) {
            if (Utlity.get_user(this).getUserroles_id().equalsIgnoreCase("2")){
                startActivity(new Intent(this,AdminMainActivity.class));
                finishAffinity();
            }
            else if (Utlity.get_user(this).getUserroles_id().equalsIgnoreCase("3")){
                startActivity(new Intent(this,UserMainActivity.class));
                finishAffinity();
            }
        }
        else if (view.getId() == R.id.selectedimg) {

            selectImage(this);
        }


        else if (view.getId() == R.id.send) {

            commentvalue = addInspectBinding.commentbox.getText().toString();
            fileds = new ArrayList<>();
            fileds.add(new Validation_custome("text", addInspectBinding.commentbox));
            if (Utlity.validation(AddInspect.this, fileds)) {
                if (responsibility.getSelectedItemPosition() == 0) {
                    Utlity.show_toast(AddInspect.this, "please Select Responsbility");

                } else if (location.getSelectedItemPosition() == 0) {
                    Utlity.show_toast(AddInspect.this, "please Select Locations");

                } else if (concerarea.getSelectedItemPosition() == 0) {
                    Utlity.show_toast(AddInspect.this, "please Select Concern Area");
                }

                else if (Utlity.is_online(AddInspect.this)){
                    if (Utlity.get_user(AddInspect.this).getUserroles_id().equalsIgnoreCase("2")){
                        addalladmin();
                        imguri=null;
                    }
                    else if (Utlity.get_user(AddInspect.this).getUserroles_id().equalsIgnoreCase("3")){
                        addalluser();
                        imguri=null;
                    }


                }

                else {
                    Utlity.show_toast(AddInspect.this, getResources().getString(R.string.nointernet));
                }
            }

        }
    }


    private void respolist() {
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
                        Utlity.dismiss_dilog(AddInspect.this);
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
                                new ArrayAdapter<String>(AddInspect.this,  R.layout.spinneritem, respolist);
                        adapter1.setDropDownViewResource( R.layout.spinneritem);
                        addInspectBinding.responsbility.setAdapter(adapter1);


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog(AddInspect.this);

            }
        });

    }
    private void locationlist() {
        String respoid1 = pref.getString("respoid",null);
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
                                new ArrayAdapter<String>(AddInspect.this,  R.layout.spinneritem, locationlist);
                        adapter1.setDropDownViewResource( R.layout.spinneritem);
                        addInspectBinding.locations.setAdapter(adapter1);


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
             //   Utlity.show_toast(AddInspect.this, "Not found");

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
                                new ArrayAdapter<String>(AddInspect.this,  R.layout.spinneritem, concernlist);
                        adapter1.setDropDownViewResource( R.layout.spinneritem);
                        addInspectBinding.concernarea.setAdapter(adapter1);


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
               // Utlity.show_toast(AddInspect.this, "Not found");

            }
        });

    }


    public Bitmap loadBitmap(String url)
    {
        Bitmap bm = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        try
        {
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is, 8192);
            bm = BitmapFactory.decodeStream(bis);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if (bis != null)
            {
                try
                {
                    bis.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return bm;
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

    private void addalladmin() {
        Utlity.show_progress(this);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        if (CapGalImg!=null){
            Log.d("validate_file_size", String.valueOf(CapGalImg.length() / 1024));
            builder.addFormDataPart("before_img", CapGalImg.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), CapGalImg));

        }
        else {
            builder.addFormDataPart("before_img","", RequestBody.create(MediaType.parse("multipart/form-data"),""));
        }


        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Gson gson = new Gson();
        builder.setType(MultipartBody.FORM);

        builder.addFormDataPart("locations_id",locatioid);
        builder.addFormDataPart("departments_id", responseid);
        builder.addFormDataPart("concern_area", converid);
        builder.addFormDataPart("before_comment",commentvalue);
        builder.addFormDataPart("fobunits_id",respoid);
        MultipartBody body = builder.build();

        final Request request = new Request.Builder()
                .url(Apis.addinspection)
                .header("Authorization", "Bearer "+Utlity.get_user(this).getToken())
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100,TimeUnit.SECONDS).build();

        //OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.retryOnConnectionFailure();
        okHttpClient.connectTimeoutMillis();
        okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Utlity.dismiss_dilog(AddInspect.this);
               // Utlity.show_toast(AddInspect.this,e.toString());
            }

            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Utlity.dismiss_dilog(AddInspect.this);
                            StrictMode.ThreadPolicy policy =
                                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            if (response.isSuccessful()){
                              //  Utlity.show_toast(AddInspect.this,apidata+" Success");
                                startActivity(new Intent(AddInspect.this,InspectionActivity.class));
                                finish();
                            }
                            else {
                                Utlity.show_toast(AddInspect.this,"failed");
                            }
                        } catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

    }
    private void addalluser() {
        Utlity.show_progress(this);
        respoiduser=pref.getString("respoid",null);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        if (CapGalImg!=null){
            Log.d("validate_file_size", String.valueOf(CapGalImg.length() / 1024));
            builder.addFormDataPart("before_img", CapGalImg.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), CapGalImg));

        }
        else {
            builder.addFormDataPart("before_img","", RequestBody.create(MediaType.parse("multipart/form-data"),""));
        }


        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Gson gson = new Gson();
        builder.setType(MultipartBody.FORM);

        builder.addFormDataPart("locations_id",locatioid);
        builder.addFormDataPart("departments_id", responseid);
        builder.addFormDataPart("concern_area", converid);
        builder.addFormDataPart("before_comment",commentvalue);
        builder.addFormDataPart("fobunits_id",respoiduser);
        MultipartBody body = builder.build();

        final Request request = new Request.Builder()
                .url(Apis.addinspection)
                .header("Authorization", "Bearer "+Utlity.get_user(this).getToken())
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100,TimeUnit.SECONDS).build();

        //OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.retryOnConnectionFailure();
        okHttpClient.connectTimeoutMillis();
        okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Utlity.dismiss_dilog(AddInspect.this);
                Utlity.show_toast(AddInspect.this,e.toString());
            }

            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Utlity.dismiss_dilog(AddInspect.this);
                            StrictMode.ThreadPolicy policy =
                                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            if (response.isSuccessful()){
                               // Utlity.show_toast(AddInspect.this,apidata+" Success");
                                startActivity(new Intent(AddInspect.this,InspectionActivity.class));
                                finish();
                            }
                            else {
                                Utlity.show_toast(AddInspect.this,"failed");
                            }
                        } catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

    }

    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            private static final String TAG = "tag";

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    dispatchTakePictureIntent();

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , REQUEST_IMAGE_CAPTURE2);

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
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.safefoodmitra.safefoodmitra",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

                if (resultCode == RESULT_OK) {
                    if (requestCode== REQUEST_IMAGE_CAPTURE) {
                        try {
                            mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                        startActivityForResult(new Intent(this, EditImageActivity.class).putExtra("image",mCurrentPhotoPath),REQUEST_IMAGE_CAPTURE);

                    }
                    else if (requestCode== REQUEST_IMAGE_CAPTURE2) {
                        Uri selectedImage = data.getData();
                        String[] filePath = { MediaStore.Images.Media.DATA };

                        Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);

                        c.moveToFirst();

                        int columnIndex = c.getColumnIndex(filePath[0]);

                        String picturePath = c.getString(columnIndex);
                        startActivityForResult(new Intent(this, EditImageActivity.class).putExtra("image",picturePath),REQUEST_IMAGE_CAPTURE2);
                        c.close();


                    }

        }
    }


}