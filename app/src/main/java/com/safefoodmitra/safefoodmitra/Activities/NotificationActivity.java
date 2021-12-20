package com.safefoodmitra.safefoodmitra.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.safefoodmitra.safefoodmitra.Adapter.NotificationAdapter;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.Notification;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.Room.DatabaseClient;
import com.safefoodmitra.safefoodmitra.Room.TaskSync;
import com.safefoodmitra.safefoodmitra.databinding.ActivityNotificationBinding;

import java.util.ArrayList;
import java.util.List;


import static com.safefoodmitra.safefoodmitra.Fragments.SentFragment.sentlist;
import static com.safefoodmitra.safefoodmitra.MessagingService.MyFirebaseMessagingService.NOTIFICATION_CHANNEL_ID;


public class NotificationActivity extends AppCompatActivity implements View.OnClickListener, NotificationAdapter.OnItemClickListener {
    ActivityNotificationBinding notificationBinding;
    NotificationAdapter notificationAdapter;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationBinding = DataBindingUtil.setContentView(this, R.layout.activity_notification);
        LinearLayoutManager layoutManager = new LinearLayoutManager(NotificationActivity.this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        notificationBinding.notificationlist.setLayoutManager(layoutManager);
       // NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
       // notificationManager.cancelAll();
        click();
        Utlity.show_progress(NotificationActivity.this);
        class GetTasks extends AsyncTask<Void, Void, List<TaskSync>> {

            @Override
            protected List<TaskSync> doInBackground(Void... voids) {

                List<TaskSync> taskList = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().taskDao().getAll();
                return taskList;
            }

            @SuppressLint("SetTextI18n")
            @Override
            protected void onPostExecute(List<TaskSync> taskSyncs) {
                super.onPostExecute(taskSyncs);
                if (taskSyncs.size() != 0) {
                    Utlity.dismiss_dilog(NotificationActivity.this);
                    notificationBinding.notificationlinear.setVisibility(View.GONE);
                    notificationBinding.notificationlist.setVisibility(View.VISIBLE);
                    // notificationBinding.notificationlist.setLayoutManager(new LinearLayoutManager(NotificationActivity.this, RecyclerView.VERTICAL, false));
                    notificationAdapter = new NotificationAdapter(NotificationActivity.this,taskSyncs,NotificationActivity.this);
                    notificationBinding.notificationlist.setAdapter(notificationAdapter);
                    notificationAdapter.notifyDataSetChanged();
                }else {
                    Utlity.dismiss_dilog(NotificationActivity.this);
                    notificationBinding.notificationlinear.setVisibility(View.VISIBLE);
                    notificationBinding.notificationlist.setVisibility(View.GONE);
                }

            }


        }
        GetTasks gt = new GetTasks();
        gt.execute();
    }

    private void click() {
        notificationBinding.back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back) {

            if (Utlity.get_user(this).getUserroles_id()==null) {
               startActivity(new Intent(this, LoginActivity.class));
               finishAffinity();
            }
            else if (Utlity.get_user(this).getUserroles_id().equalsIgnoreCase("2")){
                startActivity(new Intent(this, AdminMainActivity.class));
                finishAffinity();

            }
            else if (Utlity.get_user(this).getUserroles_id().equalsIgnoreCase("3")){
                startActivity(new Intent(this, UserMainActivity.class));
                finishAffinity();

            }

            else {
                if (doubleBackToExitPressedOnce) {
                    onBackPressed();
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }

            /*if (Utlity.get_user(this).getUserroles_id().equalsIgnoreCase("2")) {
                onBackPressed();

              *//*  startActivity(new Intent(this, AdminMainActivity.class));
                finishAffinity();*//*

            } else if (Utlity.get_user(this).getUserroles_id().equalsIgnoreCase("3")) {
                onBackPressed();

                *//*startActivity(new Intent(this, UserMainActivity.class));
                finishAffinity();*//*

            }*/
        }
    }

    @Override
    public void onBackPressed() {
        if (Utlity.get_user(this).getUserroles_id()==null) {
            startActivity(new Intent(this, LoginActivity.class));
            finishAffinity();
        }
        else if (Utlity.get_user(this).getUserroles_id().equalsIgnoreCase("2")){
            startActivity(new Intent(this, AdminMainActivity.class));
            finishAffinity();

        }
        else if (Utlity.get_user(this).getUserroles_id().equalsIgnoreCase("3")){
            startActivity(new Intent(this, UserMainActivity.class));
            finishAffinity();

        }

        else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
        /*if (Utlity.get_user(this).getUserroles_id().equalsIgnoreCase("2")) {
            super.onBackPressed();

            //startActivity(new Intent(this, AdminMainActivity.class));
            //finishAffinity();

        } else if (Utlity.get_user(this).getUserroles_id().equalsIgnoreCase("3")) {
            super.onBackPressed();

            //startActivity(new Intent(this, UserMainActivity.class));
            //finishAffinity();

        }*/
    }

    @Override
    public void onItemClick(TaskSync item) {
       // if (item.getLinktype().equals("pdf")) {
            startActivity(new Intent(this, PdfActivity.class)
                    .putExtra("pdflink", item.getPdflink())
                     .putExtra("pdftitle",item.getTitle()));
       // }
        /*else {
            openWebPage(item.getPdflink());
        }*/
    }
    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


   /* @Override
    protected void onStart() {
        super.onStart();
        notificationAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        notificationAdapter.stopListening();
    }*/
}