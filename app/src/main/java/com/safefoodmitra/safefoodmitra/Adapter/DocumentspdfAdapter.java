package com.safefoodmitra.safefoodmitra.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.safefoodmitra.safefoodmitra.Activities.DocumentlistActivity;
import com.safefoodmitra.safefoodmitra.Activities.PdfActivity;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.DocumentsModel;
import com.safefoodmitra.safefoodmitra.Modals.DocumentspdfModel;
import com.safefoodmitra.safefoodmitra.Modals.SubFoodDocspdfModel;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.DocumentsitemBinding;
import com.safefoodmitra.safefoodmitra.databinding.DocumentsitempdfBinding;

import java.util.List;


public class DocumentspdfAdapter extends RecyclerView.Adapter<DocumentspdfAdapter.Myview> {
    LayoutInflater layoutInflater;
    Context context;
    List<SubFoodDocspdfModel> subFoodDocspdfModels;
    String pdfFileName,baseurl,toturl;
    public DocumentspdfAdapter(Context context, List<SubFoodDocspdfModel> subFoodDocspdfModels) {
        this.context = context;
        this.subFoodDocspdfModels = subFoodDocspdfModels;
    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        DocumentsitempdfBinding documentsitempdfBinding = DataBindingUtil.inflate(layoutInflater, R.layout.documentsitempdf,parent,false);
        return new Myview(documentsitempdfBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final Myview holder, final int position) {
        SubFoodDocspdfModel subFoodDocspdfModel = subFoodDocspdfModels.get(position);
       holder.documentsitempdfBinding.fsmstitles.setText(subFoodDocspdfModel.getDoc_title());
       baseurl=subFoodDocspdfModel.getBaseurl();
       pdfFileName=subFoodDocspdfModel.getDoc_path();
        toturl=baseurl+"/"+pdfFileName;
        holder.documentsitempdfBinding.linearpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, PdfActivity.class).putExtra("detail", Utlity.gson.toJson(subFoodDocspdfModel)));
            }
        });


    }

    @Override
    public int getItemCount() {
        return subFoodDocspdfModels.size();
    }


    public class Myview extends RecyclerView.ViewHolder {
        DocumentsitempdfBinding documentsitempdfBinding;
        public Myview(DocumentsitempdfBinding documentsitempdfBinding) {
            super(documentsitempdfBinding.getRoot());
            this.documentsitempdfBinding=documentsitempdfBinding;
        }
    }
}
