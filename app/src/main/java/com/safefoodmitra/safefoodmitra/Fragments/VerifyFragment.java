package com.safefoodmitra.safefoodmitra.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;
import com.safefoodmitra.safefoodmitra.Adapter.SentsAdapter;
import com.safefoodmitra.safefoodmitra.Apis.ApiClients;
import com.safefoodmitra.safefoodmitra.Apis.RetApis;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.SentsModals;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.RecivedfragBinding;
import com.safefoodmitra.safefoodmitra.databinding.VerifiedfragBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.safefoodmitra.safefoodmitra.Apis.Apis.inspections;

public class VerifyFragment extends Fragment {
    VerifiedfragBinding verifiedfragBinding;
    public SentsAdapter sentsAdapter;
    public List<SentsModals> sentsModals;
    RetApis apiInterface;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        verifiedfragBinding= DataBindingUtil.inflate(inflater, R.layout.verifiedfrag,container,false);
       
        if (Utlity.is_online(getActivity())){
            inspectionsdata();
        }
        else {
            Utlity.show_toast(getActivity(),getResources().getString(R.string.nointernet));
        }
        return verifiedfragBinding.getRoot();
    }

    private void inspectionsdata() {
       // Utlity.show_progress(getActivity());
        Request result= get(inspections+"3");
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
         //       Utlity.dismiss_dilog(getActivity());
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
                 //               Utlity.dismiss_dilog(getActivity());
                                apidata = response.body().string();
                                Log.d("responce>>>>>",apidata);
                                JSONObject object = new JSONObject(apidata);
                                sentsModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SentsModals>>() {}.getType());
                                if(sentsModals.size()!=0) {
                                    verifiedfragBinding.noreords.setVisibility(View.GONE);
                                    verifiedfragBinding.verifylist.setVisibility(View.VISIBLE);
                                    verifiedfragBinding.verifylist.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                                    sentsAdapter = new SentsAdapter(getActivity(), sentsModals);
                                    verifiedfragBinding.verifylist.setAdapter(sentsAdapter);
                                }
                                else
                                {
                                    verifiedfragBinding.noreords.setVisibility(View.VISIBLE);
                                    verifiedfragBinding.verifylist.setVisibility(View.GONE);
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
