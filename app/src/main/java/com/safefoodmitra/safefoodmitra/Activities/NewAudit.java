package com.safefoodmitra.safefoodmitra.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.Validation_custome;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.ActivityNewAuditBinding;

import java.util.ArrayList;

public class NewAudit extends AppCompatActivity  implements View.OnClickListener{
    ActivityNewAuditBinding newAuditBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newAuditBinding= DataBindingUtil.setContentView(this,R.layout.activity_new_audit);
        click();

    }
    public void click(){
        newAuditBinding.back.setOnClickListener(this);
        newAuditBinding.start.setOnClickListener(this);
        newAuditBinding.mapicon.setOnClickListener(this);
        newAuditBinding.imgdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.back){
            onBackPressed();
        }

        else if (v.getId()==R.id.start){
            startAlert();
        }

        else if (v.getId()==R.id.mapicon){
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr="));
            startActivity(intent);
        }else if (v.getId()==R.id.imgdate){
            Utlity.show_date_picker_audit(NewAudit.this,newAuditBinding.tvauditdate);
        }
    }

    public  void startAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(NewAudit.this);
        builder.setTitle(R.string.confirmationpopup).
                setMessage(R.string.startpopup);
        builder.setPositiveButton(R.string.yespopup,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(NewAudit.this,InternalExternalDetail.class));
                        dialog.cancel();
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

    }

}