package com.safefoodmitra.safefoodmitra.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
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
import com.safefoodmitra.safefoodmitra.Modals.InspectrespoModals;
import com.safefoodmitra.safefoodmitra.Modals.ZoneModals;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.DepartitemBinding;
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

import static com.safefoodmitra.safefoodmitra.Apis.Apis.Deletezones;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Editzones;

public class InspectResponsibilityAdapter extends RecyclerView.Adapter<InspectResponsibilityAdapter.Myview> {
    LayoutInflater layoutInflater;
    Activity context;
    List<InspectrespoModals> inspectrespoModals;
    InspectResponsibilityAdapter2  inspectResponsibilityAdapter2;

    public InspectResponsibilityAdapter(Activity context, List<InspectrespoModals> inspectrespoModals) {
        this.context = context;
        this.inspectrespoModals = inspectrespoModals;

    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        DepartitemBinding departitemBinding= DataBindingUtil.inflate(layoutInflater, R.layout.departitem,parent,false);
        return new Myview(departitemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final Myview holder, final int position) {
        final InspectrespoModals inspectrespoModal=inspectrespoModals.get(position);
        holder.departitemBinding.dptnme.setText(inspectrespoModals.get(position).getDept_name());
        inspectResponsibilityAdapter2 = new InspectResponsibilityAdapter2(context,inspectrespoModal.getInspectrespoModals2s());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        holder.departitemBinding.secondlist.setLayoutManager(layoutManager);
        holder.departitemBinding.secondlist.setAdapter(inspectResponsibilityAdapter2);


    }

    @Override
    public int getItemCount() {
        return inspectrespoModals.size();
    }


    public class Myview extends RecyclerView.ViewHolder {
        DepartitemBinding departitemBinding;
        public Myview(DepartitemBinding departitemBinding) {
            super(departitemBinding.getRoot());
            this.departitemBinding=departitemBinding;
        }
    }

    public void setCacheMenuRes(List<InspectrespoModals> inspectrespoModals)
    {
        this.inspectrespoModals = inspectrespoModals;
        notifyDataSetChanged();
    }
}
