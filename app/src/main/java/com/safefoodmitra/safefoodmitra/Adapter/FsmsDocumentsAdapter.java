package com.safefoodmitra.safefoodmitra.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.safefoodmitra.safefoodmitra.Activities.DocumentlistActivity;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.FsmsDocumentsModals;
import com.safefoodmitra.safefoodmitra.Modals.UserModals;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.FsmsdocumentsitemBinding;
import com.safefoodmitra.safefoodmitra.databinding.UsersitemBinding;

import java.util.List;

public class FsmsDocumentsAdapter extends RecyclerView.Adapter<FsmsDocumentsAdapter.Myview> {
    LayoutInflater layoutInflater;
    Context context;
    List<FsmsDocumentsModals> fsmsDocumentsModals;

    public FsmsDocumentsAdapter(Context context, List<FsmsDocumentsModals> fsmsDocumentsModals) {
        this.context = context;
        this.fsmsDocumentsModals = fsmsDocumentsModals;
    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        FsmsdocumentsitemBinding fsmsdocumentsitemBinding= DataBindingUtil.inflate(layoutInflater, R.layout.fsmsdocumentsitem,parent,false);
        return new Myview(fsmsdocumentsitemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final Myview holder, final int position) {
        FsmsDocumentsModals fsmsDocumentsModals1 = fsmsDocumentsModals.get(position);
        holder.fsmsdocumentsitemBinding.fsmstitles.setText(fsmsDocumentsModals1.getCat_name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, DocumentlistActivity.class).putExtra("detail", Utlity.gson.toJson(fsmsDocumentsModals1)));
            }
        });

    }

    @Override
    public int getItemCount() {
        return fsmsDocumentsModals.size();
    }


    public class Myview extends RecyclerView.ViewHolder {
        FsmsdocumentsitemBinding fsmsdocumentsitemBinding;
        public Myview(FsmsdocumentsitemBinding fsmsdocumentsitemBinding) {
            super(fsmsdocumentsitemBinding.getRoot());
            this.fsmsdocumentsitemBinding=fsmsdocumentsitemBinding;
        }
    }
}
