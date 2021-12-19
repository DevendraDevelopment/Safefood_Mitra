
package com.safefoodmitra.safefoodmitra.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.safefoodmitra.safefoodmitra.Activities.InternalExternalDetail;
import com.safefoodmitra.safefoodmitra.Activities.NewAudit;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.AuditsitemBinding;
import com.safefoodmitra.safefoodmitra.databinding.InternallistBinding;

public class InternalAdapter extends RecyclerView.Adapter<InternalAdapter.Myview> {
    LayoutInflater layoutInflater;
    Activity context;



    public InternalAdapter(Activity context) {
        this.context = context;

    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        InternallistBinding internallistBinding = DataBindingUtil.inflate(layoutInflater, R.layout.internallist,parent,false);
        return new Myview(internallistBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final Myview holder, final int position) {
     holder.itemView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             context.startActivity(new Intent(context, NewAudit.class));
         }
     });

    }

    @Override
    public int getItemCount() {
        return 8;
    }



    public class Myview extends RecyclerView.ViewHolder {
        InternallistBinding internallistBinding;
        public Myview(InternallistBinding internallistBinding) {
            super(internallistBinding.getRoot());
            this.internallistBinding=internallistBinding;
        }
    }

}
