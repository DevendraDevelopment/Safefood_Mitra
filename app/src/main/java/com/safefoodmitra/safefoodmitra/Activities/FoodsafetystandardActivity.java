package com.safefoodmitra.safefoodmitra.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.safefoodmitra.safefoodmitra.Adapter.FoodSafetyStandardAdapter;
import com.safefoodmitra.safefoodmitra.Adapter.FsmsDocumentsAdapter;
import com.safefoodmitra.safefoodmitra.Apis.ApiClients;
import com.safefoodmitra.safefoodmitra.Apis.RetApis;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.FoodSafetyModel;
import com.safefoodmitra.safefoodmitra.Modals.FsmsDocumentsModals;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.ActivityFoodsafetystandardBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

public class FoodsafetystandardActivity extends AppCompatActivity implements View.OnClickListener {
      ActivityFoodsafetystandardBinding foodsafetystandardBinding;
      List<FoodSafetyModel> foodSafetyModels;
      FoodSafetyStandardAdapter foodSafetyStandardAdapter;
    RetApis apiInterface;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        foodsafetystandardBinding= DataBindingUtil.setContentView(this,R.layout.activity_foodsafetystandard);

        if (Utlity.is_online(this)){
            foodData();
        }
        else Utlity.show_toast(FoodsafetystandardActivity.this,getResources().getString(R.string.nointernet));

       click();
    }



    private void click() {
        foodsafetystandardBinding.back.setOnClickListener(this);
        foodsafetystandardBinding.imgfilter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.back){
            if (Utlity.get_user(this).getUserroles_id().equalsIgnoreCase("2")){
                startActivity(new Intent(this,AdminMainActivity.class));
                finishAffinity();
            }
            else if (Utlity.get_user(this).getUserroles_id().equalsIgnoreCase("3")){
                startActivity(new Intent(this,UserMainActivity.class));
                finishAffinity();
            }
        }else if (v.getId()==R.id.imgfilter){
            startActivity(new Intent(this,AdvanceFilterActivity.class));
        }
    }
    private void foodData() {
        Utlity.show_progress(this);
        apiInterface = ApiClients.getClient1().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.FssCategories("Bearer "+ Utlity.get_user(this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        Utlity.dismiss_dilog(FoodsafetystandardActivity.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        foodSafetyModels = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<FoodSafetyModel>>() {}.getType());
                        if(foodSafetyModels.size()!=0) {
                            foodsafetystandardBinding.dashlist.setHasFixedSize(true);
                            foodsafetystandardBinding.dashlist.setLayoutManager(new GridLayoutManager(FoodsafetystandardActivity.this,3));
                            foodSafetyStandardAdapter= new FoodSafetyStandardAdapter(FoodsafetystandardActivity.this,foodSafetyModels);
                            foodsafetystandardBinding.dashlist.setAdapter(foodSafetyStandardAdapter);
                        }
                        else
                        {
                            foodsafetystandardBinding.noreords.setVisibility(View.VISIBLE);
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog(FoodsafetystandardActivity.this);

            }
        });

    }
}