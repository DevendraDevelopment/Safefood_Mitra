package com.safefoodmitra.safefoodmitra.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.safefoodmitra.safefoodmitra.Adapter.DocumentsAdapter;
import com.safefoodmitra.safefoodmitra.Adapter.DocumentspdfAdapter;
import com.safefoodmitra.safefoodmitra.Adapter.FsmsDocumentsAdapter;
import com.safefoodmitra.safefoodmitra.Apis.ApiClients;
import com.safefoodmitra.safefoodmitra.Apis.RetApis;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.DocumentsModel;
import com.safefoodmitra.safefoodmitra.Modals.DocumentspdfModel;
import com.safefoodmitra.safefoodmitra.Modals.FsmsDocumentsModals;
import com.safefoodmitra.safefoodmitra.Modals.SubFoodDocspdfModel;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.ActivityDocumentlistBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.safefoodmitra.safefoodmitra.Apis.Apis.Addlocations;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.documentlist;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.documentpdflist;

public class DocumentlistActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityDocumentlistBinding documentlistBinding;
    DocumentsAdapter documentsAdapter;
    FsmsDocumentsModals fsmsDocumentsModal;
    List<DocumentsModel> documentsModels;
    List<SubFoodDocspdfModel> documentspdfModels;
    DocumentspdfAdapter documentspdfAdapter;
    RetApis apiInterface;
    String id,respoid1;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        documentlistBinding = DataBindingUtil.setContentView(this,R.layout.activity_documentlist);
        pref = this.getSharedPreferences("MyG9", MODE_PRIVATE);
        respoid1 = pref.getString("respoid",null);
        if (getIntent()!=null){
            fsmsDocumentsModal= Utlity.gson.fromJson(getIntent().getStringExtra("detail"),FsmsDocumentsModals.class);
            documentlistBinding.titles.setText(fsmsDocumentsModal.getCat_name());
            id=fsmsDocumentsModal.getId();
        }
        if (Utlity.is_online(this)){
            // documentdata();
            docsdata();
            docspdfile();
        }
        click();
    }

    private void click() {
        documentlistBinding.back.setOnClickListener(this);
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
    private void docsdata() {
        Utlity.show_progress(this);
        apiInterface = ApiClients.getClient1().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.FsmsDocs(id,respoid1,"Bearer "+ Utlity.get_user(this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        Utlity.dismiss_dilog(DocumentlistActivity.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        documentsModels = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<DocumentsModel>>() {}.getType());
                        if(documentsModels.size()!=0) {
                            documentlistBinding.dashlist.setHasFixedSize(true);
                            documentlistBinding.dashlist.setLayoutManager(new GridLayoutManager(DocumentlistActivity.this,3));
                            documentsAdapter = new DocumentsAdapter(DocumentlistActivity.this,documentsModels);
                            documentlistBinding.dashlist.setAdapter(documentsAdapter);
                        }


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog(DocumentlistActivity.this);

            }
        });
    }
    private void docspdfile() {
        // Utlity.show_progress(this);
        apiInterface = ApiClients.getClient1().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.FsmsSubDocs(id,respoid1,"Bearer "+ Utlity.get_user(this).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        //     Utlity.dismiss_dilog(DocumentlistActivity.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        documentspdfModels = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SubFoodDocspdfModel>>() {}.getType());
                        if(documentspdfModels.size()!=0) {
                            documentlistBinding.fillelist.setHasFixedSize(true);
                            documentlistBinding.fillelist.setLayoutManager(new GridLayoutManager(DocumentlistActivity.this,3));
                            documentspdfAdapter = new DocumentspdfAdapter(DocumentlistActivity.this,documentspdfModels);
                            documentlistBinding.fillelist.setAdapter(documentspdfAdapter);
                        }
                        else
                        {
                            documentlistBinding.noreords.setVisibility(View.VISIBLE);
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                //   Utlity.dismiss_dilog(DocumentlistActivity.this);
                Log.d("responce>>>>>",t.getMessage());

            }
        });
    }

    /*  private void document(){
          Utlity.show_progress(this);
          Request result= get(this, documentlist+id);
          OkHttpClient okHttpClient= new OkHttpClient();
          okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
              @Override
              public void onFailure(okhttp3.Call call, IOException e) {
                  Utlity.dismiss_dilog(DocumentlistActivity.this);
              }

              @Override
              public void onResponse(okhttp3.Call call, final Response response) throws IOException {

                  runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          try {
                              Utlity.dismiss_dilog(DocumentlistActivity.this);
                             String apidata = response.body().string();
                              Log.d("responce>>>>>",apidata);
                              JSONObject object = new JSONObject(apidata);
                              documentsModels = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<DocumentsModel>>() {}.getType());
                              if(documentsModels.size()!=0) {
                                  documentlistBinding.dashlist.setHasFixedSize(true);
                                  documentlistBinding.dashlist.setLayoutManager(new GridLayoutManager(DocumentlistActivity.this,3));
                                  documentsAdapter = new DocumentsAdapter(DocumentlistActivity.this,documentsModels);
                                  documentlistBinding.dashlist.setAdapter(documentsAdapter);
                              }
                              else
                              {
                                  documentlistBinding.noreords.setVisibility(View.VISIBLE);
                              }

                          } catch (IOException | JSONException e) {
                              e.printStackTrace();
                          }

                      }
                  });

              }

          });
      }
      private void documentpdffile(){
          //Utlity.show_progress(this);
          Request result= get(this, documentpdflist+id);
          OkHttpClient okHttpClient= new OkHttpClient();
          okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
              @Override
              public void onFailure(okhttp3.Call call, IOException e) {
                  //Utlity.dismiss_dilog(DocumentlistActivity.this);
              }

              @Override
              public void onResponse(okhttp3.Call call, final Response response) throws IOException {

                  runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          try {
                              //Utlity.dismiss_dilog(DocumentlistActivity.this);
                              String apidata = response.body().string();
                              Log.d("responce>>>>>",apidata);
                              JSONObject object = new JSONObject(apidata);
                              documentspdfModels = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SubFoodDocspdfModel>>() {}.getType());
                              if(documentspdfModels.size()!=0) {
                                  documentlistBinding.fillelist.setHasFixedSize(true);
                                  documentlistBinding.fillelist.setLayoutManager(new GridLayoutManager(DocumentlistActivity.this,3));
                                  documentspdfAdapter = new DocumentspdfAdapter(DocumentlistActivity.this,documentspdfModels);
                                  documentlistBinding.fillelist.setAdapter(documentspdfAdapter);
                              }
                              else
                              {
                                  documentlistBinding.noreords.setVisibility(View.VISIBLE);
                              }

                          } catch (IOException | JSONException e) {
                              e.printStackTrace();
                          }

                      }
                  });

              }

          });
      }
      */
    public static Request get(Activity activity, String api_name) {
        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        return new Request.Builder()
                .url(api_name)
                .get()
                .header("Authorization", "Bearer "+ Utlity.get_user(activity).getToken())
                .build();
    }


}