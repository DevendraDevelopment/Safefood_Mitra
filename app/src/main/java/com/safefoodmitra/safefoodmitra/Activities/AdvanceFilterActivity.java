package com.safefoodmitra.safefoodmitra.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.reflect.TypeToken;
import com.safefoodmitra.safefoodmitra.Adapter.CustomCatsearchSpinnerAdapter;
import com.safefoodmitra.safefoodmitra.Adapter.CustomsearchSpinnerAdapter;
import com.safefoodmitra.safefoodmitra.Adapter.ExpandAdapter;
import com.safefoodmitra.safefoodmitra.Adapter.SearchShowAdapter;
import com.safefoodmitra.safefoodmitra.Apis.ApiClients;
import com.safefoodmitra.safefoodmitra.Apis.Apis;
import com.safefoodmitra.safefoodmitra.Apis.RetApis;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;

import com.safefoodmitra.safefoodmitra.Modals.CategoryModel;
import com.safefoodmitra.safefoodmitra.Modals.InspectrespoModals;
import com.safefoodmitra.safefoodmitra.Modals.InspectrespoModals2;
import com.safefoodmitra.safefoodmitra.Modals.ParameterCategoryModel;
import com.safefoodmitra.safefoodmitra.Modals.Root;
import com.safefoodmitra.safefoodmitra.Modals.SubCategoryModel;
import com.safefoodmitra.safefoodmitra.Modals.SubCategoryModel2;
import com.safefoodmitra.safefoodmitra.Modals.SubCategoryModel3;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.ActivityAdvanceFilterBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.safefoodmitra.safefoodmitra.Adapter.CustomCatsearchSpinnerAdapter.catid;
import static com.safefoodmitra.safefoodmitra.Adapter.CustomsearchSpinnerAdapter.subcatid;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.subcategorylist;

public class AdvanceFilterActivity extends AppCompatActivity implements View.OnClickListener {
   ActivityAdvanceFilterBinding activityAdvanceFilterBinding;
   List<CategoryModel> categoryModels;
   List<SubCategoryModel> subCategoryModels;
   List<SubCategoryModel2> subCategoryModels2;
   List<SubCategoryModel3> subCategoryModels3;
   List<ParameterCategoryModel> parameterCategoryModels;

