package com.safefoodmitra.safefoodmitra.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.Validation_custome;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.ActivityAdminRegisterBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.safefoodmitra.safefoodmitra.Apis.Apis.Login;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Register;

public class AdminRegister extends AppCompatActivity implements View.OnClickListener {
    ActivityAdminRegisterBinding registerBinding;
    ArrayList<Validation_custome> fileds;
    String firnme,lstnme,mobnumber,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBinding= DataBindingUtil.setContentView(this,R.layout.activity_admin_register);
        click();
    }
    public void click(){
        registerBinding.logingo.setOnClickListener(this);
        registerBinding.nextlogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.logingo){
            startActivity(new Intent(AdminRegister.this,LoginActivity.class));
        }

        if (view.getId()==R.id.nextlogin){
            firnme=registerBinding.firstnme.getText().toString();
            lstnme=registerBinding.secondnme.getText().toString();
            mobnumber=registerBinding.mobilenumber.getText().toString();
            email=registerBinding.emaildi.getText().toString();
            fileds = new ArrayList<>();
            fileds.add(new Validation_custome("text", registerBinding.firstnme));
            fileds.add(new Validation_custome("text", registerBinding.secondnme));
            fileds.add(new Validation_custome("text", registerBinding.mobilenumber));
            fileds.add(new Validation_custome("text", registerBinding.emaildi));

            if (Utlity.validation(this,fileds)) {
                if (Utlity.isValidMobile(mobnumber)){
                    if (Utlity.is_online(this)) {
                        doregister(firnme,lstnme,mobnumber,email);
                    }
                    else {
                        Utlity.show_toast(this, getResources().getString(R.string.nointernet));
                    }

                } else {
                    Utlity.show_toast(this,"please enter 10 digit mobile number");

                }

            }


        }
    }

    private void doregister(final String firstnme,String lastnme,String mobilenumber,String email) {
        Utlity.show_progress(this);
        HashMap<String, String> keys = new HashMap<>();
        keys.put("first_name", firstnme);
        keys.put("last_name", lastnme);
        keys.put("mobile_no", mobilenumber);
        keys.put("email", email);
        Request result= Utlity.post(this, keys, Register);
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Utlity.dismiss_dilog(AdminRegister.this);
            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Utlity.dismiss_dilog(AdminRegister.this);
                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            if (response.isSuccessful()){
//Utlity.show_toast(LoginActivity.this,object.getJSONObject("success").toString());
                                Utlity.user_db(AdminRegister.this,object.getJSONObject("success").toString());
                                startActivity(new Intent(AdminRegister.this,OtpActivity.class).putExtra("usernumber",mobilenumber));
                            }
                            else {
                                String error = object.getString("error");
                                showdata(error);
                               // Utlity.show_toast(AdminRegister.this,"User Already Registered!");
                            }
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

        });

    }
    private void showdata(String error){
        final Dialog dialog = new Dialog(AdminRegister.this);
        final ImageButton cancle;
        final TextView tverror;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirmdilog);
        dialog.getWindow().setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
        cancle=dialog.findViewById(R.id.cancle);
        tverror = dialog.findViewById(R.id.tverror);
        tverror.setText(error);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
}