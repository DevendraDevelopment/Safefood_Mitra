package com.safefoodmitra.safefoodmitra.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.safefoodmitra.safefoodmitra.Adapter.FsmsDocumentsAdapter;
import com.safefoodmitra.safefoodmitra.Adapter.RecordAdapter;
import com.safefoodmitra.safefoodmitra.Apis.ApiClients;
import com.safefoodmitra.safefoodmitra.Apis.RetApis;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.FsmsDocumentsModals;
import com.safefoodmitra.safefoodmitra.Modals.RecordModals;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.ActivityFsmsdocumentBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

public class FsmsdocumentActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityFsmsdocumentBinding activityFsmsdocumentBinding;
    public FsmsDocumentsAdapter fsmsDocumentsAdapter;
    List<FsmsDocumentsModals> fsmsDocumentsModals;
    RetApis apiInterface;
    SharedPreferences pref;
    String respoid1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityFsmsdocumentBinding = DataBindingUtil.setContentView(this,R.layout.activity_fsmsdocument);
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        pref = this.getSharedPreferences("MyG9", MODE_PRIVATE);
        respoid1 = pref.getString("respoid",null);

        // fsmsDocumentsModals = new ArrayList<>();
        if (Utlity.is_online(this)){
            recorddata();
        }
        else Utlity.show_toast(FsmsdocumentActivity.this,getResources().getString(R.string.nointernet));
        click();
    }
    private void recorddata() {
        Utlity.show_progress(this);
        apiInterface = ApiClients.getClient1().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.FsmsCategories(respoid1,"Bearer "+ Utlity.get_user(this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        Utlity.dismiss_dilog(FsmsdocumentActivity.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        fsmsDocumentsModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<FsmsDocumentsModals>>() {}.getType());
                        if(fsmsDocumentsModals.size()!=0) {
                            activityFsmsdocumentBinding.dashlist.setHasFixedSize(true);
                            activityFsmsdocumentBinding.dashlist.setLayoutManager(new GridLayoutManager(FsmsdocumentActivity.this,3));
                            fsmsDocumentsAdapter = new FsmsDocumentsAdapter(FsmsdocumentActivity.this,fsmsDocumentsModals);
                            activityFsmsdocumentBinding.dashlist.setAdapter(fsmsDocumentsAdapter);
                        }
                        else
                        {
                            activityFsmsdocumentBinding.noreords.setVisibility(View.VISIBLE);
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog(FsmsdocumentActivity.this);

            }
        });
    }
    private void click() {
        activityFsmsdocumentBinding.back.setOnClickListener(this);
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
        }
    }

}