
package com.safefoodmitra.safefoodmitra.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.reflect.TypeToken;
import com.safefoodmitra.safefoodmitra.Activities.FullScreenImage;
import com.safefoodmitra.safefoodmitra.Activities.InspectionDetails;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.SentsModals;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.AuditsitemBinding;
import com.safefoodmitra.safefoodmitra.databinding.SentsitemBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.safefoodmitra.safefoodmitra.Activities.AdminMainActivity.respoid;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.DeleteInspections;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.inspections;
import static com.safefoodmitra.safefoodmitra.Fragments.SentFragment.norecord;
import static com.safefoodmitra.safefoodmitra.Fragments.SentFragment.sentlist;

public class AuditAdapter extends RecyclerView.Adapter<AuditAdapter.Myview> {
    LayoutInflater layoutInflater;
    Activity context;



    public AuditAdapter(Activity context) {
        this.context = context;

    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        AuditsitemBinding auditsitemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.auditsitem,parent,false);
        return new Myview(auditsitemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final Myview holder, final int position) {


    }

    @Override
    public int getItemCount() {
        return 4;
    }



    public class Myview extends RecyclerView.ViewHolder {
        AuditsitemBinding auditsitemBinding;
        public Myview(AuditsitemBinding auditsitemBinding) {
            super(auditsitemBinding.getRoot());
            this.auditsitemBinding=auditsitemBinding;
        }
    }

}
