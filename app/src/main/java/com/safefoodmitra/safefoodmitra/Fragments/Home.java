package com.safefoodmitra.safefoodmitra.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.gson.reflect.TypeToken;
import com.safefoodmitra.safefoodmitra.Activities.EqupmentActivity;
import com.safefoodmitra.safefoodmitra.Activities.UserMainActivity;
import com.safefoodmitra.safefoodmitra.Adapter.DashboardAdapter;
import com.safefoodmitra.safefoodmitra.Adapter.MySliderAdapter;
import com.safefoodmitra.safefoodmitra.Apis.ApiClients;
import com.safefoodmitra.safefoodmitra.Apis.RetApis;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.DashboardModals;
import com.safefoodmitra.safefoodmitra.Modals.MySliderList;
import com.safefoodmitra.safefoodmitra.Modals.UnitModals;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.HomeBinding;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends Fragment {
    public static boolean isRecursionEnable = true;
    HomeBinding homeBinding;
    public DashboardAdapter dashboardAdapter;
    RetApis apiInterface;
    List<MySliderList>mySliderLists;
    MySliderAdapter adapter;
    private static int currentPage = 0;
    private static int NUM_PAGES = 13;
    public List<DashboardModals> dashboardModals;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    Timer swipeTimer;
    String respoid1;
    List<UnitModals> unitModals;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeBinding= DataBindingUtil.inflate(inflater, R.layout.home,container,false);
        mySliderLists = new ArrayList<>();
        if (Utlity.is_online(getActivity())){
            getBabarImg();
            unitdata();
        }
        else {
            Utlity.show_toast(getActivity(),getResources().getString(R.string.nointernet));

        }
        return homeBinding.getRoot();
    }

    private void getBabarImg() {

       /* homeBinding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setupCurrentIndicator(position);
            }
        });*/
        //NUM_PAGES =onBordingLists.size();
       /* final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == mySliderLists.size()) {
                    currentPage = 0;
                }
                homeBinding.viewPager.setCurrentItem(currentPage++, true);
            }
        };*/
      /*  swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);*/
        apiInterface = ApiClients.getClient1().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.BannarLiset("Bearer "+ Utlity.get_user(getActivity()).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        mySliderLists = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<MySliderList>>() {}.getType());
                        if(mySliderLists.size()!=0) {
                            adapter = new MySliderAdapter(getActivity(), mySliderLists);
                            homeBinding.slider.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
                            homeBinding.slider.setSliderAdapter(adapter);
                            homeBinding.slider.setScrollTimeInSec(3);

                            // to set it scrollable automatically
                            // we use below method.
                            homeBinding.slider.setAutoCycle(true);

                            // to start autocycle below method is used.
                            homeBinding.slider.startAutoCycle();
                           // setupIndicator();
                            //setupCurrentIndicator(0);


                        }else {
                            homeBinding.slider.setVisibility(View.GONE);
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

   /* private void setupIndicator() {
        ImageView[] indicator=new ImageView[adapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(4,0,4,0);
        for (int i=0; i<indicator.length; i++){
            indicator[i]=new ImageView(getActivity());
            indicator[i].setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.indicator_inactive));
            indicator[i].setLayoutParams(layoutParams);
            homeBinding.layIndicator.addView(indicator[i]);
        }

    }
    private void setupCurrentIndicator(int index) {
        int itemcildcount=homeBinding.layIndicator.getChildCount();
        for (int i=0; i<itemcildcount; i++){
            ImageView imageView=(ImageView)homeBinding.layIndicator.getChildAt(i);
            try {
                if (i==index){
                    imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.indicator_active));
                }else {
                    imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.indicator_inactive));
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }

        }

    }*/
   public void unitdata(){

       apiInterface = ApiClients.getClient().create(RetApis.class);
       Call<ResponseBody> call=apiInterface.Units("Bearer "+ Utlity.get_user(getActivity()).getToken());
       call.enqueue(new Callback<ResponseBody>() {
           @Override
           public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               if (response.isSuccessful()) {
                   String apidata = null;

                   try {
                       apidata = response.body().string();
                       Log.d("responcedesh>>>>>",apidata);
                       JSONObject object = new JSONObject(apidata);
                       unitModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<UnitModals>>() {}.getType());
                       if (unitModals.size()==0){
                           respoid1 = "0";
                       }

                       dashboardModals=new ArrayList<>();
                       dashboardModals.add(new DashboardModals("Inspection",R.drawable.inspection));
                       dashboardModals.add(new DashboardModals("Record Keeping",R.drawable.recordkeeping));

                       dashboardModals.add(new DashboardModals("Cleaning & maintenance",R.drawable.cleaning));
                       dashboardModals.add(new DashboardModals("FSMS documents",R.drawable.fsms));
                       dashboardModals.add(new DashboardModals("Food  Safety  Standard",R.drawable.foodsafety));

                       dashboardModals.add(new DashboardModals("Safe Food Mitra audit",R.drawable.safefoodmitra));

                       homeBinding.dashlist.setHasFixedSize(true);
                       homeBinding.dashlist.setLayoutManager(new GridLayoutManager(getActivity(),2));
                       dashboardAdapter = new DashboardAdapter(getActivity(), dashboardModals,respoid1);
                       homeBinding.dashlist.setAdapter(dashboardAdapter);
                   } catch (IOException | JSONException e) {
                       e.printStackTrace();
                   }

               }
           }

           @Override
           public void onFailure(Call<ResponseBody> call, Throwable t) {
           }
       });
   }
   /* @Override
    public void onPause() {
        super.onPause();
         swipeTimer.cancel();

    }*/

    @Override
    public void onResume() {
        super.onResume();

    }

}
