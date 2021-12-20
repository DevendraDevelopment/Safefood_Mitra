package com.safefoodmitra.safefoodmitra.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.Validation_custome;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.ActivityOtpBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.safefoodmitra.safefoodmitra.Apis.Apis.Fbtoken;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Login;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Otp;

public class OtpActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityOtpBinding otpBinding;
    ArrayList<Validation_custome> fileds;
    String otpnumber,usernumber,otpdb,fbtoken="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        otpBinding= DataBindingUtil.setContentView(this,R.layout.activity_otp);
//        fbtoken= FirebaseInstanceId.getInstance().getToken();
        Log.d("fbtoken",fbtoken);
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            if (!TextUtils.isEmpty(token)) {
                fbtoken= token;
//                Log.d(TAG, "retrieve token successful : " + token);
            } else{
//                Log.w(TAG, "token should not be null...");
            }
        }).addOnFailureListener(e -> {
            //handle e
        }).addOnCanceledListener(() -> {
            //handle cancel
        }).addOnCompleteListener(task ->
                Log.v("TAG", "This is the token : " + task.getResult()));

        otpdb= Utlity.get_user(this).getOtp();
        if (getIntent()!=null){
            usernumber = getIntent().getStringExtra("usernumber");
        }
        timecount();

        clicks();
    }

    public void timecount(){
        long duratin= TimeUnit.MINUTES.toMillis(1);
        new CountDownTimer(duratin, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                otpBinding.resendotpin.setVisibility(View.VISIBLE);
                otpBinding.resendhide.setVisibility(View.GONE);

                String sduration=String.format(Locale.ENGLISH,"%02d : %02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)-
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MICROSECONDS.toMinutes(millisUntilFinished)));


                otpBinding.timesecond.setText(sduration+" seconds");
            }

            @Override
            public void onFinish() {
                otpBinding.resendotpin.setVisibility(View.GONE);
                otpBinding.resendhide.setVisibility(View.VISIBLE);

            }
        }.start();
    }

    public void clicks(){
        otpBinding.nextscreen.setOnClickListener(this);
        otpBinding.resendtotps.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if (view.getId()==R.id.nextscreen){
            otpnumber=otpBinding.userotp.getText().toString();

            fileds = new ArrayList<>();
            fileds.add(new Validation_custome("text", otpBinding.userotp));
            if (Utlity.validation(this, fileds)) {
                if (Utlity.is_online(this)) {
                    doverify(otpnumber);
                }
                else {
                    Utlity.show_toast(this, getResources().getString(R.string.nointernet));
                }

            }
        }

        else if (view.getId()==R.id.resendtotps){
            timecount();
            dologin(usernumber);

        }

    }
    private void dologin(final String usernumber) {
        HashMap<String, String> keys = new HashMap<>();
        keys.put("mobile_no", usernumber);
        Request result= Utlity.post(this, keys, Login);
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            if (response.isSuccessful()){
                                Utlity.user_db(OtpActivity.this,object.getJSONObject("success").toString());

                            }
                            else {
                                String error = object.getString("error");
                                showdata(error);
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


    private void doverify(final String otpnumber) {
        Utlity.show_progress(this);
        HashMap<String, String> keys = new HashMap<>();
        keys.put("mobile_no", usernumber);
        keys.put("otp", otpnumber);
        Request result= Utlity.post(this, keys, Otp);
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Utlity.dismiss_dilog(OtpActivity.this);
            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Utlity.dismiss_dilog(OtpActivity.this);
                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            if (response.isSuccessful()){
                                Utlity.user_db(OtpActivity.this,object.getJSONObject("success").toString());
                                if (Utlity.get_user(OtpActivity.this).getUserroles_id().equalsIgnoreCase("2")){
                                    Utlity.show_toast(OtpActivity.this,"You Are Login Sucessfully");
                                    startActivity(new Intent(OtpActivity.this,AdminMainActivity.class));
                                    finishAffinity();
                                }

                                else if (Utlity.get_user(OtpActivity.this).getUserroles_id().equalsIgnoreCase("3")){
                                    Utlity.show_toast(OtpActivity.this,"You Are Login Sucessfully");
                                    startActivity(new Intent(OtpActivity.this,UserMainActivity.class));
                                    finishAffinity();
                                }
                                dotoken(fbtoken);
                            }
                            else {
                                Utlity.show_toast(OtpActivity.this,"Unauthorised / Otp dont match");
                            }
                        }
                        catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

        });

    }
    private void dotoken(final String fbtoken) {
        HashMap<String, String> keys = new HashMap<>();
        keys.put("fb_token",fbtoken);
        Request result= post(keys, Fbtoken);
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            if (response.isSuccessful()){
                                Log.d("token", object.getJSONObject("success").toString());
                            }
                            else {
                                Utlity.show_toast(OtpActivity.this,"Problem");
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
        final Dialog dialog = new Dialog(OtpActivity.this);
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

    public Request post(HashMap<String, String> keys, String api_name) {
        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        FormBody.Builder body = new FormBody.Builder();
        for (Object key : keys.keySet()) {
            String value = keys.get(key);
            if(!TextUtils.isEmpty(value)) {
                body.add(key.toString(), value);
            }
            else {
                body.add(key.toString(), "");
            }
        }
        RequestBody parmetrs = body.build();
        return new Request.Builder()
                .url(api_name)
                .header("Authorization","Bearer "+ Utlity.get_user(this).getToken())
                .post(parmetrs)
                .build();
    }

}