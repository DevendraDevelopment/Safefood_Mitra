package com.safefoodmitra.safefoodmitra.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.CategoryModel;
import com.safefoodmitra.safefoodmitra.Modals.ParameterCategoryModel;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.CustomcatspinnerlistitemBinding;
import com.safefoodmitra.safefoodmitra.databinding.CustomspinnerlistitemBinding;

import java.util.ArrayList;
import java.util.List;

import static com.safefoodmitra.safefoodmitra.Activities.AdvanceFilterActivity.catdialog;
import static com.safefoodmitra.safefoodmitra.Activities.AdvanceFilterActivity.catname;
import static com.safefoodmitra.safefoodmitra.Activities.AdvanceFilterActivity.dialog;
import static com.safefoodmitra.safefoodmitra.Activities.AdvanceFilterActivity.layoutcatfirst;
import static com.safefoodmitra.safefoodmitra.Activities.AdvanceFilterActivity.subcatename;


public class CustomCatsearchSpinnerAdapter extends RecyclerView.Adapter<CustomCatsearchSpinnerAdapter.Myview> {
    LayoutInflater layoutInflater;
    Activity context;
    List<CategoryModel> categoryModels;
    List<CategoryModel> categoryModels1;
    public static String catid;
    public CustomCatsearchSpinnerAdapter(Activity context,  List<CategoryModel> categoryModels) {
        this.context = context;
        this.categoryModels=categoryModels;
        this.categoryModels1 = new ArrayList<>(categoryModels);
    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        CustomcatspinnerlistitemBinding customcatspinnerlistitemBinding= DataBindingUtil.inflate(layoutInflater, R.layout.customcatspinnerlistitem,parent,false);
        return new Myview(customcatspinnerlistitemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final Myview holder, final int position) {
        final CategoryModel categoryModel = categoryModels.get(position);
        holder.customcatspinnerlistitemBinding.spinneritem.setText(categoryModel.getCat_name());
       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               catname.setText(categoryModel.getCat_name());
               subcatename.setText("Sub Category");
               catid = categoryModel.getId();
              // Utlity.show_toast(context,catid);
               layoutcatfirst.setVisibility(View.VISIBLE);
               catdialog.cancel();
           }
       });
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    public Filter getFilter() {
        return selectfilter;
    }
    private Filter selectfilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<CategoryModel> filterdlist = new ArrayList<>();

            if (constraint==null || constraint.length() ==0){
                filterdlist.addAll(categoryModels1);

            }
            else {
                for (CategoryModel categoryModel:categoryModels1){
                    String textfilter=categoryModel.getCat_name().toLowerCase();
                    String idfilter= categoryModel.getId().toLowerCase();
                    if(textfilter.contains(constraint.toString().toLowerCase())){
                        filterdlist.add(categoryModel);
                    }
                    else if (idfilter.contains(constraint.toString().toLowerCase())){
                        filterdlist.add(categoryModel);
                    }

                }
            }
            FilterResults results = new FilterResults();
            results.values = filterdlist;
            return  results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            categoryModels.clear();
            categoryModels.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    public class Myview extends RecyclerView.ViewHolder {
        CustomcatspinnerlistitemBinding customcatspinnerlistitemBinding;
        public Myview(CustomcatspinnerlistitemBinding customcatspinnerlistitemBinding) {
            super(customcatspinnerlistitemBinding.getRoot());
            this.customcatspinnerlistitemBinding=customcatspinnerlistitemBinding;
        }
    }
}