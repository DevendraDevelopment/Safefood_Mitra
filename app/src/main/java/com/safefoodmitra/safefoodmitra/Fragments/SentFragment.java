package com.safefoodmitra.safefoodmitra.Fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;
import com.safefoodmitra.safefoodmitra.Activities.InspectionActivity;
import com.safefoodmitra.safefoodmitra.Activities.UnitsActivity;
import com.safefoodmitra.safefoodmitra.Activities.ZoneActivity;
import com.safefoodmitra.safefoodmitra.Adapter.DashboardAdapter;
import com.safefoodmitra.safefoodmitra.Adapter.SentsAdapter;
import com.safefoodmitra.safefoodmitra.Adapter.ZoneAdapter;
import com.safefoodmitra.safefoodmitra.Apis.ApiClients;
import com.safefoodmitra.safefoodmitra.Apis.RetApis;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.SentsModals;
import com.safefoodmitra.safefoodmitra.Modals.ZoneModals;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.SentfragBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Addfobunits;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.inspections;
import static com.safefoodmitra.safefoodmitra.Fragments.RecivedFragment.norecord1;
import static com.safefoodmitra.safefoodmitra.Fragments.RecivedFragment.receivelist;

public class SentFragment extends Fragment {
    SentfragBinding sentfragBinding;
    public SentsAdapter sentsAdapter;
    public List<SentsModals> sentsModals;
    RetApis apiInterface;
    public static RecyclerView sentlist;
    public static TextView norecord;
    String respoiduser,respoid1;
    SharedPreferences pref;
    int page=1;
    int limit=10;
    boolean isScrolling=true;
    private int currentitems,totaliitems,scrolloutitems;
    LinearLayoutManager linearLayoutManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sentfragBinding= DataBindingUtil.inflate(inflater, R.layout.sentfrag,container,false);
        pref = getActivity().getSharedPreferences("MyG9", MODE_PRIVATE);
        respoid1 = pref.getString("respoid",null);

