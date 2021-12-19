package com.safefoodmitra.safefoodmitra.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.ActivitySummaryBinding;

public class Summary extends AppCompatActivity {
    ActivitySummaryBinding activitySummaryBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySummaryBinding = DataBindingUtil.setContentView(this,R.layout.activity_summary);

    }
}