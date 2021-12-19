package com.safefoodmitra.safefoodmitra.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.safefoodmitra.safefoodmitra.Adapter.SubFoodDocpdfAdapter;
import com.safefoodmitra.safefoodmitra.Adapter.SubFoodSafetyFifthStandardAdapter;
import com.safefoodmitra.safefoodmitra.Adapter.SubFoodSafetyFourthStandardAdapter;
import com.safefoodmitra.safefoodmitra.Adapter.SubFoodSafetySecondStandardAdapter;
import com.safefoodmitra.safefoodmitra.Adapter.SubFoodSafetyStandardAdapter;
import com.safefoodmitra.safefoodmitra.Adapter.SubFoodSafetyThirdStandardAdapter;
import com.safefoodmitra.safefoodmitra.Apis.Apis;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.FoodSafetyModel;
import com.safefoodmitra.safefoodmitra.Modals.SubFoodDocspdfModel;
import com.safefoodmitra.safefoodmitra.Modals.SubFoodSafetyModel;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.ActivitySubfoodsafetystandardBinding;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.safefoodmitra.safefoodmitra.Apis.Apis.foodsafetylist;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.foodsafetypdflist;

public class SubfoodsafetystandardActivity extends AppCompatActivity implements View.OnClickListener {
    ActivitySubfoodsafetystandardBinding subfoodsafetystandardBinding;
    List<SubFoodSafetyModel> subFoodSafetyModels;
    SubFoodSafetyStandardAdapter subFoodSafetyStandardAdapter;
    SubFoodSafetySecondStandardAdapter subFoodSafetySecondStandardAdapter;
    SubFoodSafetyThirdStandardAdapter subFoodSafetyThirdStandardAdapter;
    SubFoodSafetyFourthStandardAdapter subFoodSafetyFourthStandardAdapter;
    SubFoodSafetyFifthStandardAdapter subFoodSafetyFifthStandardAdapter;
    FoodSafetyModel foodSafetyModel;
    SubFoodDocpdfAdapter subFoodDocpdfAdapter;
    List<SubFoodDocspdfModel> subFoodDocspdfModels;
    YouTubePlayerView youTubePlayerView;
    String id,pdfid,catid,catid3,catid4,catid5;
    ImageView cancle;
    private int pos = 0;
    Animation topanim;
    Dialog dialog;
    // public static RecyclerView recyclerViewpdf;
    // sub categry
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subfoodsafetystandardBinding = DataBindingUtil.setContentView(this,R.layout.activity_subfoodsafetystandard);
        if (getIntent()!=null){
            foodSafetyModel= Utlity.gson.fromJson(getIntent().getStringExtra("detail"),FoodSafetyModel.class);
            Utlity.Set_imageGlide(this,foodSafetyModel.getIcon_path(),subfoodsafetystandardBinding.foodicon);
            subfoodsafetystandardBinding.titles.setText(foodSafetyModel.getCat_name());
            id=foodSafetyModel.getId();
        }
        if (Utlity.is_online(this)){
            subFoodData(id);
        }
        else Utlity.show_toast(SubfoodsafetystandardActivity.this,getResources().getString(R.string.nointernet));