        sentlist=sentfragBinding.sentslist;
        norecord=sentfragBinding.noreords;
      //  respoiduser=Utlity.get_user(getActivity()).getFobunits_id();
        respoiduser=pref.getString("respoid",null);
        if (Utlity.is_online(getActivity())){
            if (Utlity.get_user(getActivity()).getUserroles_id().equalsIgnoreCase("2")){
                if (valuesall==1){
                    inspectionsearcsentsrecived(onefromdate,twofromdate,locatioid,responseid,converid,spinnervalue);
                }
                else {
                    inspectionsdataadmin();

                   // getListDataAdmin(page,limit);
                }

            }
            else if (Utlity.get_user(getActivity()).getUserroles_id().equalsIgnoreCase("3")){
                if (valuesall==1){
                    inspectionsearcsentsrecived(onefromdate,twofromdate,locatioid,responseid,converid,spinnervalue);
                }
                else {
                    if (respoid1.equals("0")){
                        showDialogBox();
                    }else {
                        inspectionsdatauser();
                    }

                }

            }

        }
        else {
            Utlity.show_toast(getActivity(),getResources().getString(R.string.nointernet));
        }
       /* sentfragBinding.paginationnestscrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
                    page++;
                    sentfragBinding.paginationProgress.setVisibility(View.VISIBLE);
                    getListDataAdmin(page,limit);
                }
            }
        });*/
       sentfragBinding.upArrow.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               sentfragBinding.sentslist.smoothScrollToPosition(0);
           }
       });
        sentfragBinding.sentslist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    Log.d("pppp","Scrolling up");
                   sentfragBinding.upArrow.setVisibility(View.VISIBLE);
                    // Scrolling up
                } else {
                    Log.d("pppp","Scrolling down");
                    sentfragBinding.upArrow.setVisibility(View.GONE);
                    // Scrolling down
                }
            }


        });
        return sentfragBinding.getRoot();
    }
 /* private void getListDataAdmin(int page,int limit){
      apiInterface = ApiClients.getClient1().create(RetApis.class);
      retrofit2.Call<ResponseBody> call=apiInterface.Inspectionssendpagination(respoid1,"Bearer "+ Utlity.get_user(getActivity()).getToken(),page,limit);
      call.enqueue(new retrofit2.Callback<ResponseBody>() {
          @Override
          public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
              if (response.isSuccessful()) {
                  String apidata = null;
                  try {
                      sentfragBinding.paginationProgress.setVisibility(View.GONE);
                      apidata = response.body().string();
                      Log.d("responce>>>>>",apidata);
                      JSONObject object = new JSONObject(apidata);
                      sentsModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SentsModals>>() {}.getType());
                      if(sentsModals.size()!=0) {
                          sentfragBinding.noreords.setVisibility(View.GONE);
                          sentfragBinding.sentslist.setVisibility(View.VISIBLE);
                          sentfragBinding.sentslist.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                          sentsAdapter = new SentsAdapter(getActivity(), sentsModals);
                          sentfragBinding.sentslist.setAdapter(sentsAdapter);
                      }
                      else
                      {
                          sentfragBinding.noreords.setVisibility(View.VISIBLE);
                          sentfragBinding.sentslist.setVisibility(View.GONE);
                      }

                  } catch (IOException | JSONException e) {
                      e.printStackTrace();
                  }
              }
          }

          @Override
          public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
            //  Utlity.dismiss_dilog(getActivity());

          }
      });

  }*/
   /* private void inspectionsdata() {
        Utlity.show_progress(getActivity());
        apiInterface = ApiClients.getClient1().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Inspections("Bearer "+ Utlity.get_user(getActivity()).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        Utlity.dismiss_dilog(getActivity());
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        sentsModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SentsModals>>() {}.getType());
                        if(sentsModals.size()!=0) {
                            sentfragBinding.noreords.setVisibility(View.GONE);
                            sentfragBinding.sentslist.setVisibility(View.VISIBLE);
                            sentfragBinding.sentslist.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                            sentsAdapter = new SentsAdapter(getActivity(), sentsModals);
                            sentfragBinding.sentslist.setAdapter(sentsAdapter);
                        }
                        else
                        {
                            sentfragBinding.noreords.setVisibility(View.VISIBLE);
                            sentfragBinding.sentslist.setVisibility(View.GONE);
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog(getActivity());

            }
        });

    }*/
    private void inspectionsdataadmin() {
        Utlity.show_progress(getActivity());
        Request result= get(inspections+respoid+"/"+"1");
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Utlity.dismiss_dilog(getActivity());
                try {
                   // Utlity.show_toast(getActivity(), "Not Founded");
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
                                Utlity.dismiss_dilog(getActivity());
                                apidata = response.body().string();
                                Log.d("responce>>>>>",apidata);
                                JSONObject object = new JSONObject(apidata);
                                sentsModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SentsModals>>() {}.getType());
                                if(sentsModals.size()!=0) {
                                    sentfragBinding.noreords.setVisibility(View.GONE);
                                    sentfragBinding.sentslist.setVisibility(View.VISIBLE);
                                    linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                                    sentfragBinding.sentslist.setLayoutManager(linearLayoutManager);
                                    sentsAdapter = new SentsAdapter(getActivity(), sentsModals);
                                    sentfragBinding.sentslist.setAdapter(sentsAdapter);
                                }
                                else
                                {
                                    sentfragBinding.noreords.setVisibility(View.VISIBLE);
                                    sentfragBinding.sentslist.setVisibility(View.GONE);
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
        Utlity.show_progress(getActivity());
        Request result= get(inspections+respoiduser+"/"+"1");
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Utlity.dismiss_dilog(getActivity());
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
                                Utlity.dismiss_dilog(getActivity());
                                apidata = response.body().string();
                                Log.d("responce>>>>>",apidata);
                                JSONObject object = new JSONObject(apidata);
                                sentsModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SentsModals>>() {}.getType());
                                if(sentsModals.size()!=0) {
                                    sentfragBinding.noreords.setVisibility(View.GONE);
                                    sentfragBinding.sentslist.setVisibility(View.VISIBLE);
                                    linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                                    sentfragBinding.sentslist.setLayoutManager(linearLayoutManager);
                                    sentsAdapter = new SentsAdapter(getActivity(), sentsModals);
                                    sentfragBinding.sentslist.setAdapter(sentsAdapter);
                                }
                                else
                                {
                                    sentfragBinding.noreords.setVisibility(View.VISIBLE);
                                    sentfragBinding.sentslist.setVisibility(View.GONE);
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
    private void inspectionsearcsentsrecived(String firstdate,String secondate,String locatioid,String respoid, String converid, String status) {

        Utlity.show_progress(getActivity());
        String respoid1 = pref.getString("respoid",null);
        apiInterface = ApiClients.getClient1().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Inspectionsearchdatasen(respoid1,"Bearer "+ Utlity.get_user(getActivity()).getToken(),firstdate,secondate,locatioid,respoid,converid,status);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        Utlity.dismiss_dilog(getActivity());
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);

                        JSONObject object = new JSONObject(apidata);
                        sentsModals1 = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SentsModals>>() {}.getType());
                        if(sentsModals1.size()!=0) {
                            norecord.setVisibility(View.GONE);
                            linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                            sentlist.setLayoutManager(linearLayoutManager);
                            sentsAdapter = new SentsAdapter(getActivity(), sentsModals1);
                            sentlist.setAdapter(sentsAdapter);
                            /*inspectionBinding.firstdateshow.setText("");
                            inspectionBinding.seconddateshow.setText("");

                             */


                        }
                        else if (sentsModals1.size()==0){
                            norecord.setVisibility(View.VISIBLE);
                            sentlist.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                            sentsAdapter = new SentsAdapter(getActivity(), sentsModals1);
                            sentlist.setAdapter(sentsAdapter);
                           /* inspectionBinding.firstdateshow.setText("");
                            inspectionBinding.seconddateshow.setText("");

                            */

                        }
                        else
                        {
                            norecord.setVisibility(View.VISIBLE);
                        }


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog(getActivity());

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
    public void showDialogBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.nounitidtitle).
                setMessage(R.string.notunitid);
        builder.setPositiveButton(R.string.closepopup,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder.create();
        alert11.show();
    }
}