   List<Root> advanceDataModels;
    RetApis apiInterface;
    ArrayList<String> catlist;
    String cateid,subcateid,subcateid2,subcateid3,subcateid4,keyword,cat1,cat2,cat3,cat4,cat5,search;
    public SearchShowAdapter searchShowAdapter;
    int count=0;
    ExpandAdapter expandAdapter;
    private LinkedHashMap<String, Root> myroot = new LinkedHashMap<String, Root>();
    private ArrayList<Root> roottList = new ArrayList<Root>();
    Animation topanim;
    public static TextView subcatename,catname;
    public static LinearLayout layoutcatfirst;
    public static Dialog dialog,catdialog;
    CustomsearchSpinnerAdapter customsearchSpinnerAdapter;
    CustomCatsearchSpinnerAdapter customCatsearchSpinnerAdapter;
    private static Dialog progressDialog = null;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAdvanceFilterBinding = DataBindingUtil.setContentView(this, R.layout.activity_advance_filter);
        subcatename = activityAdvanceFilterBinding.subcatname;
        catname = activityAdvanceFilterBinding.catname;
        layoutcatfirst = activityAdvanceFilterBinding.catfirst;
        progressDialog = new Dialog(AdvanceFilterActivity.this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        progressDialog.setContentView(R.layout.custom_progress);
        if (Utlity.is_online(this)){
           /* activityAdvanceFilterBinding.swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    searchData();
                    activityAdvanceFilterBinding.swipeToRefresh.setRefreshing(false);
                }
            });*/
        }
        else Utlity.show_toast(AdvanceFilterActivity.this,getResources().getString(R.string.nointernet));
        click();

        activityAdvanceFilterBinding.expanddata.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                setListViewHeight(parent, groupPosition);
                return false;
            }
        });
      /*  activityAdvanceFilterBinding.searching.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //expandAdapter.getFilter().filter(s);

               *//* String searchedVal = s.toString();

                String categoryString = "";
                ArrayList<Root> cacheMenuResList = new ArrayList<>();
                List<Root.ValuesBean> ms = new ArrayList<>();

                if (TextUtils.isEmpty(searchedVal)) {
                    expandAdapter.setCacheMenuRes(roottList);

                } else {
                    for (Root c : roottList) {
                        List<Root.ValuesBean> menuList = c.getValues();

                        for (Root.ValuesBean m : menuList) {

                            try {
                                if (m.getParameter().toLowerCase().contains(searchedVal) || m.getPerm_limit().toLowerCase().contains(searchedVal) || m.getPerm_desc().toLowerCase().contains(searchedVal)) {
                                    categoryString = c.getParcat_name();
                                    ms.add(m);
                                } *//**//*else if (m.getPerm_limit().toLowerCase().contains(searchedVal)) {
                                    categoryString = c.getParcat_name();
                                    ms.add(m);
                                } else if (m.getPerm_desc().toLowerCase().contains(searchedVal)) {
                                    categoryString = c.getParcat_name();
                                    ms.add(m);
                                }*//**//*


                            }catch (NullPointerException e){
                                e.printStackTrace();
                            }

                        }

                       *//**//* for (Root.ValuesBean m : menuList) {
                            try {

                                if (m.getProd_name().toLowerCase().contains(searchedVal)) {
                                    categoryString = c.getParcat_name();
                                    ms.add(m);
                                }
                            }catch (NullPointerException e){
                                e.printStackTrace();
                            }

                        }*//**//*


                    }
                    cacheMenuResList.add(new Root(categoryString, ms));
                    expandAdapter.setCacheMenuRes(cacheMenuResList);
                }
*//*
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/


    }


    private void setListViewHeight(ExpandableListView listView, int group) {
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < expandAdapter.getGroupCount(); i++) {
            View groupItem = expandAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < expandAdapter.getChildrenCount(i); j++) {
                    View listItem = expandAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
                //Add Divider Height
                totalHeight += listView.getDividerHeight() * (expandAdapter.getChildrenCount(i) - 1);
            }
        }
        //Add Divider Height
        totalHeight += listView.getDividerHeight() * (expandAdapter.getGroupCount() - 1);

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (expandAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    public void click(){
        activityAdvanceFilterBinding.back.setOnClickListener(this);
        activityAdvanceFilterBinding.btnSearch.setOnClickListener(this);
        activityAdvanceFilterBinding.imgfilter.setOnClickListener(this);
        activityAdvanceFilterBinding.searchfilterdata.setOnClickListener(this);
        activityAdvanceFilterBinding.cancelsearchfilterdata.setOnClickListener(this);
         activityAdvanceFilterBinding.subcatdownarrow.setOnClickListener(this);
        activityAdvanceFilterBinding.catdownarrow.setOnClickListener(this);
        activityAdvanceFilterBinding.refreshimage.setOnClickListener(this);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onClick(View v) {
        topanim= AnimationUtils.loadAnimation(this,R.xml.top_animation);
        if (v.getId()== R.id.back){
            if (Utlity.get_user(this).getUserroles_id().equalsIgnoreCase("2")){
                startActivity(new Intent(this,AdminMainActivity.class));
                finishAffinity();
            }
            else if (Utlity.get_user(this).getUserroles_id().equalsIgnoreCase("3")){
                startActivity(new Intent(this,UserMainActivity.class));
                finishAffinity();
            }
        }
        else if (v.getId()== R.id.btnSearch) {

           /* fileds = new ArrayList<>();
            fileds.add(new Validation_custome("text", activityAdvanceFilterBinding.etkeyword));*/
            //if (Utlity.validation(this, fileds)) {
          /*  if (activityAdvanceFilterBinding.catspinner.getSelectedItemPosition() == 0) {
                Utlity.show_toast(this, "please select Any Option");
            }
            else {*/
            searchData();

            //}

       // }


        }else if (v.getId()==R.id.imgfilter){
           // catshowdata();
           // categorydata();
            //activityAdvanceFilterBinding.etkeyword.setText(" ");
            catid=null;
            subcatid=null;
            layoutcatfirst.setVisibility(View.GONE);
            activityAdvanceFilterBinding.linearsearchdata.setVisibility(View.VISIBLE);
            activityAdvanceFilterBinding.linearshowdata.setVisibility(View.GONE);
            catname.setText("Category");
            subcatename.setText("Sub Category");
        }else if (v.getId()==R.id.searchfilterdata){
            searchAdavanceFilterData();
            activityAdvanceFilterBinding.cancelsearchfilterdata.setVisibility(View.VISIBLE);
            activityAdvanceFilterBinding.searchfilterdata.setVisibility(View.GONE);

        }else if (v.getId()== R.id.cancelsearchfilterdata){
            searchData();
            activityAdvanceFilterBinding.cancelsearchfilterdata.setVisibility(View.GONE);
            activityAdvanceFilterBinding.searchfilterdata.setVisibility(View.VISIBLE);
            activityAdvanceFilterBinding.searching.getText().clear();
        }else if (v.getId() == R.id.subcatdownarrow){
            showdata(catid);
        }else if (v.getId() == R.id.catdownarrow){
            catshowdata();
        }else if (v.getId()==R.id.refreshimage){
            searchData();
        }
    }

   /* public void categorydata(){
        Utlity.show_progress(this);
        apiInterface = ApiClients.getClient1().create(RetApis.class);
        Call<ResponseBody> call=apiInterface.Categories("Bearer "+ Utlity.get_user(this).getToken());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        Utlity.dismiss_dilog(AdvanceFilterActivity.this);
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        categoryModels = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<CategoryModel>>() {}.getType());
                        catlist=new ArrayList<>();
                        catlist.add("Category");
                        for (CategoryModel categoryModel:categoryModels){
                            catlist.add(categoryModel.getCat_name());
                        }

                        ArrayAdapter<String> adapter1 =
                                new ArrayAdapter<String>(AdvanceFilterActivity.this,  R.layout.spinner_item, catlist);
                        adapter1.setDropDownViewResource( R.layout.spinneritem);
                        activityAdvanceFilterBinding.catspinner.setAdapter(adapter1);
                        activityAdvanceFilterBinding.catspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position!=0){
                                    cateid=categoryModels.get(position-1).getId();
                                    cat1=categoryModels.get(position-1).getCat_name();
                                   // subCatdata(cateid);
                                    //showdata(cateid);
                                    layoutcatfirst.setVisibility(View.VISIBLE);
                                    Utlity.show_toast(AdvanceFilterActivity.this, cateid);
                                    count++;
                                    if (count>1) {
                                      //  activityAdvanceFilterBinding.subcatspinner2.setAdapter(null);
                                       // activityAdvanceFilterBinding.subcatspinner3.setAdapter(null);
                                       // activityAdvanceFilterBinding.subcatspinner4.setAdapter(null);
                                        cat2="";
                                       // cat3="";
                                       // cat4="";
                                       // cat5="";
                                      //  activityAdvanceFilterBinding.catsecond.setVisibility(View.GONE);
                                      //  activityAdvanceFilterBinding.catthird.setVisibility(View.GONE);
                                      //  activityAdvanceFilterBinding.catfour.setVisibility(View.GONE);
                                        //subCategoryModels.clear();
                                    }
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog(AdvanceFilterActivity.this);
            }
        });
    }*/

   /* public void subCatdata(String categoryid){
        Utlity.show_progress(this);
        Request result= get(this, subcategorylist+categoryid);
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Utlity.dismiss_dilog(AdvanceFilterActivity.this);
            }

            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String apidata = null;
                        try {
                            Utlity.dismiss_dilog(AdvanceFilterActivity.this);
                            apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            parameterCategoryModels = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<ParameterCategoryModel>>() {}.getType());
                            if (parameterCategoryModels.size()!=0){
                                activityAdvanceFilterBinding.catfirst.setVisibility(View.VISIBLE);
                                catlist=new ArrayList<>();
                                catlist.add("SubCategory");
                                for (ParameterCategoryModel parameterCategoryModel:parameterCategoryModels){
                                    catlist.add(parameterCategoryModel.getCat_name());
                                }

                                ArrayAdapter<String> adapter1 =
                                        new ArrayAdapter<String>(AdvanceFilterActivity.this,  R.layout.spinner_item, catlist);
                                adapter1.setDropDownViewResource( R.layout.spinneritem);
                                activityAdvanceFilterBinding.subcatspinner.setAdapter(adapter1);
                            }
                            else {
                                activityAdvanceFilterBinding.catfirst.setVisibility(View.GONE);
                            }


                            activityAdvanceFilterBinding.subcatspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (position!=0){
                                        subcateid=parameterCategoryModels.get(position-1).getId();
                                        cat2=parameterCategoryModels.get(position-1).getCat_name();
                                        Utlity.show_toast(AdvanceFilterActivity.this, subcateid);
                                        //subCatdat                                                                                                                                                               a2(subcateid);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });


                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }

        });
    }*/

   /* private void subCatdata2(String categoryid) {
        Utlity.show_progress(this);
        Request result= get(this, subcategorylist+categoryid);
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Utlity.dismiss_dilog(AdvanceFilterActivity.this);
            }

            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Utlity.dismiss_dilog(AdvanceFilterActivity.this);
                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            subCategoryModels = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SubCategoryModel>>() {}.getType());
                            if (subCategoryModels.size()!=0) {
                                activityAdvanceFilterBinding.catsecond.setVisibility(View.VISIBLE);
                                catlist=new ArrayList<>();
                                catlist.add("SubCategory");
                                for (SubCategoryModel subCategoryModel:subCategoryModels){
                                    catlist.add(subCategoryModel.getCat_name());
                                }

                                ArrayAdapter<String> adapter1 =
                                        new ArrayAdapter<String>(AdvanceFilterActivity.this,  R.layout.spinner_item, catlist);
                                adapter1.setDropDownViewResource( R.layout.spinneritem);
                                activityAdvanceFilterBinding.subcatspinner2.setAdapter(adapter1);
                            }
                            else {
                                activityAdvanceFilterBinding.catsecond.setVisibility(View.GONE);
                            }

                            activityAdvanceFilterBinding.subcatspinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (position!=0){
                                        subcateid2=subCategoryModels.get(position-1).getId();
                                        cat3=subCategoryModels.get(position-1).getCat_name();
                                        Utlity.show_toast(AdvanceFilterActivity.this, subcateid2);
                                        subCatdata3(subcateid2);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }

        });

    }*/
   /* private void subCatdata3(String categoryid) {
        Utlity.show_progress(this);
        Request result= get(this, subcategorylist+categoryid);
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Utlity.dismiss_dilog(AdvanceFilterActivity.this);
            }

            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Utlity.dismiss_dilog(AdvanceFilterActivity.this);
                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            subCategoryModels2 = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SubCategoryModel2>>() {}.getType());

                            if (subCategoryModels2.size()!=0){
                                activityAdvanceFilterBinding.catthird.setVisibility(View.VISIBLE);
                                catlist=new ArrayList<>();
                                catlist.add("SubCategory");
                                for (SubCategoryModel2 subCategoryModel:subCategoryModels2){
                                    catlist.add(subCategoryModel.getCat_name());
                                }

                                ArrayAdapter<String> adapter1 =
                                        new ArrayAdapter<String>(AdvanceFilterActivity.this,  R.layout.spinner_item, catlist);
                                adapter1.setDropDownViewResource( R.layout.spinneritem);
                                activityAdvanceFilterBinding.subcatspinner3.setAdapter(adapter1);
                            }
                            else {
                                activityAdvanceFilterBinding.catthird.setVisibility(View.GONE);
                            }
                            activityAdvanceFilterBinding.subcatspinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (position!=0){
                                        subcateid3=subCategoryModels2.get(position-1).getId();
                                        cat4=subCategoryModels2.get(position-1).getCat_name();
                                        Utlity.show_toast(AdvanceFilterActivity.this, subcateid3);
                                        subCatdata4(subcateid3);


                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });


                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }

        });


    }
    private void subCatdata4(String categoryid) {
        Utlity.show_progress(this);
        Request result= get(this, subcategorylist+categoryid);
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Utlity.dismiss_dilog(AdvanceFilterActivity.this);
            }

            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Utlity.dismiss_dilog(AdvanceFilterActivity.this);
                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            subCategoryModels3 = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SubCategoryModel3>>() {}.getType());
                            if (subCategoryModels3.size()!=0){
                                activityAdvanceFilterBinding.catfour.setVisibility(View.VISIBLE);

                                catlist=new ArrayList<>();
                                catlist.add("SubCategory");
                                for (SubCategoryModel3 subCategoryModel:subCategoryModels3){
                                    catlist.add(subCategoryModel.getCat_name());
                                }

                                ArrayAdapter<String> adapter1 =
                                        new ArrayAdapter<String>(AdvanceFilterActivity.this,  R.layout.spinner_item, catlist);
                                adapter1.setDropDownViewResource( R.layout.spinneritem);
                                activityAdvanceFilterBinding.subcatspinner4.setAdapter(adapter1);
                            }
                            else {
                                activityAdvanceFilterBinding.catfour.setVisibility(View.GONE);
                            }

                            activityAdvanceFilterBinding.subcatspinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (position!=0){
                                        subcateid4=subCategoryModels3.get(position-1).getId();
                                        cat5=subCategoryModels3.get(position-1).getCat_name();
                                        Utlity.show_toast(AdvanceFilterActivity.this, subcateid4);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }

        });


    }*/