        click();
        //   recyclerViewpdf=subfoodsafetystandardBinding.fillelist;
        subfoodsafetystandardBinding.searching.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                subFoodDocpdfAdapter.getFilter().filter(charSequence);
                //  subFoodDocpdfAdapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        subfoodsafetystandardBinding.searching01.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                subFoodSafetyStandardAdapter.getFilter().filter(charSequence);
                //  subFoodDocpdfAdapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        subfoodsafetystandardBinding.searching02.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                subFoodSafetySecondStandardAdapter.getFilter().filter(charSequence);
                //  subFoodDocpdfAdapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        subfoodsafetystandardBinding.searching03.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                subFoodSafetyThirdStandardAdapter.getFilter().filter(charSequence);
                //  subFoodDocpdfAdapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        subfoodsafetystandardBinding.searching04.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                subFoodSafetyFourthStandardAdapter.getFilter().filter(charSequence);
                //  subFoodDocpdfAdapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        subfoodsafetystandardBinding.searching05.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                subFoodSafetyFifthStandardAdapter.getFilter().filter(charSequence);
                //  subFoodDocpdfAdapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void click() {
        subfoodsafetystandardBinding.back.setOnClickListener(this);
        subfoodsafetystandardBinding.backward.setOnClickListener(this);
        subfoodsafetystandardBinding.forward.setOnClickListener(this);
        subfoodsafetystandardBinding.backward2.setOnClickListener(this);
        subfoodsafetystandardBinding.forward2.setOnClickListener(this);
        subfoodsafetystandardBinding.imgfilter.setOnClickListener(this);
        //subfoodsafetystandardBinding.linear.setOnClickListener(this);
        subfoodsafetystandardBinding.searclayout.setOnClickListener(this);
        subfoodsafetystandardBinding.searchunit.setOnClickListener(this);
        subfoodsafetystandardBinding.cancle.setOnClickListener(this);
        //subfoodsafetystanderd
        subfoodsafetystandardBinding.searclayout01.setOnClickListener(this);
        subfoodsafetystandardBinding.searchunit01.setOnClickListener(this);
        subfoodsafetystandardBinding.cancle01.setOnClickListener(this);
        //subfoodsafetystanderd
        subfoodsafetystandardBinding.searclayout02.setOnClickListener(this);
        subfoodsafetystandardBinding.searchunit02.setOnClickListener(this);
        subfoodsafetystandardBinding.cancle02.setOnClickListener(this);
        //subfoodsafetystanderd
        subfoodsafetystandardBinding.searclayout03.setOnClickListener(this);
        subfoodsafetystandardBinding.searchunit03.setOnClickListener(this);
        subfoodsafetystandardBinding.cancle03.setOnClickListener(this);
        //subfoodsafetystanderd
        subfoodsafetystandardBinding.searclayout04.setOnClickListener(this);
        subfoodsafetystandardBinding.searchunit04.setOnClickListener(this);
        subfoodsafetystandardBinding.cancle04.setOnClickListener(this);
        //subfoodsafetystanderd
        subfoodsafetystandardBinding.searclayout05.setOnClickListener(this);
        subfoodsafetystandardBinding.searchunit05.setOnClickListener(this);
        subfoodsafetystandardBinding.cancle05.setOnClickListener(this);
    }


    private int getCurrentItem(){
        return ((LinearLayoutManager)subfoodsafetystandardBinding.dashlist.getLayoutManager())
                .findFirstVisibleItemPosition();
    }
    private void setCurrentItem(int position, boolean smooth){
        if (smooth)
            subfoodsafetystandardBinding.dashlist.scrollToPosition(position);
        else
            subfoodsafetystandardBinding.dashlist.scrollToPosition(position);
    }
    private int getCurrentSubItem(){
        return ((LinearLayoutManager)subfoodsafetystandardBinding.sublist.getLayoutManager())
                .findFirstVisibleItemPosition();
    }
    private void setCurrentSubItem(int position, boolean smooth){
        if (smooth)
            subfoodsafetystandardBinding.sublist.scrollToPosition(position);
        else
            subfoodsafetystandardBinding.sublist.scrollToPosition(position);
    }
    private int getCurrentSubItem3(){
        return ((LinearLayoutManager)subfoodsafetystandardBinding.sublist3.getLayoutManager())
                .findFirstVisibleItemPosition();
    }
    private void setCurrentSubItem3(int position, boolean smooth){
        if (smooth)
            subfoodsafetystandardBinding.sublist3.scrollToPosition(position);
        else
            subfoodsafetystandardBinding.sublist3.scrollToPosition(position);
    }
    private int getCurrentSubItem4(){
        return ((LinearLayoutManager)subfoodsafetystandardBinding.sublist4.getLayoutManager())
                .findFirstVisibleItemPosition();
    }
    private void setCurrentSubItem4(int position, boolean smooth){
        if (smooth)
            subfoodsafetystandardBinding.sublist4.scrollToPosition(position);
        else
            subfoodsafetystandardBinding.sublist4.scrollToPosition(position);
    }
    private int getCurrentSubItem5(){
        return ((LinearLayoutManager)subfoodsafetystandardBinding.sublist5.getLayoutManager())
                .findFirstVisibleItemPosition();
    }
    private void setCurrentSubItem5(int position, boolean smooth){
        if (smooth)
            subfoodsafetystandardBinding.sublist5.scrollToPosition(position);
        else
            subfoodsafetystandardBinding.sublist5.scrollToPosition(position);
    }
    @SuppressLint("ResourceType")
    @Override
    public void onClick(View v) {
        topanim= AnimationUtils.loadAnimation(this,R.xml.top_animation);
        if (v.getId()==R.id.back){
            if (Utlity.get_user(this).getUserroles_id().equalsIgnoreCase("2")){
                startActivity(new Intent(this,AdminMainActivity.class));
                finishAffinity();
            }
            else if (Utlity.get_user(this).getUserroles_id().equalsIgnoreCase("3")){
                startActivity(new Intent(this,UserMainActivity.class));
                finishAffinity();
            }
        }else if (v.getId()==R.id.forward){
            RecyclerView.Adapter adapter = subfoodsafetystandardBinding.dashlist.getAdapter();
            if (adapter==null){
                return;
            }
            int position = getCurrentItem();
            int count = adapter.getItemCount();
            if (position <(count-1)){
                position=position+4;
                setCurrentItem(position, true);
                // Utlity.show_toast(SubfoodsafetystandardActivity.this,String.valueOf(position));
            }

        }else if (v.getId()==R.id.backward){
            int position = getCurrentItem();
            // Utlity.show_toast(SubfoodsafetystandardActivity.this,String.valueOf(position));
            if (position > 0){
                setCurrentItem(position-1, true);
            }
            //Utlity.show_toast(SubfoodsafetystandardActivity.this, String.valueOf(position));
        }else if (v.getId()==R.id.backward2){
            int position = getCurrentSubItem();
            Utlity.show_toast(SubfoodsafetystandardActivity.this,String.valueOf(position));
            if (position > 0){
                setCurrentSubItem(position-1, true);
            }
        }else if (v.getId()==R.id.forward2){
            RecyclerView.Adapter adapter = subfoodsafetystandardBinding.sublist.getAdapter();
            if (adapter==null){
                return;
            }
            int position = getCurrentSubItem();
            int count = adapter.getItemCount();
            if (position <(count-1)) {
                position = position+4;
                setCurrentSubItem(position, true);
                Utlity.show_toast(SubfoodsafetystandardActivity.this,String.valueOf(position));
            }
        }else if (v.getId()==R.id.backward3){
            int position = getCurrentSubItem3();
            Utlity.show_toast(SubfoodsafetystandardActivity.this,String.valueOf(position));
            if (position > 0){
                setCurrentSubItem3(position-1, true);
            }
        }else if (v.getId()==R.id.forward3){
            RecyclerView.Adapter adapter = subfoodsafetystandardBinding.sublist3.getAdapter();
            if (adapter==null){
                return;
            }
            int position = getCurrentSubItem3();
            int count = adapter.getItemCount();
            if (position <(count-1)) {
                position = position+4;
                setCurrentSubItem3(position, true);
                Utlity.show_toast(SubfoodsafetystandardActivity.this,String.valueOf(position));
            }
        }else if (v.getId()==R.id.backward4){
            int position = getCurrentSubItem4();
            Utlity.show_toast(SubfoodsafetystandardActivity.this,String.valueOf(position));
            if (position > 0){
                setCurrentSubItem4(position-1, true);
            }
        }else if (v.getId()==R.id.forward4){
            RecyclerView.Adapter adapter = subfoodsafetystandardBinding.sublist4.getAdapter();
            if (adapter==null){
                return;
            }
            int position = getCurrentSubItem4();
            int count = adapter.getItemCount();
            if (position <(count-1)) {
                position = position+4;
                setCurrentSubItem4(position, true);
                Utlity.show_toast(SubfoodsafetystandardActivity.this,String.valueOf(position));
            }
        }else if (v.getId()==R.id.backward5){
            int position = getCurrentSubItem5();
            Utlity.show_toast(SubfoodsafetystandardActivity.this,String.valueOf(position));
            if (position > 0){
                setCurrentSubItem5(position-1, true);
            }
        }else if (v.getId()==R.id.forward5){
            RecyclerView.Adapter adapter = subfoodsafetystandardBinding.sublist5.getAdapter();
            if (adapter==null){
                return;
            }
            int position = getCurrentSubItem5();
            int count = adapter.getItemCount();
            if (position <(count-1)) {
                position = position+4;
                setCurrentSubItem5(position, true);
                Utlity.show_toast(SubfoodsafetystandardActivity.this,String.valueOf(position));
            }
        }else if (v.getId()==R.id.imgfilter){
            //startActivity(new Intent(this,AdvanceFilterActivity.class));

        }else  if (v.getId()==R.id.searchunit){
            subfoodsafetystandardBinding.searclayout.setAnimation(topanim);
            subfoodsafetystandardBinding.searclayout.setVisibility(View.VISIBLE);
            subfoodsafetystandardBinding.searchunit.setVisibility(View.GONE);
            subfoodsafetystandardBinding.text.setVisibility(View.GONE);
            subfoodsafetystandardBinding.searching.getText().clear();
        }else if (v.getId()==R.id.cancle){
            subfoodsafetystandardBinding.searclayout.setVisibility(View.GONE);
            subfoodsafetystandardBinding.searchunit.setVisibility(View.VISIBLE);
            subfoodsafetystandardBinding.text.setVisibility(View.VISIBLE);
            subCategreypdffile(pdfid);
            subfoodsafetystandardBinding.searching.getText().clear();
        }
        else  if (v.getId()==R.id.searchunit01){
            subfoodsafetystandardBinding.searclayout01.setAnimation(topanim);
            subfoodsafetystandardBinding.searclayout01.setVisibility(View.VISIBLE);
            subfoodsafetystandardBinding.searchunit01.setVisibility(View.GONE);
            subfoodsafetystandardBinding.text01.setVisibility(View.GONE);
            subfoodsafetystandardBinding.searching01.getText().clear();
        }else if (v.getId()==R.id.cancle01){
            subfoodsafetystandardBinding.searclayout01.setVisibility(View.GONE);
            subfoodsafetystandardBinding.searchunit01.setVisibility(View.VISIBLE);
            subfoodsafetystandardBinding.text01.setVisibility(View.VISIBLE);
            subFoodData(id);
            subfoodsafetystandardBinding.searching01.getText().clear();
        }
        else  if (v.getId()==R.id.searchunit02){
            subfoodsafetystandardBinding.searclayout02.setAnimation(topanim);
            subfoodsafetystandardBinding.searclayout02.setVisibility(View.VISIBLE);
            subfoodsafetystandardBinding.searchunit02.setVisibility(View.GONE);
            subfoodsafetystandardBinding.text02.setVisibility(View.GONE);
            subfoodsafetystandardBinding.searching02.getText().clear();
        }else if (v.getId()==R.id.cancle02){
            subfoodsafetystandardBinding.searclayout02.setVisibility(View.GONE);
            subfoodsafetystandardBinding.searchunit02.setVisibility(View.VISIBLE);
            subfoodsafetystandardBinding.text02.setVisibility(View.VISIBLE);
            subCatedata(catid);
            subfoodsafetystandardBinding.searching02.getText().clear();
        }
        else  if (v.getId()==R.id.searchunit03){
            subfoodsafetystandardBinding.searclayout03.setAnimation(topanim);
            subfoodsafetystandardBinding.searclayout03.setVisibility(View.VISIBLE);
            subfoodsafetystandardBinding.searchunit03.setVisibility(View.GONE);
            subfoodsafetystandardBinding.text03.setVisibility(View.GONE);
            subfoodsafetystandardBinding.searching03.getText().clear();
        }else if (v.getId()==R.id.cancle03){
            subfoodsafetystandardBinding.searclayout03.setVisibility(View.GONE);
            subfoodsafetystandardBinding.searchunit03.setVisibility(View.VISIBLE);
            subfoodsafetystandardBinding.text03.setVisibility(View.VISIBLE);
            subCatedata3(catid3);
            subfoodsafetystandardBinding.searching03.getText().clear();
        }
        else  if (v.getId()==R.id.searchunit04){
            subfoodsafetystandardBinding.searclayout04.setAnimation(topanim);
            subfoodsafetystandardBinding.searclayout04.setVisibility(View.VISIBLE);
            subfoodsafetystandardBinding.searchunit04.setVisibility(View.GONE);
            subfoodsafetystandardBinding.text04.setVisibility(View.GONE);
            subfoodsafetystandardBinding.searching04.getText().clear();
        }else if (v.getId()==R.id.cancle04){
            subfoodsafetystandardBinding.searclayout04.setVisibility(View.GONE);
            subfoodsafetystandardBinding.searchunit04.setVisibility(View.VISIBLE);
            subfoodsafetystandardBinding.text04.setVisibility(View.VISIBLE);
            subCatedata4(catid4);
            subfoodsafetystandardBinding.searching04.getText().clear();
        }
        else  if (v.getId()==R.id.searchunit05){
            subfoodsafetystandardBinding.searclayout05.setAnimation(topanim);
            subfoodsafetystandardBinding.searclayout05.setVisibility(View.VISIBLE);
            subfoodsafetystandardBinding.searchunit05.setVisibility(View.GONE);
            subfoodsafetystandardBinding.text05.setVisibility(View.GONE);
            subfoodsafetystandardBinding.searching05.getText().clear();
        }else if (v.getId()==R.id.cancle05){
            subfoodsafetystandardBinding.searclayout05.setVisibility(View.GONE);
            subfoodsafetystandardBinding.searchunit05.setVisibility(View.VISIBLE);
            subfoodsafetystandardBinding.text05.setVisibility(View.VISIBLE);
            subCatedata5(catid5);
            subfoodsafetystandardBinding.searching05.getText().clear();
        }
    }

    private void subFoodData(String id) {
        Utlity.show_progress(this);
        //subfoodsafetystandardBinding.progressBar.setVisibility(View.VISIBLE);
        Request result= get(this, foodsafetylist+id);
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Utlity.dismiss_dilog(SubfoodsafetystandardActivity.this);
                // subfoodsafetystandardBinding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Utlity.dismiss_dilog(SubfoodsafetystandardActivity.this);
                            // subfoodsafetystandardBinding.progressBar.setVisibility(View.GONE);
                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            subFoodSafetyModels = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SubFoodSafetyModel>>() {}.getType());
                            if(subFoodSafetyModels.size()!=0) {
                                subfoodsafetystandardBinding.subcatagery.setVisibility(View.VISIBLE);
                                // subfoodsafetystandardBinding.searchunit.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.dashlist.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this,RecyclerView.HORIZONTAL,false));
                                subFoodSafetyStandardAdapter = new SubFoodSafetyStandardAdapter(SubfoodsafetystandardActivity.this, subFoodSafetyModels);
                                subfoodsafetystandardBinding.dashlist.setAdapter(subFoodSafetyStandardAdapter);
                                subfoodsafetystandardBinding.dashlist.setHasFixedSize(true);

                            }
                            else
                            {
                                subfoodsafetystandardBinding.noreords.setVisibility(View.VISIBLE);
                            }

                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }

        });


    }


    public void subCatedata(String id){
        catid=id;
        Utlity.show_progress(this);
        // subfoodsafetystandardBinding.progressBar2.setVisibility(View.VISIBLE);
        Request result= get(this, foodsafetylist+id);
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                //subfoodsafetystandardBinding.progressBar2.setVisibility(View.GONE);
                Utlity.dismiss_dilog(SubfoodsafetystandardActivity.this);
            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // subfoodsafetystandardBinding.progressBar2.setVisibility(View.GONE);
                            Utlity.dismiss_dilog(SubfoodsafetystandardActivity.this);
                            // subfoodsafetystandardBinding.linearadvancefilter.setVisibility(View.VISIBLE);
                          /*  if (id.equalsIgnoreCase("7")){
                                subfoodsafetystandardBinding.linearadvancefilter.setVisibility(View.VISIBLE);
                            }else {
                                subfoodsafetystandardBinding.linearadvancefilter.setVisibility(View.GONE);
                            }*/
                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            subFoodSafetyModels = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SubFoodSafetyModel>>() {}.getType());
                            if(subFoodSafetyModels.size()!=0) {
                                subfoodsafetystandardBinding.subcatagery2.setVisibility(View.VISIBLE);
                                // subfoodsafetystandardBinding.searchunit.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.linear.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.subcatagery3.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.subcatagery4.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.subcatagery5.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.sublist.setHasFixedSize(true);
                                subfoodsafetystandardBinding.sublist.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this,RecyclerView.HORIZONTAL,false));
                                subFoodSafetySecondStandardAdapter = new SubFoodSafetySecondStandardAdapter(SubfoodsafetystandardActivity.this, subFoodSafetyModels);
                                subfoodsafetystandardBinding.sublist.setAdapter(subFoodSafetySecondStandardAdapter);
                            }else {
                                subfoodsafetystandardBinding.subcatagery2.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.subcatagery3.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.subcatagery4.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.subcatagery5.setVisibility(View.GONE);
                                //subfoodsafetystandardBinding.searchunit.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.linear.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.sublist.setHasFixedSize(true);
                                subfoodsafetystandardBinding.sublist.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this,RecyclerView.HORIZONTAL,false));
                                subFoodSafetySecondStandardAdapter = new SubFoodSafetySecondStandardAdapter(SubfoodsafetystandardActivity.this, subFoodSafetyModels);
                                subfoodsafetystandardBinding.sublist.setAdapter(subFoodSafetySecondStandardAdapter);
                            }
                           /* else
                            {
                               //  subfoodsafetystandardBinding.noreords.setVisibility(View.VISIBLE);
                            }*/
                            // subCategreypdffile(id);
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }

        });

    }
    public void subCatedataduble(String id){
        pdfid=id;
        Utlity.show_progress(this);
        // subfoodsafetystandardBinding.progressBar2.setVisibility(View.VISIBLE);
        Request result= get(this, foodsafetylist+id);
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                //subfoodsafetystandardBinding.progressBar2.setVisibility(View.GONE);
                Utlity.dismiss_dilog(SubfoodsafetystandardActivity.this);
            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // subfoodsafetystandardBinding.progressBar2.setVisibility(View.GONE);
                            Utlity.dismiss_dilog(SubfoodsafetystandardActivity.this);
                          /*  if (id.equalsIgnoreCase("7")){
                                subfoodsafetystandardBinding.linearadvancefilter.setVisibility(View.VISIBLE);
                            }else {
                                subfoodsafetystandardBinding.linearadvancefilter.setVisibility(View.GONE);
                            }*/
                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            subFoodSafetyModels = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SubFoodSafetyModel>>() {}.getType());
                            if(subFoodSafetyModels.size()!=0) {
                                subfoodsafetystandardBinding.subcatagery2.setVisibility(View.VISIBLE);
                                // subfoodsafetystandardBinding.searchunit.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.linear.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.subcatagery3.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.subcatagery4.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.subcatagery5.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.sublist.setHasFixedSize(true);
                                subfoodsafetystandardBinding.sublist.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this,RecyclerView.HORIZONTAL,false));
                                subFoodSafetySecondStandardAdapter = new SubFoodSafetySecondStandardAdapter(SubfoodsafetystandardActivity.this, subFoodSafetyModels);
                                subfoodsafetystandardBinding.sublist.setAdapter(subFoodSafetySecondStandardAdapter);
                            }else {
                                subfoodsafetystandardBinding.subcatagery2.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.subcatagery3.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.subcatagery4.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.subcatagery5.setVisibility(View.GONE);
                                //subfoodsafetystandardBinding.searchunit.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.linear.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.sublist.setHasFixedSize(true);
                                subfoodsafetystandardBinding.sublist.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this,RecyclerView.HORIZONTAL,false));
                                subFoodSafetySecondStandardAdapter = new SubFoodSafetySecondStandardAdapter(SubfoodsafetystandardActivity.this, subFoodSafetyModels);
                                subfoodsafetystandardBinding.sublist.setAdapter(subFoodSafetySecondStandardAdapter);
                            }
                           /* else
                            {
                               //  subfoodsafetystandardBinding.noreords.setVisibility(View.VISIBLE);
                            }*/
                            // subCategreypdffile(id);
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }

        });
        Request result1= get(this, foodsafetypdflist+id);
        OkHttpClient okHttpClient1= new OkHttpClient();
        okHttpClient1.newCall(result1).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
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
                            subFoodDocspdfModels = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SubFoodDocspdfModel>>() {}.getType());
                            if(subFoodDocspdfModels.size()!=0) {
                                subfoodsafetystandardBinding.linear.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.fillelist.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.fillelist.setHasFixedSize(true);
                                subfoodsafetystandardBinding.fillelist.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this));
                                subFoodDocpdfAdapter = new SubFoodDocpdfAdapter(SubfoodsafetystandardActivity.this,subFoodDocspdfModels);
                                subfoodsafetystandardBinding.fillelist.setAdapter(subFoodDocpdfAdapter);
                            }else {
                                subfoodsafetystandardBinding.fillelist.setHasFixedSize(true);
                                subfoodsafetystandardBinding.fillelist.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this));
                                subFoodDocpdfAdapter = new SubFoodDocpdfAdapter(SubfoodsafetystandardActivity.this,subFoodDocspdfModels);
                                subfoodsafetystandardBinding.fillelist.setAdapter(subFoodDocpdfAdapter);
                            }


                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }

        });

    }
    public void subcatepdffile2(String id){
        pdfid=id;
        Utlity.show_progress(this);
        Request result1= get(this, foodsafetypdflist+id);
        OkHttpClient okHttpClient1= new OkHttpClient();
        okHttpClient1.newCall(result1).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Utlity.dismiss_dilog(SubfoodsafetystandardActivity.this);
            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Utlity.dismiss_dilog(SubfoodsafetystandardActivity.this);

                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            subFoodDocspdfModels = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SubFoodDocspdfModel>>() {}.getType());
                            if(subFoodDocspdfModels.size()!=0) {
                                subfoodsafetystandardBinding.linear.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.fillelist.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery2.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.subcatagery3.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.subcatagery4.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.subcatagery5.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.fillelist.setHasFixedSize(true);
                                subfoodsafetystandardBinding.fillelist.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this));
                                subFoodDocpdfAdapter = new SubFoodDocpdfAdapter(SubfoodsafetystandardActivity.this,subFoodDocspdfModels);
                                subfoodsafetystandardBinding.fillelist.setAdapter(subFoodDocpdfAdapter);
                            }else {
                                subfoodsafetystandardBinding.fillelist.setHasFixedSize(true);
                                subfoodsafetystandardBinding.fillelist.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this));
                                subFoodDocpdfAdapter = new SubFoodDocpdfAdapter(SubfoodsafetystandardActivity.this,subFoodDocspdfModels);
                                subfoodsafetystandardBinding.fillelist.setAdapter(subFoodDocpdfAdapter);
                            }


                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }

        });
    }
    public void subCatedata3(String id){
        Utlity.show_progress(this);
        catid3=id;
        //subfoodsafetystandardBinding.progressBar3.setVisibility(View.VISIBLE);
        Request result= get(this, foodsafetylist+id);
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                //subfoodsafetystandardBinding.progressBar3.setVisibility(View.GONE);
                Utlity.dismiss_dilog(SubfoodsafetystandardActivity.this);
            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // subfoodsafetystandardBinding.progressBar3.setVisibility(View.GONE);
                            Utlity.dismiss_dilog(SubfoodsafetystandardActivity.this);
                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            subFoodSafetyModels = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SubFoodSafetyModel>>() {}.getType());
                            if(subFoodSafetyModels.size()!=0) {
                                subfoodsafetystandardBinding.subcatagery3.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery2.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery4.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.subcatagery5.setVisibility(View.GONE);
                                // subfoodsafetystandardBinding.searchunit.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.linear.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.sublist3.setHasFixedSize(true);
                                subfoodsafetystandardBinding.sublist3.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this,RecyclerView.HORIZONTAL,false));
                                subFoodSafetyThirdStandardAdapter = new SubFoodSafetyThirdStandardAdapter(SubfoodsafetystandardActivity.this, subFoodSafetyModels);
                                subfoodsafetystandardBinding.sublist3.setAdapter(subFoodSafetyThirdStandardAdapter);
                            }else {
                                subfoodsafetystandardBinding.subcatagery3.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.subcatagery2.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery4.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.subcatagery5.setVisibility(View.GONE);
                                // subfoodsafetystandardBinding.searchunit.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.linear.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.sublist3.setHasFixedSize(true);
                                subfoodsafetystandardBinding.sublist3.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this,RecyclerView.HORIZONTAL,false));
                                subFoodSafetyThirdStandardAdapter = new SubFoodSafetyThirdStandardAdapter(SubfoodsafetystandardActivity.this, subFoodSafetyModels);
                                subfoodsafetystandardBinding.sublist3.setAdapter(subFoodSafetyThirdStandardAdapter);
                            }

                            //subCategreypdffile(id);
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }

        });

    }
    public void subCatedata3suble(String id){
        pdfid=id;
        Utlity.show_progress(this);
        //subfoodsafetystandardBinding.progressBar3.setVisibility(View.VISIBLE);
        Request result= get(this, foodsafetylist+id);
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                //subfoodsafetystandardBinding.progressBar3.setVisibility(View.GONE);
                Utlity.dismiss_dilog(SubfoodsafetystandardActivity.this);
            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // subfoodsafetystandardBinding.progressBar3.setVisibility(View.GONE);
                            Utlity.dismiss_dilog(SubfoodsafetystandardActivity.this);
                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            subFoodSafetyModels = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SubFoodSafetyModel>>() {}.getType());
                            if(subFoodSafetyModels.size()!=0) {
                                subfoodsafetystandardBinding.subcatagery3.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery2.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery4.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.subcatagery5.setVisibility(View.GONE);
                                // subfoodsafetystandardBinding.searchunit.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.linear.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.sublist3.setHasFixedSize(true);
                                subfoodsafetystandardBinding.sublist3.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this,RecyclerView.HORIZONTAL,false));
                                subFoodSafetyThirdStandardAdapter = new SubFoodSafetyThirdStandardAdapter(SubfoodsafetystandardActivity.this, subFoodSafetyModels);
                                subfoodsafetystandardBinding.sublist3.setAdapter(subFoodSafetyThirdStandardAdapter);
                            }else {
                                subfoodsafetystandardBinding.subcatagery3.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.subcatagery2.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery4.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.subcatagery5.setVisibility(View.GONE);
                                // subfoodsafetystandardBinding.searchunit.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.linear.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.sublist3.setHasFixedSize(true);
                                subfoodsafetystandardBinding.sublist3.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this,RecyclerView.HORIZONTAL,false));
                                subFoodSafetyThirdStandardAdapter = new SubFoodSafetyThirdStandardAdapter(SubfoodsafetystandardActivity.this, subFoodSafetyModels);
                                subfoodsafetystandardBinding.sublist3.setAdapter(subFoodSafetyThirdStandardAdapter);
                            }

                            //subCategreypdffile(id);
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }

        });

        Request result1= get(this, foodsafetypdflist+id);
        OkHttpClient okHttpClient1= new OkHttpClient();
        okHttpClient1.newCall(result1).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
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
                            subFoodDocspdfModels = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SubFoodDocspdfModel>>() {}.getType());
                            if(subFoodDocspdfModels.size()!=0) {
                                subfoodsafetystandardBinding.linear.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.fillelist.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.fillelist.setHasFixedSize(true);
                                subfoodsafetystandardBinding.fillelist.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this));
                                subFoodDocpdfAdapter = new SubFoodDocpdfAdapter(SubfoodsafetystandardActivity.this,subFoodDocspdfModels);
                                subfoodsafetystandardBinding.fillelist.setAdapter(subFoodDocpdfAdapter);
                            }else {
                                subfoodsafetystandardBinding.fillelist.setHasFixedSize(true);
                                subfoodsafetystandardBinding.fillelist.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this));
                                subFoodDocpdfAdapter = new SubFoodDocpdfAdapter(SubfoodsafetystandardActivity.this,subFoodDocspdfModels);
                                subfoodsafetystandardBinding.fillelist.setAdapter(subFoodDocpdfAdapter);
                            }


                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }

        });
    }
    public void subcatepdffile3(String id){
        pdfid=id;
        Utlity.show_progress(this);
        Request result1= get(this, foodsafetypdflist+id);
        OkHttpClient okHttpClient1= new OkHttpClient();
        okHttpClient1.newCall(result1).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Utlity.dismiss_dilog(SubfoodsafetystandardActivity.this);
            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Utlity.dismiss_dilog(SubfoodsafetystandardActivity.this);

                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            subFoodDocspdfModels = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SubFoodDocspdfModel>>() {}.getType());
                            if(subFoodDocspdfModels.size()!=0) {
                                subfoodsafetystandardBinding.linear.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.fillelist.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery2.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery3.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.subcatagery4.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.subcatagery5.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.fillelist.setHasFixedSize(true);
                                subfoodsafetystandardBinding.fillelist.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this));
                                subFoodDocpdfAdapter = new SubFoodDocpdfAdapter(SubfoodsafetystandardActivity.this,subFoodDocspdfModels);
                                subfoodsafetystandardBinding.fillelist.setAdapter(subFoodDocpdfAdapter);
                            }else {
                                subfoodsafetystandardBinding.fillelist.setHasFixedSize(true);
                                subfoodsafetystandardBinding.fillelist.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this));
                                subFoodDocpdfAdapter = new SubFoodDocpdfAdapter(SubfoodsafetystandardActivity.this,subFoodDocspdfModels);
                                subfoodsafetystandardBinding.fillelist.setAdapter(subFoodDocpdfAdapter);
                            }


                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }

        });
    }
    public void subCatedata4(String id){
        catid4=id;
        // subfoodsafetystandardBinding.progressBar4.setVisibility(View.VISIBLE);
        Utlity.show_progress(this);
        Request result= get(this, foodsafetylist+id);
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                // subfoodsafetystandardBinding.progressBar4.setVisibility(View.GONE);
                Utlity.dismiss_dilog(SubfoodsafetystandardActivity.this);
            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // subfoodsafetystandardBinding.progressBar4.setVisibility(View.GONE);
                            Utlity.dismiss_dilog(SubfoodsafetystandardActivity.this);
                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            subFoodSafetyModels = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SubFoodSafetyModel>>() {}.getType());
                            if(subFoodSafetyModels.size()!=0) {
                                subfoodsafetystandardBinding.subcatagery4.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery3.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery2.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery5.setVisibility(View.GONE);
                                //subfoodsafetystandardBinding.searchunit.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.linear.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.sublist4.setHasFixedSize(true);
                                subfoodsafetystandardBinding.sublist4.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this,RecyclerView.HORIZONTAL,false));
                                subFoodSafetyFourthStandardAdapter = new SubFoodSafetyFourthStandardAdapter(SubfoodsafetystandardActivity.this, subFoodSafetyModels);
                                subfoodsafetystandardBinding.sublist4.setAdapter(subFoodSafetyFourthStandardAdapter);
                            }else {
                                subfoodsafetystandardBinding.subcatagery4.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.subcatagery3.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery2.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery5.setVisibility(View.GONE);
                                // subfoodsafetystandardBinding.searchunit.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.linear.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.sublist4.setHasFixedSize(true);
                                subfoodsafetystandardBinding.sublist4.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this,RecyclerView.HORIZONTAL,false));
                                subFoodSafetyFourthStandardAdapter = new SubFoodSafetyFourthStandardAdapter(SubfoodsafetystandardActivity.this, subFoodSafetyModels);
                                subfoodsafetystandardBinding.sublist4.setAdapter(subFoodSafetyFourthStandardAdapter);
                            }

                            //subCategreypdffile(id);
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }

        });

    }
    public void subCatedata4duble(String id){
        pdfid=id;
        // subfoodsafetystandardBinding.progressBar4.setVisibility(View.VISIBLE);
        Utlity.show_progress(this);
        Request result= get(this, foodsafetylist+id);
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                // subfoodsafetystandardBinding.progressBar4.setVisibility(View.GONE);
                Utlity.dismiss_dilog(SubfoodsafetystandardActivity.this);
            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // subfoodsafetystandardBinding.progressBar4.setVisibility(View.GONE);
                            Utlity.dismiss_dilog(SubfoodsafetystandardActivity.this);
                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            subFoodSafetyModels = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SubFoodSafetyModel>>() {}.getType());
                            if(subFoodSafetyModels.size()!=0) {
                                subfoodsafetystandardBinding.subcatagery4.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery3.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery2.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery5.setVisibility(View.GONE);
                                //subfoodsafetystandardBinding.searchunit.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.linear.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.sublist4.setHasFixedSize(true);
                                subfoodsafetystandardBinding.sublist4.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this,RecyclerView.HORIZONTAL,false));
                                subFoodSafetyFourthStandardAdapter = new SubFoodSafetyFourthStandardAdapter(SubfoodsafetystandardActivity.this, subFoodSafetyModels);
                                subfoodsafetystandardBinding.sublist4.setAdapter(subFoodSafetyFourthStandardAdapter);
                            }else {
                                subfoodsafetystandardBinding.subcatagery4.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.subcatagery3.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery2.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery5.setVisibility(View.GONE);
                                // subfoodsafetystandardBinding.searchunit.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.linear.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.sublist4.setHasFixedSize(true);
                                subfoodsafetystandardBinding.sublist4.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this,RecyclerView.HORIZONTAL,false));
                                subFoodSafetyFourthStandardAdapter = new SubFoodSafetyFourthStandardAdapter(SubfoodsafetystandardActivity.this, subFoodSafetyModels);
                                subfoodsafetystandardBinding.sublist4.setAdapter(subFoodSafetyFourthStandardAdapter);
                            }

                            //subCategreypdffile(id);
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }

        });

        Request result1= get(this, foodsafetypdflist+id);
        OkHttpClient okHttpClient1= new OkHttpClient();
        okHttpClient1.newCall(result1).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

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
                            subFoodDocspdfModels = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SubFoodDocspdfModel>>() {}.getType());
                            if(subFoodDocspdfModels.size()!=0) {
                                subfoodsafetystandardBinding.linear.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.fillelist.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.fillelist.setHasFixedSize(true);
                                subfoodsafetystandardBinding.fillelist.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this));
                                subFoodDocpdfAdapter = new SubFoodDocpdfAdapter(SubfoodsafetystandardActivity.this,subFoodDocspdfModels);
                                subfoodsafetystandardBinding.fillelist.setAdapter(subFoodDocpdfAdapter);
                            }else {
                                subfoodsafetystandardBinding.fillelist.setHasFixedSize(true);
                                subfoodsafetystandardBinding.fillelist.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this));
                                subFoodDocpdfAdapter = new SubFoodDocpdfAdapter(SubfoodsafetystandardActivity.this,subFoodDocspdfModels);
                                subfoodsafetystandardBinding.fillelist.setAdapter(subFoodDocpdfAdapter);
                            }


                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }

        });
    }
    public void subcatepdffile4(String id){
        pdfid=id;
        Utlity.show_progress(this);
        Request result1= get(this, foodsafetypdflist+id);
        OkHttpClient okHttpClient1= new OkHttpClient();
        okHttpClient1.newCall(result1).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Utlity.dismiss_dilog(SubfoodsafetystandardActivity.this);
            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Utlity.dismiss_dilog(SubfoodsafetystandardActivity.this);

                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            subFoodDocspdfModels = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SubFoodDocspdfModel>>() {}.getType());
                            if(subFoodDocspdfModels.size()!=0) {
                                subfoodsafetystandardBinding.linear.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.fillelist.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery2.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery3.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery4.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.subcatagery5.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.fillelist.setHasFixedSize(true);
                                subfoodsafetystandardBinding.fillelist.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this));
                                subFoodDocpdfAdapter = new SubFoodDocpdfAdapter(SubfoodsafetystandardActivity.this,subFoodDocspdfModels);
                                subfoodsafetystandardBinding.fillelist.setAdapter(subFoodDocpdfAdapter);
                            }else {
                                subfoodsafetystandardBinding.fillelist.setHasFixedSize(true);
                                subfoodsafetystandardBinding.fillelist.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this));
                                subFoodDocpdfAdapter = new SubFoodDocpdfAdapter(SubfoodsafetystandardActivity.this,subFoodDocspdfModels);
                                subfoodsafetystandardBinding.fillelist.setAdapter(subFoodDocpdfAdapter);
                            }


                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }

        });
    }
    public void subCatedata5(String id){
        catid5=id;
        //subfoodsafetystandardBinding.progressBar5.setVisibility(View.VISIBLE);
        Utlity.show_progress(this);
        Request result= get(this, foodsafetylist+id);
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                //subfoodsafetystandardBinding.progressBar5.setVisibility(View.GONE);
                Utlity.dismiss_dilog(SubfoodsafetystandardActivity.this);
            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // subfoodsafetystandardBinding.progressBar5.setVisibility(View.GONE);
                            Utlity.dismiss_dilog(SubfoodsafetystandardActivity.this);
                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            subFoodSafetyModels = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SubFoodSafetyModel>>() {}.getType());
                            if(subFoodSafetyModels.size()!=0) {
                                subfoodsafetystandardBinding.subcatagery5.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery3.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery4.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery2.setVisibility(View.VISIBLE);
                                // subfoodsafetystandardBinding.searchunit.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.linear.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.sublist5.setHasFixedSize(true);
                                subfoodsafetystandardBinding.sublist5.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this,RecyclerView.HORIZONTAL,false));
                                subFoodSafetyFifthStandardAdapter = new SubFoodSafetyFifthStandardAdapter(SubfoodsafetystandardActivity.this, subFoodSafetyModels);
                                subfoodsafetystandardBinding.sublist5.setAdapter(subFoodSafetyFifthStandardAdapter);
                            }else {
                                subfoodsafetystandardBinding.subcatagery5.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.subcatagery3.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery4.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery2.setVisibility(View.VISIBLE);
                                //subfoodsafetystandardBinding.searchunit.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.linear.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.sublist5.setHasFixedSize(true);
                                subfoodsafetystandardBinding.sublist5.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this,RecyclerView.HORIZONTAL,false));
                                subFoodSafetyFifthStandardAdapter = new SubFoodSafetyFifthStandardAdapter(SubfoodsafetystandardActivity.this, subFoodSafetyModels);
                                subfoodsafetystandardBinding.sublist5.setAdapter(subFoodSafetyFifthStandardAdapter);
                            }

                            //subCategreypdffile(id);
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }

        });

    }
    public void subCatedata5duble(String id){
        pdfid=id;
        //subfoodsafetystandardBinding.progressBar5.setVisibility(View.VISIBLE);
        Utlity.show_progress(this);
        Request result= get(this, foodsafetylist+id);
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                //subfoodsafetystandardBinding.progressBar5.setVisibility(View.GONE);
                Utlity.dismiss_dilog(SubfoodsafetystandardActivity.this);
            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // subfoodsafetystandardBinding.progressBar5.setVisibility(View.GONE);
                            Utlity.dismiss_dilog(SubfoodsafetystandardActivity.this);
                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            subFoodSafetyModels = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SubFoodSafetyModel>>() {}.getType());
                            if(subFoodSafetyModels.size()!=0) {
                                subfoodsafetystandardBinding.subcatagery5.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery3.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery4.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery2.setVisibility(View.VISIBLE);
                                // subfoodsafetystandardBinding.searchunit.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.linear.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.sublist5.setHasFixedSize(true);
                                subfoodsafetystandardBinding.sublist5.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this,RecyclerView.HORIZONTAL,false));
                                subFoodSafetyFifthStandardAdapter = new SubFoodSafetyFifthStandardAdapter(SubfoodsafetystandardActivity.this, subFoodSafetyModels);
                                subfoodsafetystandardBinding.sublist5.setAdapter(subFoodSafetyFifthStandardAdapter);
                            }else {
                                subfoodsafetystandardBinding.subcatagery5.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.subcatagery3.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery4.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery2.setVisibility(View.VISIBLE);
                                //subfoodsafetystandardBinding.searchunit.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.linear.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.sublist5.setHasFixedSize(true);
                                subfoodsafetystandardBinding.sublist5.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this,RecyclerView.HORIZONTAL,false));
                                subFoodSafetyFifthStandardAdapter = new SubFoodSafetyFifthStandardAdapter(SubfoodsafetystandardActivity.this, subFoodSafetyModels);
                                subfoodsafetystandardBinding.sublist5.setAdapter(subFoodSafetyFifthStandardAdapter);
                            }

                            //subCategreypdffile(id);
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }

        });
        Request result1= get(this, foodsafetypdflist+id);
        OkHttpClient okHttpClient1= new OkHttpClient();
        okHttpClient1.newCall(result1).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

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
                            subFoodDocspdfModels = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SubFoodDocspdfModel>>() {}.getType());
                            if(subFoodDocspdfModels.size()!=0) {
                                subfoodsafetystandardBinding.linear.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.fillelist.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery2.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery3.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery4.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery5.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.fillelist.setHasFixedSize(true);
                                subfoodsafetystandardBinding.fillelist.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this));
                                subFoodDocpdfAdapter = new SubFoodDocpdfAdapter(SubfoodsafetystandardActivity.this,subFoodDocspdfModels);
                                subfoodsafetystandardBinding.fillelist.setAdapter(subFoodDocpdfAdapter);
                            }else {
                                subfoodsafetystandardBinding.fillelist.setHasFixedSize(true);
                                subfoodsafetystandardBinding.fillelist.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this));
                                subFoodDocpdfAdapter = new SubFoodDocpdfAdapter(SubfoodsafetystandardActivity.this,subFoodDocspdfModels);
                                subfoodsafetystandardBinding.fillelist.setAdapter(subFoodDocpdfAdapter);
                            }


                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }

        });
    }
    public void subcatepdffile5(String id){
        pdfid=id;
        Utlity.show_progress(this);
        Request result1= get(this, foodsafetypdflist+id);
        OkHttpClient okHttpClient1= new OkHttpClient();
        okHttpClient1.newCall(result1).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Utlity.dismiss_dilog(SubfoodsafetystandardActivity.this);
            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Utlity.dismiss_dilog(SubfoodsafetystandardActivity.this);

                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            subFoodDocspdfModels = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SubFoodDocspdfModel>>() {}.getType());
                            if(subFoodDocspdfModels.size()!=0) {
                                subfoodsafetystandardBinding.linear.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.fillelist.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery2.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery3.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery4.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.subcatagery5.setVisibility(View.GONE);
                                subfoodsafetystandardBinding.fillelist.setHasFixedSize(true);
                                subfoodsafetystandardBinding.fillelist.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this));
                                subFoodDocpdfAdapter = new SubFoodDocpdfAdapter(SubfoodsafetystandardActivity.this,subFoodDocspdfModels);
                                subfoodsafetystandardBinding.fillelist.setAdapter(subFoodDocpdfAdapter);
                            }else {
                                subfoodsafetystandardBinding.fillelist.setHasFixedSize(true);
                                subfoodsafetystandardBinding.fillelist.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this));
                                subFoodDocpdfAdapter = new SubFoodDocpdfAdapter(SubfoodsafetystandardActivity.this,subFoodDocspdfModels);
                                subfoodsafetystandardBinding.fillelist.setAdapter(subFoodDocpdfAdapter);
                            }


                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }

        });
    }


    public void subcatepdffile(String id){
        pdfid=id;
        Utlity.show_progress(this);
        Request result1= get(this, foodsafetypdflist+id);
        OkHttpClient okHttpClient1= new OkHttpClient();
        okHttpClient1.newCall(result1).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Utlity.dismiss_dilog(SubfoodsafetystandardActivity.this);
            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Utlity.dismiss_dilog(SubfoodsafetystandardActivity.this);

                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            subFoodDocspdfModels = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SubFoodDocspdfModel>>() {}.getType());
                            if(subFoodDocspdfModels.size()!=0) {
                                subfoodsafetystandardBinding.linear.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.fillelist.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.fillelist.setHasFixedSize(true);
                                subfoodsafetystandardBinding.fillelist.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this));
                                subFoodDocpdfAdapter = new SubFoodDocpdfAdapter(SubfoodsafetystandardActivity.this,subFoodDocspdfModels);
                                subfoodsafetystandardBinding.fillelist.setAdapter(subFoodDocpdfAdapter);
                            }else {
                                subfoodsafetystandardBinding.fillelist.setHasFixedSize(true);
                                subfoodsafetystandardBinding.fillelist.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this));
                                subFoodDocpdfAdapter = new SubFoodDocpdfAdapter(SubfoodsafetystandardActivity.this,subFoodDocspdfModels);
                                subfoodsafetystandardBinding.fillelist.setAdapter(subFoodDocpdfAdapter);
                            }


                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }

        });
    }



    public void subCategreypdffile(String id){

        //Utlity.show_progress(this);
        Request result= get(this, foodsafetypdflist+id);
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                //Utlity.dismiss_dilog(SubfoodsafetystandardActivity.this);
            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //Utlity.dismiss_dilog(SubfoodsafetystandardActivity.this);

                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            subFoodDocspdfModels = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SubFoodDocspdfModel>>() {}.getType());
                            if(subFoodDocspdfModels.size()!=0) {
                                subfoodsafetystandardBinding.linear.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.fillelist.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.fillelist.setHasFixedSize(true);
                                subfoodsafetystandardBinding.fillelist.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this));
                                subFoodDocpdfAdapter = new SubFoodDocpdfAdapter(SubfoodsafetystandardActivity.this,subFoodDocspdfModels);
                                subfoodsafetystandardBinding.fillelist.setAdapter(subFoodDocpdfAdapter);
                            }if (subFoodDocspdfModels.size()==0){
                                subfoodsafetystandardBinding.fillelist.setHasFixedSize(true);
                                subfoodsafetystandardBinding.fillelist.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this));
                                subFoodDocpdfAdapter = new SubFoodDocpdfAdapter(SubfoodsafetystandardActivity.this,subFoodDocspdfModels);
                                subfoodsafetystandardBinding.fillelist.setAdapter(subFoodDocpdfAdapter);
                            }
                            else
                            {
                                // subfoodsafetystandardBinding.noreords.setVisibility(View.VISIBLE);
                            }

                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }

        });
    }
    public void Categreypdffile(String id){
        Utlity.show_progress(this);
        Request result= get(this, foodsafetypdflist+id);
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Utlity.dismiss_dilog(SubfoodsafetystandardActivity.this);
            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Utlity.dismiss_dilog(SubfoodsafetystandardActivity.this);

                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            subFoodDocspdfModels = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SubFoodDocspdfModel>>() {}.getType());

                            if(subFoodDocspdfModels.size()!=0) {
                                subfoodsafetystandardBinding.linear.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.fillelist.setVisibility(View.VISIBLE);
                                subfoodsafetystandardBinding.fillelist.setHasFixedSize(true);
                                subfoodsafetystandardBinding.fillelist.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this));
                                subFoodDocpdfAdapter = new SubFoodDocpdfAdapter(SubfoodsafetystandardActivity.this,subFoodDocspdfModels);
                                subfoodsafetystandardBinding.fillelist.setAdapter(subFoodDocpdfAdapter);
                                subfoodsafetystandardBinding.scrollview.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        subfoodsafetystandardBinding.scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                                    }
                                });
                            }if (subFoodDocspdfModels.size()==0){
                                subfoodsafetystandardBinding.fillelist.setHasFixedSize(true);
                                subfoodsafetystandardBinding.fillelist.setLayoutManager(new LinearLayoutManager(SubfoodsafetystandardActivity.this));
                                subFoodDocpdfAdapter = new SubFoodDocpdfAdapter(SubfoodsafetystandardActivity.this,subFoodDocspdfModels);
                                subfoodsafetystandardBinding.fillelist.setAdapter(subFoodDocpdfAdapter);
                            }
                            else
                            {
                                // subfoodsafetystandardBinding.noreords.setVisibility(View.VISIBLE);
                            }

                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }

        });
    }




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
    public void youtubeData(String videoId){
        dialog = new Dialog(SubfoodsafetystandardActivity.this,R.style.full_screen_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.youtube);
        dialog.getWindow().setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
        youTubePlayerView=dialog.findViewById(R.id.youtube_player_view);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NotNull YouTubePlayer youTubePlayer) {
                // String videoId = "https://www.youtube.com/watch?v=hoNb6HuNmU0";
                youTubePlayer.loadVideo(videoId, 0);

                if (!videoId.isEmpty()){
                    try {
                        String[] arrOfStr = videoId.split("=", 2);
                        String part1= arrOfStr[1];
                        // for (String a : arrOfStr)
                        System.out.println(part1);
                        youTubePlayer.loadVideo(part1, 0);
                    }catch (IndexOutOfBoundsException e){
                        e.printStackTrace();
                    }

                }
                else {
                    Toast.makeText(SubfoodsafetystandardActivity.this,"Not good url your video",Toast.LENGTH_LONG).show();
                }
            }
        });

        youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {

            }

            @Override
            public void onYouTubePlayerExitFullScreen() {

            }
        });
        dialog.show();
    }
    /*  @Override
      public void onPause() {
          super.onPause();
          youTubePlayerView.release();

      }

      @Override
      public void onDestroy() {
          youTubePlayerView.release();
          super.onDestroy();

      }*/
   /* private void fillterData(){
        final Dialog dialog = new Dialog(SubfoodsafetystandardActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.filterdata);
        dialog.getWindow().setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
       cancle= dialog.findViewById(R.id.filtercancle);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (dialog!=null){
            dialog.cancel();
        }

    }

}