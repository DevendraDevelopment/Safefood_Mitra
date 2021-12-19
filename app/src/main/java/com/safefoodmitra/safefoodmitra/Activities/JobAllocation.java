package com.safefoodmitra.safefoodmitra.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;

import com.google.gson.reflect.TypeToken;
import com.safefoodmitra.safefoodmitra.Adapter.AllocationAdapter;
import com.safefoodmitra.safefoodmitra.Adapter.UsersAdapter;
import com.safefoodmitra.safefoodmitra.Apis.ApiClients;
import com.safefoodmitra.safefoodmitra.Apis.RetApis;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.RespoModal;
import com.safefoodmitra.safefoodmitra.Modals.UserModals;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.JobAllocationBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

import static com.safefoodmitra.safefoodmitra.Activities.AdminMainActivity.respoid;

public class JobAllocation extends AppCompatActivity implements View.OnClickListener {
    JobAllocationBinding jobAllocationBinding;
    RetApis apiInterface;
    List<UserModals>userModals;
    AllocationAdapter allocationAdapter;
    Animation topanim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        jobAllocationBinding= DataBindingUtil.setContentView(this,R.layout.job_allocation);
        click();

        if (Utlity.is_online(this)){
            usersdata();
        }
        else {
            Utlity.show_toast(JobAllocation.this,getResources().getString(R.string.nointernet));
        }

        jobAllocationBinding.searching.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                allocationAdapter.getFilter().filter(charSequence);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void click(){
        jobAllocationBinding.back.setOnClickListener(this);
        jobAllocationBinding.serchlocation.setOnClickListener(this);
        jobAllocationBinding.cancle.setOnClickListener(this);
    }
    @SuppressLint("ResourceType")
    @Override
    public void onClick(View view) {
        topanim= AnimationUtils.loadAnimation(this,R.xml.top_animation);
        if (view.getId()==R.id.back){
            onBackPressed();
        }

        else if (view.getId()==R.id.serchlocation){
            jobAllocationBinding.searclayout.setAnimation(topanim);
            jobAllocationBinding.searclayout.setVisibility(View.VISIBLE);
            jobAllocationBinding.serchlocation.setVisibility(View.GONE);
            jobAllocationBinding.titles.setVisibility(View.GONE);
        }

        else if (view.getId()==R.id.cancle){
            jobAllocationBinding.searclayout.setVisibility(View.GONE);
            jobAllocationBinding.serchlocation.setVisibility(View.VISIBLE);
            jobAllocationBinding.titles.setVisibility(View.VISIBLE);
        }

    }


    private void usersdata() {
        Utlity.show_progress(this);
        apiInterface = ApiClients.getClient().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Users(respoid,"Bearer "+ Utlity.get_user(this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        Utlity.dismiss_dilog(JobAllocation.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        userModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<UserModals>>() {}.getType());

                        if(userModals.size()!=0) {
                            jobAllocationBinding.noreords.setVisibility(View.GONE);
                            jobAllocationBinding.joblist.setLayoutManager(new LinearLayoutManager(JobAllocation.this, RecyclerView.VERTICAL, false));
                            allocationAdapter = new AllocationAdapter(JobAllocation.this, userModals);
                            jobAllocationBinding.joblist.setAdapter(allocationAdapter);
                        }
                        else
                        {
                            jobAllocationBinding.noreords.setVisibility(View.VISIBLE);

                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog(JobAllocation.this);

            }
        });

    }


}