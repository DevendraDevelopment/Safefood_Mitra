package com.safefoodmitra.safefoodmitra.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.ActivityEditProfileBinding;

public class EditProfile extends AppCompatActivity implements View.OnClickListener {
    ActivityEditProfileBinding profileBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileBinding= DataBindingUtil.setContentView(this,R.layout.activity_edit_profile);
        click();
    }
    private void click(){
        profileBinding.back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.back){
            onBackPressed();
        }

    }
}