package com.safefoodmitra.safefoodmitra.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding splashBinding;
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        AnimationDrawable animationDrawable=(AnimationDrawable)splashBinding.layout.getBackground();
        animationDrawable.setEnterFadeDuration(200);
        animationDrawable.setExitFadeDuration(200);
        animationDrawable.start();
       thread();
       // startActivity(new Intent(SplashActivity.this, UserMainActivity.class));
       // finishAffinity();
    }

    public void thread() {
        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    boolean dialogShown = settings.getBoolean("dialogShown", false);
                    if (!dialogShown) {
                        startActivity(new Intent(SplashActivity.this, AboutApp.class));
                        finishAffinity();
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putBoolean("dialogShown", true);
                        editor.commit();
                    } else {
                        if (!TextUtils.isEmpty(Utlity.get_user(SplashActivity.this).getId()) && Utlity.get_user(SplashActivity.this).getUserroles_id().equalsIgnoreCase("2")) {
                            startActivity(new Intent(SplashActivity.this, AdminMainActivity.class));
                        } else if (!TextUtils.isEmpty(Utlity.get_user(SplashActivity.this).getId()) && Utlity.get_user(SplashActivity.this).getUserroles_id().equalsIgnoreCase("3")) {
                            startActivity(new Intent(SplashActivity.this, UserMainActivity.class));
                        } else {
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        }
                        finishAffinity();
                    }
                }
            }
        };
        thread.start();
    }
}