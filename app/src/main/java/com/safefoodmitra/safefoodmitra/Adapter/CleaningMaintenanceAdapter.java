package com.safefoodmitra.safefoodmitra.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.safefoodmitra.safefoodmitra.Activities.SubfoodsafetystandardActivity;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.CleaningMaintModel;
import com.safefoodmitra.safefoodmitra.Modals.SubFoodSafetyModel;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.CleaningmaintenanceBinding;
import com.safefoodmitra.safefoodmitra.databinding.SubfoodsafetyitemBinding;

import java.util.ArrayList;
import java.util.List;


public class CleaningMaintenanceAdapter extends RecyclerView.Adapter<CleaningMaintenanceAdapter.Myview> {
    LayoutInflater layoutInflater;
    Activity context;
    List<CleaningMaintModel> cleaningMaintModels;
    CleaningMaintenanceAdapter2 cleaningMaintenanceAdapter2;
    public CleaningMaintenanceAdapter(Activity context,List<CleaningMaintModel> cleaningMaintModels) {
        this.context = context;
        this.cleaningMaintModels = cleaningMaintModels;
    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        CleaningmaintenanceBinding cleaningmaintenanceBinding = DataBindingUtil.inflate(layoutInflater, R.layout.cleaningmaintenance,parent,false);
        return new Myview(cleaningmaintenanceBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final Myview holder, final int position) {
     CleaningMaintModel cleaningMaintModel = cleaningMaintModels.get(position);
     holder.cleaningmaintenanceBinding.assingdate.setText(cleaningMaintModel.getAssigneddate());
     holder.cleaningmaintenanceBinding.assingday.setText(cleaningMaintModel.getAssignedday());
     cleaningMaintenanceAdapter2 = new CleaningMaintenanceAdapter2(context,cleaningMaintModel.getDatalist());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        holder.cleaningmaintenanceBinding.ceaninglist.setLayoutManager(layoutManager);
        holder.cleaningmaintenanceBinding.ceaninglist.setAdapter(cleaningMaintenanceAdapter2);
    }

    @Override
    public int getItemCount() {
        return cleaningMaintModels.size();
    }


    public class Myview extends RecyclerView.ViewHolder {
        CleaningmaintenanceBinding cleaningmaintenanceBinding;
        public Myview(CleaningmaintenanceBinding cleaningmaintenanceBinding) {
            super(cleaningmaintenanceBinding.getRoot());
            this.cleaningmaintenanceBinding=cleaningmaintenanceBinding;
        }
    }
}