package com.safefoodmitra.safefoodmitra.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.safefoodmitra.safefoodmitra.Activities.SubdocumentlistActivity;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.DocumentsModel;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.DocumentsitemBinding;

import java.util.List;


public class DocumentsAdapter extends RecyclerView.Adapter<DocumentsAdapter.Myview> {
    LayoutInflater layoutInflater;
    Context context;
    List<DocumentsModel> documentsModels;
    String id;

    public DocumentsAdapter(Context context, List<DocumentsModel> documentsModels) {
        this.context = context;
        this.documentsModels = documentsModels;
    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        DocumentsitemBinding documentsitemBinding= DataBindingUtil.inflate(layoutInflater, R.layout.documentsitem,parent,false);
        return new Myview(documentsitemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final Myview holder, final int position) {
        DocumentsModel documentsModel = documentsModels.get(position);
        holder.documentsitemBinding.fsmstitles.setText(documentsModel.getCat_name());
        id=documentsModel.getId();
        if (documentsModel.getId()!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, SubdocumentlistActivity.class).putExtra("detail", Utlity.gson.toJson(documentsModel)));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return documentsModels.size();
    }


    public class Myview extends RecyclerView.ViewHolder {
        DocumentsitemBinding documentsitemBinding;
        public Myview(DocumentsitemBinding documentsitemBinding) {
            super(documentsitemBinding.getRoot());
            this.documentsitemBinding=documentsitemBinding;
        }
    }
}
