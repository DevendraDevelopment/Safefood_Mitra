package com.safefoodmitra.safefoodmitra.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.safefoodmitra.safefoodmitra.Activities.SubfoodsafetystandardActivity;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.ParameterCategoryModel;
import com.safefoodmitra.safefoodmitra.Modals.SubFoodSafetyModel;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.CustomspinnerlistitemBinding;
import com.safefoodmitra.safefoodmitra.databinding.SubfoodsafetyitemBinding;

import java.util.ArrayList;
import java.util.List;

import static com.safefoodmitra.safefoodmitra.Activities.AdvanceFilterActivity.dialog;
import static com.safefoodmitra.safefoodmitra.Activities.AdvanceFilterActivity.subcatename;


public class CustomsearchSpinnerAdapter extends RecyclerView.Adapter<CustomsearchSpinnerAdapter.Myview> {
    LayoutInflater layoutInflater;
    Activity context;
    List<ParameterCategoryModel> parameterCategoryModels;
    List<ParameterCategoryModel> parameterCategoryModels1;
    public static String subcatid;
    public CustomsearchSpinnerAdapter(Activity context,  List<ParameterCategoryModel> parameterCategoryModels) {
        this.context = context;
        this.parameterCategoryModels=parameterCategoryModels;
        this.parameterCategoryModels1 = new ArrayList<>(parameterCategoryModels);
    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        CustomspinnerlistitemBinding customspinnerlistitemBinding= DataBindingUtil.inflate(layoutInflater, R.layout.customspinnerlistitem,parent,false);
        return new Myview(customspinnerlistitemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final Myview holder, final int position) {
        final ParameterCategoryModel parameterCategoryModel = parameterCategoryModels.get(position);
        holder.customspinnerlistitemBinding.spinneritem.setText(parameterCategoryModel.getCat_name());
       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               subcatename.setText(parameterCategoryModel.getCat_name());
               subcatid = parameterCategoryModel.getId();
               dialog.cancel();
           }
       });
    }

    @Override
    public int getItemCount() {
        return parameterCategoryModels.size();
    }

    public Filter getFilter() {
        return selectfilter;
    }
    private Filter selectfilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ParameterCategoryModel> filterdlist = new ArrayList<>();

            if (constraint==null || constraint.length() ==0){
                filterdlist.addAll(parameterCategoryModels1);

            }
            else {
                for (ParameterCategoryModel parameterCategoryModel:parameterCategoryModels1){
                    String textfilter=parameterCategoryModel.getCat_name().toLowerCase();
                    String idfilter= parameterCategoryModel.getId().toLowerCase();
                    if(textfilter.contains(constraint.toString().toLowerCase())){
                        filterdlist.add(parameterCategoryModel);
                    }
                    else if (idfilter.contains(constraint.toString().toLowerCase())){
                        filterdlist.add(parameterCategoryModel);
                    }

                }
            }
            FilterResults results = new FilterResults();
            results.values = filterdlist;
            return  results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            parameterCategoryModels.clear();
            parameterCategoryModels.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    public class Myview extends RecyclerView.ViewHolder {
        CustomspinnerlistitemBinding customspinnerlistitemBinding;
        public Myview(CustomspinnerlistitemBinding customspinnerlistitemBinding) {
            super(customspinnerlistitemBinding.getRoot());
            this.customspinnerlistitemBinding=customspinnerlistitemBinding;
        }
    }
}