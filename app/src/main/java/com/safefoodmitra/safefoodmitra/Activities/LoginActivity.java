package com.safefoodmitra.safefoodmitra.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.safefoodmitra.safefoodmitra.Apis.Apis;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.Validation_custome;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.safefoodmitra.safefoodmitra.Apis.Apis.Login;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityLoginBinding loginBinding;
    ArrayList<Validation_custome> fileds;
    String usernumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding= DataBindingUtil.setContentView(this,R.layout.activity_login);
        click();
    }
    public void click(){
        loginBinding.nextlogin.setOnClickListener(this);
        loginBinding.registergo.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.nextlogin){
            usernumber=loginBinding.usernumber.getText().toString();
            fileds = new ArrayList<>();
            fileds.add(new Validation_custome("text", loginBinding.usernumber));
            if (Utlity.validation(this,fileds)) {
                if (Utlity.isValidMobile(usernumber)){
                    if (Utlity.is_online(this)) {
                        dologin(usernumber);
                    }
                    else {
                        Utlity.show_toast(this, getResources().getString(R.string.nointernet));
                    }

                } else {
                    Utlity.show_toast(this,"please enter 10 digit mobile number");

                }

            }

        }
        else if (view.getId()==R.id.registergo){
            startActivity(new Intent(LoginActivity.this,AdminRegister.class));

        }
    }

    private void dologin(final String usernumber) {
        Utlity.show_progress(this);
        HashMap<String, String> keys = new HashMap<>();
        keys.put("mobile_no", usernumber);
        Request result= Utlity.post(this, keys, Login);
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Utlity.dismiss_dilog(LoginActivity.this);
            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Utlity.dismiss_dilog(LoginActivity.this);
                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            if (response.isSuccessful()){
                                Utlity.user_db(LoginActivity.this,object.getJSONObject("success").toString());
                                startActivity(new Intent(LoginActivity.this,OtpActivity.class).putExtra("usernumber",usernumber));
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
private void showdata(String error){
    final Dialog dialog = new Dialog(LoginActivity.this);
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