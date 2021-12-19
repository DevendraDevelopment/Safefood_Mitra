package com.safefoodmitra.safefoodmitra.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.safefoodmitra.safefoodmitra.Modals.Root;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.SearchshowlistBinding;

import java.util.List;

public class SearchShowAdapter extends RecyclerView.Adapter<SearchShowAdapter.Myview> {
    LayoutInflater layoutInflater;
    Context context;
    List<Root> advanceDataModels;


    public SearchShowAdapter(Context context, List<Root> advanceDataModels) {
        this.context = context;
        this.advanceDataModels = advanceDataModels;
    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        SearchshowlistBinding searchshowlistBinding= DataBindingUtil.inflate(layoutInflater, R.layout.searchshowlist,parent,false);
        return new Myview(searchshowlistBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final Myview holder, final int position) {
        Root advanceDataModel = advanceDataModels.get(position);
        if(position %2 == 1)
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        else
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#fafafa"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }

            //holder.searchshowlistBinding.parcate.setText("--" +advanceDataModel.getParcat_name());
         //   holder.searchshowlistBinding.titles.setText(values.get(getItemCount()).getParameter());

    }

    @Override
    public int getItemCount() {
        return advanceDataModels.size();
    }


    public class Myview extends RecyclerView.ViewHolder {
        SearchshowlistBinding searchshowlistBinding;
        public Myview(SearchshowlistBinding searchshowlistBinding) {
            super(searchshowlistBinding.getRoot());
            this.searchshowlistBinding=searchshowlistBinding;
        }
    }
}