/*private void searchspinner( View view){
    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
    View popupView = inflater.inflate(R.layout.searchspinner, null);
    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
    boolean focusable = true; // lets taps outside the popup also dismiss it
    final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
    popupWindow.showAtLocation(view, Gravity.END, 50, 70);
}*/
    private void searchData() {
       // keyword=activityAdvanceFilterBinding.etkeyword.getText().toString();
        Utlity.show_progress(this);
        HashMap<String, String> keys = new HashMap<>();
        keys.put("cat_id", catid);
        keys.put("subcat_id1", subcatid);
       // keys.put("subcat_id2", subcateid2);
       // keys.put("subcat_id3", subcateid3);
      //  keys.put("subcat_id4", subcateid4);
        // keys.put("paramcats_id", paracateid);
       // keys.put("keyword", keyword);
        Request result= post( keys, Apis.advancefilter);
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Utlity.dismiss_dilog(AdvanceFilterActivity.this);
            }

            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);


                            if (response.isSuccessful()){
                                //peoples = new JSONArray(apidata);
                                roottList = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<ArrayList<Root>>() {}.getType());
                                //Log.d("responce",advanceDataModels.toString());

                               /* for(int i=0;i<peoples.length();i++){
                                    JSONObject c = peoples.getJSONObject(i);
                                    parcat_name = c.getString("parcat_name");
                                    parameter = c.getString("parameter");
                                    expandableListTitle.add(parcat_name);
                                    top250.add(parameter);
                                    expandableListDetail.put(expandableListTitle.get(i), top250);
                                }*/
                                if (roottList.size()!=0){
                                    activityAdvanceFilterBinding.linearsearchdata.setVisibility(View.GONE);
                                    activityAdvanceFilterBinding.linearshowdata.setVisibility(View.VISIBLE);
                                   // activityAdvanceFilterBinding.searchdata.setVisibility(View.VISIBLE);
                                    activityAdvanceFilterBinding.expanddata.setVisibility(View.VISIBLE);
                                    activityAdvanceFilterBinding.line.setVisibility(View.VISIBLE);
                                    activityAdvanceFilterBinding.noreords.setVisibility(View.GONE);

                                   // expandableListDetail = ExpendModal.getData();
                                 //   expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
                                    expandAdapter = new ExpandAdapter(AdvanceFilterActivity.this, roottList);
                                    activityAdvanceFilterBinding.expanddata.setAdapter(expandAdapter);
                                    Utlity.dismiss_dilog(AdvanceFilterActivity.this);
                                    activityAdvanceFilterBinding.nesscrool.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            activityAdvanceFilterBinding.nesscrool.fullScroll(ScrollView.FOCUS_UP);
                                        }
                                    });
                                    /*activityAdvanceFilterBinding.searchdata.setHasFixedSize(true);
                                    activityAdvanceFilterBinding.searchdata.setLayoutManager(new LinearLayoutManager(AdvanceFilterActivity.this));
                                    activityAdvanceFilterBinding.searchdata.addItemDecoration(new DividerItemDecoration(AdvanceFilterActivity.this, DividerItemDecoration.HORIZONTAL));
                                    searchShowAdapter= new SearchShowAdapter(AdvanceFilterActivity.this,advanceDataModels);
                                    activityAdvanceFilterBinding.searchdata.setAdapter(searchShowAdapter);*/

                                }
                                else {
                                    activityAdvanceFilterBinding.expanddata.setVisibility(View.GONE);
                                    activityAdvanceFilterBinding.line.setVisibility(View.GONE);
                                   // activityAdvanceFilterBinding.searchdata.setVisibility(View.GONE);
                                    activityAdvanceFilterBinding.noreords.setVisibility(View.VISIBLE);
                                    activityAdvanceFilterBinding.linearshowdata.setVisibility(View.VISIBLE);
                                    activityAdvanceFilterBinding.linearsearchdata.setVisibility(View.GONE);
                                    Utlity.dismiss_dilog(AdvanceFilterActivity.this);
                                }

                                /*if (keyword.equals("")){
                                    activityAdvanceFilterBinding.keyword.setVisibility(View.GONE);
                                }
                                else {
                                    activityAdvanceFilterBinding.keyword.setText(keyword);
                                }*/

                                if (catname==null){
                                    activityAdvanceFilterBinding.parcat1.setText(getResources().getString(R.string.parentcategory));

                                }
                                else {
                                    activityAdvanceFilterBinding.parcat1.setText(catname.getText().toString());

                                }

                              if (subcatename==null){
                                    activityAdvanceFilterBinding.cat2.setVisibility(View.GONE);

                                }
                                else {
                                    activityAdvanceFilterBinding.cat2.setText(subcatename.getText().toString());

                                }

                               /* if (cat3==null){
                                    activityAdvanceFilterBinding.cat3.setText(getResources().getString(R.string.parametercategory));

                                }
                                else {
                                    activityAdvanceFilterBinding.cat3.setText(cat3);

                                }

                                if (cat4==null){
                                    activityAdvanceFilterBinding.cat4.setText(getResources().getString(R.string.parametercategory1));

                                }
                                else {
                                    activityAdvanceFilterBinding.cat4.setText(cat4);

                                }

                                if (cat5==null){
                                    activityAdvanceFilterBinding.cat5.setText(getResources().getString(R.string.parametercategory2));
                                }
                                else {
                                    activityAdvanceFilterBinding.cat5.setText(cat5);
                                }*/

                                //startActivity(new Intent(AdvanceFilterActivity.this,SearchShowActivity.class).putExtra("detail", Utlity.gson.toJson(advanceDataModels)));
                                // Utlity.show_toast(AdvanceFilterActivity.this,object.getInt("success")+" Added Sucessfully");

                            }
                            else {
                                Utlity.show_toast(AdvanceFilterActivity.this,"Unauthorised");
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
    private void searchAdavanceFilterData() {
        Utlity.show_progress(this);
       // keyword=activityAdvanceFilterBinding.etkeyword.getText().toString();
         search =  activityAdvanceFilterBinding.searching.getText().toString();
        HashMap<String, String> keys = new HashMap<>();
        keys.put("cat_id", catid);
        keys.put("subcat_id1", subcatid);
       // keys.put("keyword", keyword);
        keys.put("searchkeyword", search);
        Request result= post( keys, Apis.advancefiltersearchdata);
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Utlity.dismiss_dilog(AdvanceFilterActivity.this);
            }

            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);


                            if (response.isSuccessful()){
                                //peoples = new JSONArray(apidata);
                                roottList = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<ArrayList<Root>>() {}.getType());
                                //Log.d("responce",advanceDataModels.toString());

                               /* for(int i=0;i<peoples.length();i++){
                                    JSONObject c = peoples.getJSONObject(i);
                                    parcat_name = c.getString("parcat_name");
                                    parameter = c.getString("parameter");
                                    expandableListTitle.add(parcat_name);
                                    top250.add(parameter);
                                    expandableListDetail.put(expandableListTitle.get(i), top250);
                                }*/
                                if (roottList.size()!=0){
                                    activityAdvanceFilterBinding.linearsearchdata.setVisibility(View.GONE);
                                    activityAdvanceFilterBinding.linearshowdata.setVisibility(View.VISIBLE);
                                    // activityAdvanceFilterBinding.searchdata.setVisibility(View.VISIBLE);
                                    activityAdvanceFilterBinding.expanddata.setVisibility(View.VISIBLE);
                                    activityAdvanceFilterBinding.line.setVisibility(View.VISIBLE);
                                    activityAdvanceFilterBinding.noreords.setVisibility(View.GONE);

                                    // expandableListDetail = ExpendModal.getData();
                                    //   expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
                                    expandAdapter = new ExpandAdapter(AdvanceFilterActivity.this, roottList);
                                    activityAdvanceFilterBinding.expanddata.setAdapter(expandAdapter);
                                    Utlity.dismiss_dilog(AdvanceFilterActivity.this);
                                    /*activityAdvanceFilterBinding.searchdata.setHasFixedSize(true);
                                    activityAdvanceFilterBinding.searchdata.setLayoutManager(new LinearLayoutManager(AdvanceFilterActivity.this));
                                    activityAdvanceFilterBinding.searchdata.addItemDecoration(new DividerItemDecoration(AdvanceFilterActivity.this, DividerItemDecoration.HORIZONTAL));
                                    searchShowAdapter= new SearchShowAdapter(AdvanceFilterActivity.this,advanceDataModels);
                                    activityAdvanceFilterBinding.searchdata.setAdapter(searchShowAdapter);*/

                                }
                                else {
                                    activityAdvanceFilterBinding.expanddata.setVisibility(View.GONE);
                                    activityAdvanceFilterBinding.line.setVisibility(View.GONE);
                                    // activityAdvanceFilterBinding.searchdata.setVisibility(View.GONE);
                                    activityAdvanceFilterBinding.noreords.setVisibility(View.VISIBLE);
                                    activityAdvanceFilterBinding.linearshowdata.setVisibility(View.VISIBLE);
                                    activityAdvanceFilterBinding.linearsearchdata.setVisibility(View.GONE);
                                    Utlity.dismiss_dilog(AdvanceFilterActivity.this);
                                }

                            }
                            else {
                                Utlity.show_toast(AdvanceFilterActivity.this,"Unauthorised");
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
    public Request get(Activity activity, String api_name) {
        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        return new Request.Builder()
                .url(api_name)
                .get()
                .header("Authorization","Bearer "+ Utlity.get_user(this).getToken())
                .build();
    }
    private void showdata(String categoryid) {

        dialog = new Dialog(AdvanceFilterActivity.this);
        SearchView searchView;
        RecyclerView recyclerView;
        TextView tvcancel,norecods;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.customsearch);
        dialog.getWindow().setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
        searchView = dialog.findViewById(R.id.search);
        recyclerView = dialog.findViewById(R.id.customsearchrecyler);
        tvcancel = dialog.findViewById(R.id.customclose);
        norecods = dialog.findViewById(R.id.noreords);
        Request result= get(this, subcategorylist+categoryid);
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                progressDialog.cancel();
            }

            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String apidata = null;
                        try {
                            apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            parameterCategoryModels = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<ParameterCategoryModel>>() {}.getType());
                            if (parameterCategoryModels.size()!=0){
                                recyclerView.setVisibility(View.VISIBLE);
                                recyclerView.setLayoutManager(new LinearLayoutManager(AdvanceFilterActivity.this,RecyclerView.VERTICAL,false));
                                customsearchSpinnerAdapter = new CustomsearchSpinnerAdapter(AdvanceFilterActivity.this,parameterCategoryModels);
                                recyclerView.setAdapter(customsearchSpinnerAdapter);
                                recyclerView.setHasFixedSize(true);
                                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                    @Override
                                    public boolean onQueryTextSubmit(String query) {
                                        if(parameterCategoryModels.contains(query)){
                                            customsearchSpinnerAdapter.getFilter().filter(query);
                                        }else{
                                            Toast.makeText(AdvanceFilterActivity.this, "No Match found",Toast.LENGTH_LONG).show();
                                        }
                                        return false;
                                    }
                                    @Override
                                    public boolean onQueryTextChange(String newText) {
                                        customsearchSpinnerAdapter.getFilter().filter(newText);
                                        return false;
                                    }
                                });
                                progressDialog.cancel();
                                //  ArrayAdapter<String> adapter1 =
                                //          new ArrayAdapter<String>(AdvanceFilterActivity.this,  R.layout.spinner_item, catlist);
                                // adapter1.setDropDownViewResource( R.layout.spinneritem);
                                //  activityAdvanceFilterBinding.subcatspinner.setAdapter(adapter1);
                            }
                            else {
                                activityAdvanceFilterBinding.catfirst.setVisibility(View.GONE);
                                norecods.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                                progressDialog.cancel();
                            }


                           /* activityAdvanceFilterBinding.subcatspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (position!=0){
                                        subcateid=parameterCategoryModels.get(position-1).getId();
                                        cat2=parameterCategoryModels.get(position-1).getCat_name();
                                        Utlity.show_toast(AdvanceFilterActivity.this, subcateid);
                                        //subCatdata2(subcateid);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
*/

                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }

        });
        dialog.show();
        progressDialog.show();
   tvcancel.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           dialog.cancel();
       }
   });
    }
    private void catshowdata() {
        catdialog = new Dialog(AdvanceFilterActivity.this);
        SearchView catsearchView;
        RecyclerView catrecyclerView;
        TextView cattvcancel;
        catdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        catdialog.setContentView(R.layout.customcatsearch);
        catdialog.getWindow().setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
        catsearchView = catdialog.findViewById(R.id.catsearch);
        catrecyclerView = catdialog.findViewById(R.id.catcustomsearchrecyler);
        cattvcancel = catdialog.findViewById(R.id.catcustomclose);
        apiInterface = ApiClients.getClient1().create(RetApis.class);
        Call<ResponseBody> call=apiInterface.Categories("Bearer "+ Utlity.get_user(this).getToken());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        progressDialog.cancel();
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        categoryModels = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<CategoryModel>>() {}.getType());
                        if (categoryModels.size()!=0){
                            catrecyclerView.setLayoutManager(new LinearLayoutManager(AdvanceFilterActivity.this,RecyclerView.VERTICAL,false));
                            customCatsearchSpinnerAdapter = new CustomCatsearchSpinnerAdapter(AdvanceFilterActivity.this,categoryModels);
                            catrecyclerView.setAdapter(customCatsearchSpinnerAdapter);
                            catrecyclerView.setHasFixedSize(true);
                            catsearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String query) {
                                    if(categoryModels.contains(query)){
                                        customCatsearchSpinnerAdapter.getFilter().filter(query);
                                    }else{
                                        Toast.makeText(AdvanceFilterActivity.this, "No Match found",Toast.LENGTH_LONG).show();
                                    }
                                    return false;
                                }
                                @Override
                                public boolean onQueryTextChange(String newText) {
                                    customCatsearchSpinnerAdapter.getFilter().filter(newText);
                                    return false;
                                }
                            });

                            //  ArrayAdapter<String> adapter1 =
                            //          new ArrayAdapter<String>(AdvanceFilterActivity.this,  R.layout.spinner_item, catlist);
                            // adapter1.setDropDownViewResource( R.layout.spinneritem);
                            //  activityAdvanceFilterBinding.subcatspinner.setAdapter(adapter1);
                        }
                        else {
                            activityAdvanceFilterBinding.catfirst.setVisibility(View.GONE);
                        }


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.cancel();
            }
        });
        catdialog.show();
        progressDialog.show();
        cattvcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catdialog.cancel();
            }
        });
    }
}