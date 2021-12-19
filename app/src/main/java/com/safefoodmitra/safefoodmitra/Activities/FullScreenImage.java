package com.safefoodmitra.safefoodmitra.Activities;

import androidx.appcompat.app.AppCompatActivity;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class FullScreenImage extends AppCompatActivity {
    ImageView myImage,cancle;
    String url = "";
    Bitmap finalBitmap;
    String cmimageid;
    FileOutputStream fileOutputStream;
    TextView btnimag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        url = getIntent().getStringExtra("image_url");
        cmimageid = getIntent().getStringExtra("cmimageid");
        myImage = findViewById(R.id.fullscreenimage);
        cancle = findViewById(R.id.cancle1);
        if (cmimageid!=null){
            if (cmimageid.equals("1")){
                Utlity.Set_imageGlideCmfDetils(FullScreenImage.this,url,myImage);
            }
        }else {
            Utlity.Set_image1(url,myImage);
        }

        btnimag = findViewById(R.id.btnsaveimag);
        btnimag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveimage();
            }
        });
        //saveImage(myImage);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void saveimage(){
        BitmapDrawable drawable =(BitmapDrawable) myImage.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        File filepath = Environment.getExternalStorageDirectory();
        File dir = new File(filepath.getAbsolutePath()+"/DCIM/safoodImages/");
        dir.mkdir();
        File file = new File(dir,System.currentTimeMillis()+".jpg");

        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
        Utlity.show_toast(this,"image saved");
        try {
            fileOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}