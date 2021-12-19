package com.safefoodmitra.safefoodmitra.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.safefoodmitra.safefoodmitra.Modals.InspectrespoModals;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.DepartitemBinding;

import java.util.List;

public class RecordAdapter1 extends RecyclerView.Adapter<RecordAdapter1.Myview> {
    LayoutInflater layoutInflater;
    Activity context;
    List<InspectrespoModals> inspectrespoModals;
    RecordAdapter2  recordAdapter2;

    public RecordAdapter1(Activity context, List<InspectrespoModals> inspectrespoModals) {
        this.context = context;
        this.inspectrespoModals = inspectrespoModals;

    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        DepartitemBinding departitemBinding= DataBindingUtil.inflate(layoutInflater, R.layout.departitem,parent,false);
        return new Myview(departitemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final Myview holder, final int position) {
        final InspectrespoModals inspectrespoModal=inspectrespoModals.get(position);

        holder.departitemBinding.dptnme.setText(inspectrespoModals.get(position).getDept_name());
        recordAdapter2 = new RecordAdapter2(context,inspectrespoModal.getInspectrespoModals2s());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        holder.departitemBinding.secondlist.setLayoutManager(layoutManager);
        holder.departitemBinding.secondlist.setAdapter(recordAdapter2);

    }

    @Override
    public int getItemCount() {
        return inspectrespoModals.size();
    }


    public class Myview extends RecyclerView.ViewHolder {
        DepartitemBinding departitemBinding;
        public Myview(DepartitemBinding departitemBinding) {
            super(departitemBinding.getRoot());
            this.departitemBinding=departitemBinding;
        }
    }

    public void setCacheMenuRes(List<InspectrespoModals> inspectrespoModals)
    {
        this.inspectrespoModals = inspectrespoModals;
        notifyDataSetChanged();
    }
}
