package com.safefoodmitra.safefoodmitra.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.safefoodmitra.safefoodmitra.Adapter.NewCategoryAdapter;
import com.safefoodmitra.safefoodmitra.Adapter.ZoneAdapter;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.NewCategoryModel;
import com.safefoodmitra.safefoodmitra.Modals.Validation_custome;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.InternalExternalDetailBinding;

import java.util.ArrayList;
import java.util.List;

public class InternalExternalDetail extends AppCompatActivity implements View.OnClickListener {
    InternalExternalDetailBinding internalExternalDetailBinding;
    public List<NewCategoryModel> newCategoryModels;
    NewCategoryAdapter newCategoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        internalExternalDetailBinding= DataBindingUtil.setContentView(this,R.layout.internal_external_detail);
        newCategoryModels= new ArrayList<>();
        newCategoryModels.add(new NewCategoryModel("parameter category1","3/5","1/05/2021"));
        newCategoryModels.add(new NewCategoryModel("parameter category2","3/5","1/05/2021"));
        newCategoryModels.add(new NewCategoryModel("parameter category3","3/5","1/05/2021"));

        internalExternalDetailBinding.parameterlist.setVisibility(View.VISIBLE);
        internalExternalDetailBinding.parameterlist.setLayoutManager(new LinearLayoutManager(InternalExternalDetail.this, RecyclerView.VERTICAL, false));
        newCategoryAdapter = new NewCategoryAdapter(InternalExternalDetail.this, newCategoryModels);
        internalExternalDetailBinding.parameterlist.setAdapter(newCategoryAdapter);
      click();
    }

    public void click(){
        internalExternalDetailBinding.back.setOnClickListener(this);
        internalExternalDetailBinding.imgadddepart.setOnClickListener(this);
        internalExternalDetailBinding.btnsignclear.setOnClickListener(this);
        internalExternalDetailBinding.threedots.setOnClickListener(this);
        internalExternalDetailBinding.imgrightsummary.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.back){
            onBackPressed();
        }else if (v.getId()==R.id.imgadddepart){
           addDepartment();
        }else if (v.getId()==R.id.btnsignclear){
            internalExternalDetailBinding.signaturePad.clear();
        }else if (v.getId()==R.id.threedots){
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.dotschecklistlayout, null);
            // create the popup window
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true; // lets taps outside the popup also dismiss it
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
            popupWindow.showAtLocation(v, Gravity.TOP | Gravity.RIGHT, 0, 125);
            TextView tvpart1 = popupView.findViewById(R.id.part1);
            tvpart1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });
            TextView tvpart2 = popupView.findViewById(R.id.part2);
            tvpart2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });
        }else if (v.getId()==R.id.imgrightsummary){
          startActivity(new Intent(InternalExternalDetail.this,Summary.class));
        }
    }

    public void addDepartment() {
        final Dialog dialog = new Dialog(InternalExternalDetail.this);
        final ImageButton cancle;
        final TextView save;

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.adddepartment);
        dialog.getWindow().setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
        save=dialog.findViewById(R.id.adddepart);
        cancle=dialog.findViewById(R.id.cancle);

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        dialog.show();

    }
}