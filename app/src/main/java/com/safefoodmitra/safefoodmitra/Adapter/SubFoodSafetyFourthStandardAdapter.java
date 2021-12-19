package com.safefoodmitra.safefoodmitra.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.safefoodmitra.safefoodmitra.Activities.SubfoodsafetystandardActivity;
import com.safefoodmitra.safefoodmitra.Apis.Apis;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.SubFoodSafetyModel;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.SubfoodsafetyitemfourthBinding;

import java.util.ArrayList;
import java.util.List;


public class SubFoodSafetyFourthStandardAdapter extends RecyclerView.Adapter<SubFoodSafetyFourthStandardAdapter.Myview> {
    LayoutInflater layoutInflater;
    Context context;
    List<SubFoodSafetyModel> subFoodSafetyModels;
    List<SubFoodSafetyModel> subFoodSafetyModels1;
    int row_index= -1;

    public SubFoodSafetyFourthStandardAdapter(Context context, List<SubFoodSafetyModel> subFoodSafetyModels) {
        this.context = context;
        this.subFoodSafetyModels = subFoodSafetyModels;
        this.subFoodSafetyModels1 = new ArrayList<>(subFoodSafetyModels);
    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        SubfoodsafetyitemfourthBinding subfoodsafetyitemfourthBinding = DataBindingUtil.inflate(layoutInflater, R.layout.subfoodsafetyitemfourth,parent,false);
        return new Myview(subfoodsafetyitemfourthBinding);
    }



    @Override
    public void onBindViewHolder(@NonNull final Myview holder, final int position) {
        SubFoodSafetyModel subFoodSafetyModel= subFoodSafetyModels.get(position);
        holder.subfoodsafetyitemfourthBinding.fsmstitles.setText(subFoodSafetyModel.getCat_name());
        Utlity.Set_imageGlide(context,subFoodSafetyModels.get(position).getIcon_path(),holder.subfoodsafetyitemfourthBinding.foodicon);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subFoodSafetyModel.getId()!=null){
                    if (subFoodSafetyModel.getIs_subcats().equals("1") && subFoodSafetyModel.getIs_docs().equals("0")){
                        if (context instanceof SubfoodsafetystandardActivity) {
                            ((SubfoodsafetystandardActivity)context).subCatedata5(subFoodSafetyModel.getId());
                        }
                    }else if (subFoodSafetyModel.getIs_docs().equals("1") && subFoodSafetyModel.getIs_subcats().equals("0")){
                        if (context instanceof SubfoodsafetystandardActivity) {
                            ((SubfoodsafetystandardActivity)context).subcatepdffile5(subFoodSafetyModel.getId());
                        }
                    }
                    else if (subFoodSafetyModel.getIs_docs().equals("1") && subFoodSafetyModel.getIs_subcats().equals("1")){

                        if (context instanceof SubfoodsafetystandardActivity) {
                            ((SubfoodsafetystandardActivity)context).subCatedata5duble(subFoodSafetyModel.getId());
                        }
                      /*  if (context instanceof SubfoodsafetystandardActivity) {
                            ((SubfoodsafetystandardActivity)context).subcatepdffile(subFoodSafetyModel.getId());
                        }*/
                    }

                    else {
                        if (context instanceof SubfoodsafetystandardActivity) {
                            ((SubfoodsafetystandardActivity)context).subCatedata5duble(subFoodSafetyModel.getId());
                        }
                        // Utlity.show_toast((Activity) context,"show");
                    }
                }

                row_index=position;
                notifyDataSetChanged();
            }
        });
        if(row_index==position){
            holder.subfoodsafetyitemfourthBinding.foodicon.setBorderColor(context.getResources().getColor(R.color.purple_200));
            holder.subfoodsafetyitemfourthBinding.fsmstitles.setTextColor(context.getResources().getColor(R.color.purple_200));
        }
        else
        {
            holder.subfoodsafetyitemfourthBinding.foodicon.setBorderColor(context.getResources().getColor(R.color.white));
            holder.subfoodsafetyitemfourthBinding.fsmstitles.setTextColor(Color.BLACK);
        }
    }


    @Override
    public int getItemCount() {
        return subFoodSafetyModels.size();
    }
    public Filter getFilter() {
        return selectfilter;
    }
    private Filter selectfilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<SubFoodSafetyModel> filterdlist = new ArrayList<>();

            if (constraint==null || constraint.length() ==0){
                filterdlist.addAll(subFoodSafetyModels1);

            }
            else {
                for (SubFoodSafetyModel zoneModals:subFoodSafetyModels1){
                    String textfilter=zoneModals.getCat_name().toLowerCase();
                    String idfilter= zoneModals.getId().toLowerCase();
                    if(textfilter.contains(constraint.toString().toLowerCase())){
                        filterdlist.add(zoneModals);
                    }
                    else if (idfilter.contains(constraint.toString().toLowerCase())){
                        filterdlist.add(zoneModals);
                    }

                }
            }
            FilterResults results = new FilterResults();
            results.values = filterdlist;
            return  results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            subFoodSafetyModels.clear();
            subFoodSafetyModels.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    public class Myview extends RecyclerView.ViewHolder {
        SubfoodsafetyitemfourthBinding subfoodsafetyitemfourthBinding;
        public Myview(SubfoodsafetyitemfourthBinding subfoodsafetyitemfourthBinding) {
            super(subfoodsafetyitemfourthBinding.getRoot());
            this.subfoodsafetyitemfourthBinding=subfoodsafetyitemfourthBinding;
        }
    }
}