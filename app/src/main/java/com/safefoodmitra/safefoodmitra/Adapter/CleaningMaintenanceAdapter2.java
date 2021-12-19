package com.safefoodmitra.safefoodmitra.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.safefoodmitra.safefoodmitra.Activities.CMFDetailsActivity;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.CleaningMaintModel;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.Cleaningmaintenance2Binding;
import com.safefoodmitra.safefoodmitra.databinding.CleaningmaintenanceBinding;

import java.util.List;


public class CleaningMaintenanceAdapter2 extends RecyclerView.Adapter<CleaningMaintenanceAdapter2.Myview> {
    LayoutInflater layoutInflater;
    Activity context;
    List<CleaningMaintModel.Datalist> cleaningMaintModels;
    public CleaningMaintenanceAdapter2(Activity context, List<CleaningMaintModel.Datalist> cleaningMaintModels) {
        this.context = context;
        this.cleaningMaintModels = cleaningMaintModels;
    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        Cleaningmaintenance2Binding cleaningmaintenance2Binding = DataBindingUtil.inflate(layoutInflater, R.layout.cleaningmaintenance2,parent,false);
        return new Myview(cleaningmaintenance2Binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final Myview holder, final int position) {
     CleaningMaintModel.Datalist cleaningMaintModel = cleaningMaintModels.get(position);
     holder.cleaningmaintenance2Binding.cmftype.setText(cleaningMaintModel.getCmtype());
     holder.cleaningmaintenance2Binding.assingdtime.setText(cleaningMaintModel.getAssignedtime());
     holder.cleaningmaintenance2Binding.zone.setText(cleaningMaintModel.getZone());
     holder.cleaningmaintenance2Binding.locations.setText(cleaningMaintModel.getLoc_name());
     holder.cleaningmaintenance2Binding.sublocations.setText(cleaningMaintModel.getSubloc_name());
     holder.cleaningmaintenance2Binding.responsbility.setText(cleaningMaintModel.getDept_name());
     if (cleaningMaintModel.getEquip_name()!=null){
         holder.cleaningmaintenance2Binding.equipment.setText(cleaningMaintModel.getEquip_name());
     }else {
         holder.cleaningmaintenance2Binding.tvcomment.setText("P-");
         holder.cleaningmaintenance2Binding.equipment.setText(cleaningMaintModel.getTaskcomment());
     }

     holder.cleaningmaintenance2Binding.linearcmfdetails.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
          context.startActivity(new Intent(context,CMFDetailsActivity.class).putExtra("detail", Utlity.gson.toJson(cleaningMaintModel)));
         }
     });

        if (cleaningMaintModel.getTaskstatus().equals("0")){
            holder.cleaningmaintenance2Binding.taskstatus.setText(cleaningMaintModel.getTaskstatusdisp());
            holder.cleaningmaintenance2Binding.linearcmfdetails.setBackgroundColor(ContextCompat.getColor(context, R.color.colorlightorange));
        }
       else if (cleaningMaintModel.getTaskstatus().equals("1")){
            holder.cleaningmaintenance2Binding.taskstatus.setText(cleaningMaintModel.getTaskstatusdisp());
           holder.cleaningmaintenance2Binding.linearcmfdetails.setBackgroundColor(ContextCompat.getColor(context, R.color.colorlightgreen));
        }
       else if (cleaningMaintModel.getTaskstatus().equals("2")){
            holder.cleaningmaintenance2Binding.taskstatus.setText(cleaningMaintModel.getTaskstatusdisp());
            holder.cleaningmaintenance2Binding.linearcmfdetails.setBackgroundColor(ContextCompat.getColor(context, R.color.colorlightred));
        }
    }

    @Override
    public int getItemCount() {
        return cleaningMaintModels.size();
    }


    public class Myview extends RecyclerView.ViewHolder {
        Cleaningmaintenance2Binding cleaningmaintenance2Binding;
        public Myview(Cleaningmaintenance2Binding cleaningmaintenance2Binding) {
            super(cleaningmaintenance2Binding.getRoot());
            this.cleaningmaintenance2Binding=cleaningmaintenance2Binding;
        }
    }
}