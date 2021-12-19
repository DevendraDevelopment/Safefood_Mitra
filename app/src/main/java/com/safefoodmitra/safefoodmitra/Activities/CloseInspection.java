package com.safefoodmitra.safefoodmitra.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;
import com.safefoodmitra.safefoodmitra.Apis.Apis;
import com.safefoodmitra.safefoodmitra.CameraAll.EditImageActivity;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.SentsModals;
import com.safefoodmitra.safefoodmitra.Modals.Validation_custome;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.ActivityCloseInspectionBinding;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import id.zelory.compressor.Compressor;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.safefoodmitra.safefoodmitra.Activities.InspectionActivity.valuesall;

public class CloseInspection extends AppCompatActivity implements View.OnClickListener {
    ActivityCloseInspectionBinding closeInspectionBinding;
    ArrayList<Validation_custome> fileds;
    String commentvalue,cost;
    File CapGalImg=null,CapGalCompImg=null;
    SentsModals sentsModals;
    String id,inprocessdate,valupos;
    int inprocessid;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_CAPTURE2 = 2;
    private String mCurrentPhotoPath,spinnervalue="";
    Bitmap mImageBitmap;
    Spinner selectprocess;
    ArrayList<String> selectprocesslist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        closeInspectionBinding= DataBindingUtil.setContentView(this,R.layout.activity_close_inspection);
        selectprocess = closeInspectionBinding.selectprocess;
        sentsModals=new SentsModals();
        if (getIntent()!=null){
            sentsModals= Utlity.gson.fromJson(this.getIntent().getStringExtra("details"), SentsModals.class);
            spinnervalue=getIntent().getStringExtra("spinnervalue");
            id=sentsModals.getId();
            //Utlity.show_toast(CloseInspection.this, id +"&&"+ spinnervalue);
        }
        click();
        selectprocesslist = new ArrayList<>();
        selectprocesslist.add("Close");
        selectprocesslist.add("In Process");
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this,  R.layout.spinneritem, selectprocesslist);
        adapter.setDropDownViewResource( R.layout.spinneritem);
        selectprocess.setAdapter(adapter);
        selectprocess.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                valupos = selectprocess.getSelectedItem().toString();
                if (valupos.equals("Close")){
                    inprocessid=0;
                }else if (valupos.equals("In Process")){
                    inprocessid=4;
                }
              // Utlity.show_toast(CloseInspection.this, String.valueOf(inprocessid));
               if (inprocessid == 4){
                   closeInspectionBinding.linearshowdata.setVisibility(View.VISIBLE);
                  // closeInspectionBinding.linearcost.setVisibility(View.VISIBLE);
               }else {
                   closeInspectionBinding.linearshowdata.setVisibility(View.GONE);
                   //closeInspectionBinding.linearcost.setVisibility(View.GONE);
               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void click(){
        closeInspectionBinding.back.setOnClickListener(this);
        closeInspectionBinding.selectedimgs.setOnClickListener(this);
        closeInspectionBinding.closeinspection.setOnClickListener(this);
        closeInspectionBinding.dtebtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.back){
            if (Utlity.get_user(this).getUserroles_id().equalsIgnoreCase("2")){
                startActivity(new Intent(this,AdminMainActivity.class));
                finishAffinity();
            }
            else if (Utlity.get_user(this).getUserroles_id().equalsIgnoreCase("3")){
                startActivity(new Intent(this,UserMainActivity.class));
                finishAffinity();
            }
        }
        else if (view.getId()==R.id.selectedimgs){
            selectImage(this);
        }
        else if (view.getId()==R.id.closeinspection){
            commentvalue = closeInspectionBinding.commentbox.getText().toString();
            cost = closeInspectionBinding.costbox.getText().toString();
            fileds = new ArrayList<>();
            fileds.add(new Validation_custome("text", closeInspectionBinding.commentbox));
            if (Utlity.validation(this,fileds)){
                if (Utlity.is_online(CloseInspection.this)) {
                    if (inprocessid == 0){
                          if(CapGalImg==null){
                    Utlity.show_toast(CloseInspection.this, "please select image");
                           } else {
                             closeall();
                           }

                    }else if (inprocessid == 4){
                        if (inprocessdate==null){
                            Utlity.show_toast(CloseInspection.this, "please select date");
                        }else {
                            closeall();
                        }

                    }


                } else {
                    Utlity.show_toast(CloseInspection.this, getResources().getString(R.string.nointernet));
                }


            }
        }else if (view.getId()== R.id.dtebtn){
            show_date_picker(CloseInspection.this,closeInspectionBinding.tvshowdate);
        }

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
                inprocessdate= String.valueOf(tv.getText());
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        calendar.add(Calendar.YEAR, 0);
        //dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());// TODO: used to hide future date,month and year
        dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());// TODO: used to hide past date,month and year
        dialog.show();
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
                    /*Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);*/

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
                            startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
                        }
                    }

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
            if (requestCode== REQUEST_IMAGE_CAPTURE) {
                try {
                    mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                    closeInspectionBinding.afterimg.setImageBitmap(mImageBitmap);
                    CapGalImg=bitmapToFile(mImageBitmap);
                    CapGalCompImg=saveBitmapToFile(CapGalImg);
                    // startActivityForResult(new Intent(this, EditImageActivity.class).putExtra("image",mCurrentPhotoPath),REQUEST_IMAGE_CAPTURE);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if (requestCode== REQUEST_IMAGE_CAPTURE2) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                //startActivityForResult(new Intent(this, EditImageActivity.class).putExtra("image",picturePath),REQUEST_IMAGE_CAPTURE2);
                c.close();
                closeInspectionBinding.afterimg.setImageURI(selectedImage);
                Bitmap bitmap = (BitmapFactory.decodeFile(picturePath));
                CapGalImg=bitmapToFile(bitmap);
                CapGalCompImg=saveBitmapToFile(CapGalImg);

            }

        }

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
            bis = new BufferedInputStream(is, 8000);
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

    public  Bitmap decodeBitmap(Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 100;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
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

    private void closeall() {
        Utlity.show_progress(this);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        if (CapGalCompImg!=null){
            Log.d("validate_file_size", String.valueOf(CapGalImg.length() / 1024));
            builder.addFormDataPart("after_img", CapGalCompImg.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), CapGalCompImg));
        }
        else {
            builder.addFormDataPart("after_img","", RequestBody.create(MediaType.parse("multipart/form-data"),""));
        }

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Gson gson = new Gson();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("id",id);
      //  builder.addFormDataPart("inprocess", String.valueOf(inprocessid));
        if (inprocessid==4){
            builder.addFormDataPart("inprocessdate",inprocessdate);
        }else {

        }

        builder.addFormDataPart("after_comment",commentvalue);
        builder.addFormDataPart("process_cost",cost);
        if (spinnervalue!=null){
            builder.addFormDataPart("ins_status",spinnervalue);
        }
        else {
            builder.addFormDataPart("ins_status",String.valueOf(inprocessid));
        }


        MultipartBody body = builder.build();

        final Request request = new Request.Builder()
                .url(Apis.closeinspection)
                .header("Authorization", "Bearer "+Utlity.get_user(this).getToken())
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100,TimeUnit.SECONDS).build();
        okHttpClient.retryOnConnectionFailure();
        okHttpClient.connectTimeoutMillis();
        okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Utlity.dismiss_dilog(CloseInspection.this);
            }

            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Utlity.dismiss_dilog(CloseInspection.this);

                           /* StrictMode.ThreadPolicy policy =
                                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);

                            */
                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            if (response.isSuccessful()){
                                //Utlity.show_toast(CloseInspection.this,apidata+" Success");
                                if (valuesall==0){
                                    startActivity(new Intent(CloseInspection.this,InspectionActivity.class));
                                    finish();
                                }
                                else if (valuesall==1){
                                    startActivity(new Intent(CloseInspection.this,InspectionActivity.class).putExtra("fillter","20"));
                                    finish();
                                }

                            }
                            else {
                                Utlity.show_toast(CloseInspection.this,"failed");
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