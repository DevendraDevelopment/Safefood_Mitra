package com.safefoodmitra.safefoodmitra.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.safefoodmitra.safefoodmitra.Adapter.AuditAdapter;
import com.safefoodmitra.safefoodmitra.Adapter.SentsAdapter;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.Room.DatabaseClient;
import com.safefoodmitra.safefoodmitra.Room.TaskSync;
import com.safefoodmitra.safefoodmitra.databinding.ActivityAuditBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class AuditActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityAuditBinding activityAuditBinding;
    AuditAdapter auditAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAuditBinding = DataBindingUtil.setContentView(this,R.layout.activity_audit);
        if (Utlity.is_online(this)){
            AuditData();
        }
        else {
            Utlity.show_toast(AuditActivity.this,getResources().getString(R.string.nointernet));

        }
       click();
    }
    private void AuditData() {
        activityAuditBinding.auditslist.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        auditAdapter = new AuditAdapter(this);
        activityAuditBinding.auditslist.setAdapter(auditAdapter);
    }

    private void click() {
        activityAuditBinding.threedots.setOnClickListener(this);
        activityAuditBinding.back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.threedots){
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.dotslayout, null);
            // create the popup window
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true; // lets taps outside the popup also dismiss it
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
            popupWindow.showAtLocation(v, Gravity.TOP | Gravity.RIGHT, 0, 125);
            TextView tvAll = popupView.findViewById(R.id.tvAll);
            tvAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                //    startActivity(new Intent(AuditActivity.this,InternalExternal.class).putExtra("all","0"));
                }
            });
            TextView tvInternal = popupView.findViewById(R.id.tvInternalAudit);
            tvInternal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
              //      startActivity(new Intent(AuditActivity.this,InternalExternal.class).putExtra("internal","1"));
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });
            TextView tvExternal = popupView.findViewById(R.id.tvExternalAudit);
            tvExternal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
            //        startActivity(new Intent(AuditActivity.this,InternalExternal.class).putExtra("external","2"));
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });
            TextView tvNesSatarAudit = popupView.findViewById(R.id.tvNewStartAudit);
            tvNesSatarAudit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
               startActivity(new Intent(AuditActivity.this,InternalExternal.class));
                }
            });
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
}