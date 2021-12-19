package com.safefoodmitra.safefoodmitra.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.reflect.TypeToken;
import com.safefoodmitra.safefoodmitra.Activities.ZoneActivity;
import com.safefoodmitra.safefoodmitra.Apis.ApiClients;
import com.safefoodmitra.safefoodmitra.Apis.RetApis;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.NewCategoryModel;
import com.safefoodmitra.safefoodmitra.Modals.ZoneModals;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.NewcatitemBinding;
import com.safefoodmitra.safefoodmitra.databinding.ZonitemBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.safefoodmitra.safefoodmitra.Activities.AdminMainActivity.respoid;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Deletezones;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Editzones;

public class NewCategoryAdapter extends RecyclerView.Adapter<NewCategoryAdapter.Myview> {
    LayoutInflater layoutInflater;
    Activity context;
    List<NewCategoryModel> newCategoryModels;



    public NewCategoryAdapter(Activity context, List<NewCategoryModel> newCategoryModels) {
        this.context = context;
        this.newCategoryModels = newCategoryModels;

    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        NewcatitemBinding newcatitemBinding= DataBindingUtil.inflate(layoutInflater, R.layout.newcatitem,parent,false);
        return new Myview(newcatitemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final Myview holder, final int position) {
        final NewCategoryModel newCategoryModel=newCategoryModels.get(position);

        holder.newcatitemBinding.cate.setText(newCategoryModels.get(position).getCat_name());
        holder.newcatitemBinding.total.setText(newCategoryModels.get(position).getCattoal());
        holder.newcatitemBinding.dte.setText(newCategoryModels.get(position).getCatdte());


    }

    @Override
    public int getItemCount() {
        return newCategoryModels.size();
    }


    public class Myview extends RecyclerView.ViewHolder {
        NewcatitemBinding newcatitemBinding;
        public Myview(NewcatitemBinding newcatitemBinding) {
            super(newcatitemBinding.getRoot());
            this.newcatitemBinding=newcatitemBinding;
        }
    }

}
