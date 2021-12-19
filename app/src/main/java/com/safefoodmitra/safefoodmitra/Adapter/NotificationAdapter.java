
package com.safefoodmitra.safefoodmitra.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.safefoodmitra.safefoodmitra.Activities.PdfActivity;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.Notification;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.Room.TaskSync;
import com.safefoodmitra.safefoodmitra.databinding.AuditsitemBinding;
import com.safefoodmitra.safefoodmitra.databinding.NotificationitemBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*public class NotificationAdapter extends FirebaseRecyclerAdapter<Notification,NotificationAdapter.Myview> {
    LayoutInflater layoutInflater;
    OnItemClickListener listener;

    public NotificationAdapter(@NonNull FirebaseRecyclerOptions<Notification> options) {
        super(options);
    }


    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        NotificationitemBinding notificationitemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.notificationitem,parent,false);
        return new Myview(notificationitemBinding);
    }

    @Override
    protected void onBindViewHolder(@NonNull Myview holder, int position, @NonNull Notification model) {
        holder.notificationitemBinding.notificatontitle.setText(model.getTitle());
        holder.notificationitemBinding.notificationmessage.setText(model.getMessage());

        holder.notificationitemBinding.notificationtime.setText(Utlity.getDays(model.getDate()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(model);

            }
        });
    }

    public class Myview extends RecyclerView.ViewHolder {
        NotificationitemBinding notificationitemBinding;
        public Myview(NotificationitemBinding notificationitemBinding) {
            super(notificationitemBinding.getRoot());
            this.notificationitemBinding=notificationitemBinding;
        }
    }
    public interface OnItemClickListener {
        void onItemClick(Notification item);
    }
}*/
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.Myview> {
    LayoutInflater layoutInflater;
    Activity context;
    List<TaskSync> taskList;
    OnItemClickListener listener;
    public NotificationAdapter(Activity context,List<TaskSync> taskList,OnItemClickListener listener) {
        this.context = context;
        this.taskList = taskList;
        this.listener = listener;
    }


    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        NotificationitemBinding notificationitemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.notificationitem,parent,false);
        return new Myview(notificationitemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final Myview holder, final int position) {
      final TaskSync taskSync = taskList.get(position);
      holder.notificationitemBinding.notificatontitle.setText(taskList.get(position).getTitle());
      holder.notificationitemBinding.notificationmessage.setText(taskList.get(position).getMessage());
      holder.notificationitemBinding.notificationtime.setText(Utlity.getDays(taskList.get(position).getDate()));
      holder.itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              listener.onItemClick(taskList.get(position));
            /*  if (taskList.get(position).getPdflink() != null) {
                  context.startActivity(new Intent(context, PdfActivity.class)
                          .putExtra("pdflink", taskList.get(position).getPdflink())
                          .putExtra("pdftitle",taskList.get(position).getTitle()));
              }*/
          }
      });
/*if (tripePosition.contains(position)){
            holder.notificationitemBinding.linearnotificaton.setBackgroundColor(ContextCompat.getColor(context, R.color.colorlightorange));
        }*/




    }

    @Override
    public int getItemCount() {

        return taskList.size();
    }



    public class Myview extends RecyclerView.ViewHolder {
        NotificationitemBinding notificationitemBinding;
        public Myview(NotificationitemBinding notificationitemBinding) {
            super(notificationitemBinding.getRoot());
            this.notificationitemBinding=notificationitemBinding;
        }
    }
    public interface OnItemClickListener {
        void onItemClick(TaskSync item);
    }
}
