package com.safefoodmitra.safefoodmitra.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.safefoodmitra.safefoodmitra.Activities.SubdocumentlistActivity;
import com.safefoodmitra.safefoodmitra.Activities.SubfoodsafetystandardActivity;
import com.safefoodmitra.safefoodmitra.Apis.Apis;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.DocumentsModel;
import com.safefoodmitra.safefoodmitra.Modals.FoodSafetyModel;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.DocumentsitemBinding;
import com.safefoodmitra.safefoodmitra.databinding.FoodsafetyitemBinding;

import java.util.List;


public class FoodSafetyStandardAdapter extends RecyclerView.Adapter<FoodSafetyStandardAdapter.Myview> {
    LayoutInflater layoutInflater;
    Context context;
    List<FoodSafetyModel> foodSafetyModels;
    String id;

    public FoodSafetyStandardAdapter(Context context, List<FoodSafetyModel> foodSafetyModels) {
        this.context = context;
        this.foodSafetyModels = foodSafetyModels;
    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        FoodsafetyitemBinding foodsafetyitemBinding= DataBindingUtil.inflate(layoutInflater, R.layout.foodsafetyitem,parent,false);
        return new Myview(foodsafetyitemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final Myview holder, final int position) {
       FoodSafetyModel foodSafetyModel = foodSafetyModels.get(position);
       Set_imageGlide(foodSafetyModels.get(position).getIcon_path(),holder.foodsafetyitemBinding.foodicon);
        //holder.foodsafetyitemBinding.foodicon.setImageResource(foodSafetyModel.getIcon());
        holder.foodsafetyitemBinding.fsmstitles.setText(foodSafetyModel.getCat_name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, SubfoodsafetystandardActivity.class).putExtra("detail", Utlity.gson.toJson(foodSafetyModel)));
            }
        });
       /* id=documentsModel.getId();
        if (documentsModel.getId()!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, SubdocumentlistActivity.class).putExtra("detail", Utlity.gson.toJson(documentsModel)));
                }
            });
        }*/
    }

    public void Set_imageGlide(String url, ImageView img) {
        if (!TextUtils.isEmpty(url));
        Glide.with(context).load(Apis.imageBaseicon_Url+url).error(R.drawable.ic_image_gallery).placeholder(R.drawable.ic_image_gallery).into(img);
    }

    @Override
    public int getItemCount() {
        return foodSafetyModels.size();
    }


    public class Myview extends RecyclerView.ViewHolder {
        FoodsafetyitemBinding foodsafetyitemBinding;
        public Myview(FoodsafetyitemBinding foodsafetyitemBinding) {
            super(foodsafetyitemBinding.getRoot());
            this.foodsafetyitemBinding=foodsafetyitemBinding;
        }
    }
}
