package com.safefoodmitra.safefoodmitra.Fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;
import com.safefoodmitra.safefoodmitra.Activities.InspectionActivity;
import com.safefoodmitra.safefoodmitra.Adapter.SentsAdapter;
import com.safefoodmitra.safefoodmitra.Apis.ApiClients;
import com.safefoodmitra.safefoodmitra.Apis.RetApis;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.SentsModals;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.RecivedfragBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.content.Context.MODE_PRIVATE;
import static com.safefoodmitra.safefoodmitra.Activities.AdminMainActivity.respoid;
import static com.safefoodmitra.safefoodmitra.Activities.InspectionActivity.converid;
import static com.safefoodmitra.safefoodmitra.Activities.InspectionActivity.locatioid;
import static com.safefoodmitra.safefoodmitra.Activities.InspectionActivity.onefromdate;
import static com.safefoodmitra.safefoodmitra.Activities.InspectionActivity.responseid;
import static com.safefoodmitra.safefoodmitra.Activities.InspectionActivity.sentsModals1;
import static com.safefoodmitra.safefoodmitra.Activities.InspectionActivity.spinnervalue;
import static com.safefoodmitra.safefoodmitra.Activities.InspectionActivity.twofromdate;
import static com.safefoodmitra.safefoodmitra.Activities.InspectionActivity.valuesall;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.inspections;

public class RecivedFragment extends Fragment {
    RecivedfragBinding recivedfragBinding;
    public SentsAdapter sentsAdapter;
    public List<SentsModals> sentsModals;
    public static RecyclerView receivelist;
    public static TextView norecord1;
    String respoiduser;
    RetApis apiInterface;
    SharedPreferences pref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recivedfragBinding= DataBindingUtil.inflate(inflater, R.layout.recivedfrag,container,false);
        pref = getActivity().getSharedPreferences("MyG9", MODE_PRIVATE);
        receivelist=recivedfragBinding.receivelist;
        norecord1=recivedfragBinding.noreords;
       // respoiduser=Utlity.get_user(getActivity()).getFobunits_id();
        respoiduser= pref.getString("respoid",null);
        if (Utlity.is_online(getActivity())){
            if (Utlity.get_user(getActivity()).getUserroles_id().equalsIgnoreCase("2")){

                if (valuesall==1){
                    inspectionsearchrecived(onefromdate,twofromdate,locatioid,responseid,converid,spinnervalue);
                }
                else {
                    inspectionsdataadmin();
                }
            }
            else if (Utlity.get_user(getActivity()).getUserroles_id().equalsIgnoreCase("3")){
                if (valuesall==1){
                    inspectionsearchrecived(onefromdate,twofromdate,locatioid,responseid,converid,spinnervalue);
                }
                else {
                    inspectionsdatauser();
                }
            }

        }
        else {
            Utlity.show_toast(getActivity(),getResources().getString(R.string.nointernet));
        }

        return recivedfragBinding.getRoot();
    }

    private void inspectionsdataadmin() {
        Request result= get(inspections+respoid+"/"+"2");
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                try {
                    Utlity.show_toast(getActivity(), "Not Founded");
                }catch (RuntimeException e1){
                    e1.printStackTrace();
                }
            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            String apidata = null;
                            try {

                                apidata = response.body().string();
                                Log.d("responce>>>>>",apidata);
                                JSONObject object = new JSONObject(apidata);
                                sentsModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SentsModals>>() {}.getType());
                                if(sentsModals.size()!=0) {
                                    recivedfragBinding.noreords.setVisibility(View.GONE);
                                    recivedfragBinding.receivelist.setVisibility(View.VISIBLE);
                                    recivedfragBinding.receivelist.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                                    sentsAdapter = new SentsAdapter(getActivity(), sentsModals);
                                    recivedfragBinding.receivelist.setAdapter(sentsAdapter);
                                }
                                else
                                {
                                    recivedfragBinding.noreords.setVisibility(View.VISIBLE);
                                    recivedfragBinding.receivelist.setVisibility(View.GONE);
                                }

                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });

            }

        });

    }
    private void inspectionsdatauser() {
        Request result= get(inspections+respoiduser+"/"+"2");
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                try {
                    Utlity.show_toast(getActivity(), "Not Founded");
                }catch (RuntimeException e1){
                    e1.printStackTrace();
                }
            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            String apidata = null;
                            try {

                                apidata = response.body().string();
                                Log.d("responce>>>>>",apidata);
                                JSONObject object = new JSONObject(apidata);
                                sentsModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SentsModals>>() {}.getType());
                                if(sentsModals.size()!=0) {
                                    recivedfragBinding.noreords.setVisibility(View.GONE);
                                    recivedfragBinding.receivelist.setVisibility(View.VISIBLE);
                                    recivedfragBinding.receivelist.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                                    sentsAdapter = new SentsAdapter(getActivity(), sentsModals);
                                    recivedfragBinding.receivelist.setAdapter(sentsAdapter);
                                }
                                else
                                {
                                    recivedfragBinding.noreords.setVisibility(View.VISIBLE);
                                    recivedfragBinding.receivelist.setVisibility(View.GONE);
                                }

                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });

            }

        });

    }
    private void inspectionsearchrecived(String firstdate,String secondate,String locatioid,String responseid, String converid,String status) {

        String respoid1 = pref.getString("respoid",null);
        apiInterface = ApiClients.getClient1().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Inspectionsearchdatarecive(respoid1,"Bearer "+ Utlity.get_user(getActivity()).getToken(),firstdate,secondate,locatioid,responseid,converid,status);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {

                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        sentsModals1 = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SentsModals>>() {}.getType());
                        if(sentsModals1.size()!=0) {
                            norecord1.setVisibility(View.GONE);
                            receivelist.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                            sentsAdapter = new SentsAdapter(getActivity(), sentsModals1);
                            receivelist.setAdapter(sentsAdapter);
                            /*inspectionBinding.firstdateshow.setText("");
                            inspectionBinding.seconddateshow.setText("");
                             */
                        }
                        else if (sentsModals1.size()==0){
                            norecord1.setVisibility(View.VISIBLE);
                            receivelist.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                            sentsAdapter = new SentsAdapter(getActivity(), sentsModals1);
                            receivelist.setAdapter(sentsAdapter);
                            /*inspectionBinding.firstdateshow.setText("");
                            inspectionBinding.seconddateshow.setText("");
                            */

                        }
                        else
                        {
                            norecord1.setVisibility(View.VISIBLE);
                        }



                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    public Request get(String api_name) {
        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        return new Request.Builder()
                .url(api_name)
                .get()
                .header("Authorization","Bearer "+ Utlity.get_user((Activity) getActivity()).getToken())
                .build();
    }

}
