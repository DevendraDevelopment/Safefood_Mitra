package com.safefoodmitra.safefoodmitra.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.safefoodmitra.safefoodmitra.Apis.RetApis;
import com.safefoodmitra.safefoodmitra.Modals.JobTypeModal;
import com.safefoodmitra.safefoodmitra.Modals.ZoneModals;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.CheckitemBinding;
import com.safefoodmitra.safefoodmitra.databinding.ZonitemBinding;

import java.util.ArrayList;
import java.util.List;


public class CheckAdapter extends RecyclerView.Adapter<CheckAdapter.Myview> {
    LayoutInflater layoutInflater;
    Activity context;
    List<JobTypeModal> jobTypeModals;
    private OnItemClickListener mlistener;

    public interface OnItemClickListener{
        void onItemClick(int position,boolean checked);
    }

    public CheckAdapter(Activity context, List<JobTypeModal> jobTypeModals,OnItemClickListener mlistener) {
        this.context = context;
        this.jobTypeModals = jobTypeModals;
        this.mlistener = mlistener;
    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(parent.getContext());
        View item =layoutInflater.inflate(R.layout.checkitem,parent,false);
        Myview myview= new Myview(item);

        return myview;
    }

    @Override
    public void onBindViewHolder(@NonNull final Myview holder, final int position) {
        final JobTypeModal jobTypeModal = jobTypeModals.get(position);
        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#FAFAFA"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }
        holder.chck.setText(jobTypeModals.get(position).getJobtitle());
        if (!jobTypeModals.get(position).getJobtype_status().equals("0")){
            holder.checkBox.setChecked(true);
        }

    }

    @Override
    public int getItemCount() {
        return jobTypeModals.size();
    }


    public class Myview extends RecyclerView.ViewHolder {
        TextView chck;
        CheckBox checkBox;
        public Myview(@NonNull View itemView) {
            super(itemView);
            chck= (TextView)itemView.findViewById(R.id.type1);
            checkBox= (CheckBox) itemView.findViewById(R.id.check);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mlistener!=null){
                        boolean checked = ((CheckBox) view).isChecked();
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mlistener.onItemClick(position,checked);
                        }
                    }

                }
            });

        }

    }
}

