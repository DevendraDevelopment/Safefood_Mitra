package com.safefoodmitra.safefoodmitra.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.SubFoodDocspdfModel;
import com.safefoodmitra.safefoodmitra.R;

import java.io.File;

import okhttp3.Request;


public class PdfActivity extends AppCompatActivity implements View.OnClickListener {
    WebView pdfView;
    String pdfFileName,baseurl,toturl;
    //DocumentspdfModel documentspdfModel;
    SubFoodDocspdfModel subFoodDocspdfModel;
    ProgressBar progressBar;
    TextView tvHeader;
    ImageView back,share,download;
    ProgressDialog progressBar1;
    String notificationpdflink="",title;
    private static final int MY_PERMISSION_REQUEST_CODE = 123;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        progressBar1 = new ProgressDialog(this);
        progressBar1.setCancelable(true);
        progressBar1.setMessage(getResources().getString(R.string.loading));

        //checkPermission();
        pdfView = (WebView) findViewById(R.id.pdfView);
        tvHeader = (TextView) findViewById(R.id.titles);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        download = findViewById(R.id.download);
        share = findViewById(R.id.share);
        back = findViewById(R.id.back);
        notificationpdflink = getIntent().getStringExtra("pdflink");

        if (notificationpdflink != null) {
            progressBar1.show();
            title = getIntent().getStringExtra("pdftitle");
            tvHeader.setText(title);
            pdfView.setWebViewClient(new MyWebViewClient());
            pdfView.getSettings().setBuiltInZoomControls(true);
            pdfView.getSettings().setJavaScriptEnabled(true);
            pdfView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            pdfView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + notificationpdflink);
            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(notificationpdflink)));
                   // Utlity.show_toast(PdfActivity.this, "download");

                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myintent = new Intent(Intent.ACTION_SEND);
                    myintent.setType("text/plain");
                    String shareBody = notificationpdflink;
                    String shareSub = "Your Subject here";
                    myintent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                    myintent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(myintent, "Share using"));
                }
            });

        }
       else if (getIntent() != null) {
            progressBar1.show();
            subFoodDocspdfModel = Utlity.gson.fromJson(getIntent().getStringExtra("detail"), SubFoodDocspdfModel.class);
            tvHeader.setText(subFoodDocspdfModel.getDoc_title());
            pdfFileName = subFoodDocspdfModel.getDoc_path();
            baseurl = subFoodDocspdfModel.getBaseurl();
            toturl = baseurl + "/" + pdfFileName;
            pdfView.setWebViewClient(new MyWebViewClient());
            pdfView.getSettings().setBuiltInZoomControls(true);
            pdfView.getSettings().setJavaScriptEnabled(true);
            pdfView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            pdfView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + toturl);

            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(toturl)));
                   // Utlity.show_toast(PdfActivity.this, "download");

                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myintent = new Intent(Intent.ACTION_SEND);
                    myintent.setType("text/plain");
                    String shareBody = toturl;
                    String shareSub = "Your Subject here";
                    myintent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                    myintent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(myintent, "Share using"));
                }
            });

        }

        click();
    }

    private void click() {
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.back){
            onBackPressed();
            /*if (Utlity.get_user(this).getUserroles_id().equalsIgnoreCase("2")){
                startActivity(new Intent(this,AdminMainActivity.class));
                finishAffinity();
            }
            else if (Utlity.get_user(this).getUserroles_id().equalsIgnoreCase("3")){
                startActivity(new Intent(this,UserMainActivity.class));
                finishAffinity();
            }*/
        }
    }


    private class MyWebViewClient extends WebViewClient {
        boolean loadingFinished = true;
        boolean redirect = false;
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
           // progressBar.setVisibility(View.VISIBLE);
            //progressBar1.show();
            if (!loadingFinished) {
                redirect = true;
                progressBar1.cancel();
            }

            loadingFinished = false;

            view.loadUrl(url);
            return true;

        }

        public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            super.onPageStarted(webView, str, bitmap);
            //progressBar1.show();
            loadingFinished = false;
            progressBar1.cancel();
           // progressBar.setVisibility(View.VISIBLE);
        }

        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);

            if(!redirect){
                loadingFinished = true;

            }
            //call remove_splash in 500 miSec
            if(loadingFinished && !redirect){

            } else{
                redirect = false;

            }

          //  progressBar.setVisibility(View.GONE);

        }

    }

}