package com.safefoodmitra.safefoodmitra.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;

import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;

import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import com.google.gson.reflect.TypeToken;
import com.safefoodmitra.safefoodmitra.Activities.AuditActivity;
import com.safefoodmitra.safefoodmitra.Activities.CleaningMaintenanceActivity;
import com.safefoodmitra.safefoodmitra.Activities.FoodsafetystandardActivity;
import com.safefoodmitra.safefoodmitra.Activities.FsmsdocumentActivity;
import com.safefoodmitra.safefoodmitra.Activities.InspectionActivity;

import com.safefoodmitra.safefoodmitra.Activities.UserMainActivity;
import com.safefoodmitra.safefoodmitra.Apis.ApiClients;
import com.safefoodmitra.safefoodmitra.Apis.RetApis;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;

import com.safefoodmitra.safefoodmitra.Modals.DashboardModals;
import com.safefoodmitra.safefoodmitra.Modals.UnitModals;
import com.safefoodmitra.safefoodmitra.Modals.Validation_custome;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.DashitemBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.Myview> {
    LayoutInflater layoutInflater;
    Context context;
    List<DashboardModals> dashboardModals;
    ArrayList<Validation_custome> fileds;
    SharedPreferences pref;
    String username,userphone,useremail,usercompany,userlocation;
    String respoid1;
    RetApis apiInterface;
    List<UnitModals> unitModals;
    public DashboardAdapter(Context context, List<DashboardModals> dashboardModals,String respoid1) {
        this.context = context;
        this.dashboardModals = dashboardModals;
        this.respoid1 = respoid1;
    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        DashitemBinding dashitemBinding= DataBindingUtil.inflate(layoutInflater, R.layout.dashitem,parent,false);
       // pref = context.getSharedPreferences("MyG9", MODE_PRIVATE);
       // respoid1 = pref.getString("respoid",null);
       /* if (Utlity.is_online((Activity) context)){
            unitdata();
        }
        else {
            Utlity.show_toast((Activity) context,context.getResources().getString(R.string.nointernet));

        }*/
        return new Myview(dashitemBinding);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull final Myview holder, final int position) {
        final DashboardModals dashboardModal=dashboardModals.get(position);
        holder.dashitemBinding.img.setImageResource(dashboardModals.get(position).getImg());
        holder.dashitemBinding.titles.setText(dashboardModals.get(position).getTxt());
        AlphaAnimation buttonClick = new AlphaAnimation(0.0f, 1.0f);
        buttonClick.setDuration(100);


        if (position==0){
            holder.dashitemBinding.iconlinear.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.deshboardbluebackground) );
            if (respoid1!=null){
                if (respoid1.equals("0")){
                    holder.dashitemBinding.subtitles.setVisibility(View.VISIBLE);
                }else {
                    holder.dashitemBinding.subtitles.setVisibility(View.GONE);
                }
            }
            holder.dashitemBinding.dashlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (respoid1!=null){

                            if (respoid1.equals("0")){
                                showDialogBox();
                            }else {
                                view.startAnimation(buttonClick);
                                context.startActivity(new Intent(context, InspectionActivity.class));
                            }
                        }else {
                            view.startAnimation(buttonClick);
                            context.startActivity(new Intent(context, InspectionActivity.class));
                        }
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }




                }
            });
        }
        if (position==1){
            holder.dashitemBinding.iconlinear.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.deshboardredbackground) );
            if (respoid1!=null){
                if (respoid1.equals("0")){
                    holder.dashitemBinding.subtitles.setVisibility(View.VISIBLE);
                }else {
                    holder.dashitemBinding.subtitles.setVisibility(View.GONE);
                }
            }
        }
        if (position==2){
            holder.dashitemBinding.iconlinear.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.deshboardorangebackground) );
            if (respoid1!=null){
                if (respoid1.equals("0")){
                    holder.dashitemBinding.subtitles.setVisibility(View.VISIBLE);
                }else {
                    holder.dashitemBinding.subtitles.setVisibility(View.GONE);
                }
            }
            holder.dashitemBinding.dashlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (respoid1!=null){
                            if (respoid1.equals("0")){
                                showDialogBox();
                            }else {
                                view.startAnimation(buttonClick);
                                context.startActivity(new Intent(context, CleaningMaintenanceActivity.class));
                            }
                        }else {
                            view.startAnimation(buttonClick);
                            context.startActivity(new Intent(context, CleaningMaintenanceActivity.class));
                        }
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }



                }
            });
        }

        if (position==3){
            holder.dashitemBinding.iconlinear.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.deshboardgreenbackground) );
            if (respoid1!=null){
                if (respoid1.equals("0")){
                    holder.dashitemBinding.subtitles.setVisibility(View.VISIBLE);
                }else {
                    holder.dashitemBinding.subtitles.setVisibility(View.GONE);
                }
            }
            holder.dashitemBinding.dashlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (respoid1!=null){
                            if (respoid1.equals("0")){
                                showDialogBox();
                            }else {
                                view.startAnimation(buttonClick);
                                context.startActivity(new Intent(context, FsmsdocumentActivity.class));
                            }
                        }else {
                            view.startAnimation(buttonClick);
                            context.startActivity(new Intent(context, FsmsdocumentActivity.class));
                        }
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }

                }
            });
        }

        if (position==4){
            holder.dashitemBinding.iconlinear.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.deshboardvolietbackground) );
            holder.dashitemBinding.dashlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.startAnimation(buttonClick);
                    context.startActivity(new Intent(context, FoodsafetystandardActivity.class));
                }
            });
        }

        if (position ==5){
            holder.dashitemBinding.iconlinear.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.deshboardpinkbackground) );
            if (respoid1!=null){
                if (respoid1.equals("0")){
                    holder.dashitemBinding.subtitles.setVisibility(View.VISIBLE);
                }else {
                    holder.dashitemBinding.subtitles.setVisibility(View.GONE);
                }
            }
            holder.dashitemBinding.dashlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (respoid1!=null){
                            if (respoid1.equals("0")){
                                showDialogBox();

                            }else {
                                view.startAnimation(buttonClick);
                                context.startActivity(new Intent(context, AuditActivity.class));
                            }
                        }else {
                            view.startAnimation(buttonClick);
                            context.startActivity(new Intent(context, AuditActivity.class));
                        }
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }

                }
            });
        }


    }

    private void showDialogBox() {
        final Dialog dialog = new Dialog(context);
        final ImageButton cancle;
        final TextView save;
        final EditText etname,etphone,etemail,etcompany,etlocation;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.addfobunitlist);
        dialog.getWindow().setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
        cancle = dialog.findViewById(R.id.cancle);
        save = dialog.findViewById(R.id.tvsave);
        etname = dialog.findViewById(R.id.username);
        etphone = dialog.findViewById(R.id.usernumber);
        etemail = dialog.findViewById(R.id.useremail);
        etcompany = dialog.findViewById(R.id.usercompanyname);
        etlocation = dialog.findViewById(R.id.userlocation);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username=etname.getText().toString();
                userphone=etphone.getText().toString();
                useremail=etemail.getText().toString();
                usercompany=etcompany.getText().toString();
                userlocation=etlocation.getText().toString();
                fileds = new ArrayList<>();
                fileds.add(new Validation_custome("text", etname));
                fileds.add(new Validation_custome("text", etphone));
                fileds.add(new Validation_custome("text", etemail));
                fileds.add(new Validation_custome("text", etcompany));
                fileds.add(new Validation_custome("text", etlocation));
                if (Utlity.validation((Activity) context,fileds)) {
                    if (Utlity.isValidMobile(userphone)){
                        if (Utlity.is_online(context)) {
                           saveDaata(username,userphone,useremail,usercompany,userlocation);
                           dialog.cancel();
                        }
                        else {
                            Utlity.show_toast((Activity) context, context.getResources().getString(R.string.nointernet));
                        }

                    } else {
                        Utlity.show_toast((Activity) context,"please enter 10 digit mobile number");

                    }

                }
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void saveDaata(String username, String userphone, String useremail, String usercompany, String userlocation) {
        Utlity.show_progress((Activity) context);
        HashMap<String,String> hmap = new HashMap<String, String>();
        hmap.put("name",username);
        hmap.put("phone",userphone);
        hmap.put("email",useremail);
        hmap.put("company",usercompany);
        hmap.put("location",userlocation);
        apiInterface = ApiClients.getClient1().create(RetApis.class);
        Call<ResponseBody> call=apiInterface.NewUserData("Bearer "+ Utlity.get_user((Activity) context).getToken(),hmap);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Utlity.dismiss_dilog((Activity) context);
                    try {
                        String apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        Intent mIntent = new Intent(context, UserMainActivity.class);
                        context.startActivity(mIntent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    Utlity.show_toast((Activity) context,"responce");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog((Activity) context);

            }
        });
    }

    @Override
    public int getItemCount() {
        return dashboardModals.size();
    }

    public class Myview extends RecyclerView.ViewHolder {
        DashitemBinding dashitemBinding;
        public Myview(DashitemBinding dashitemBinding) {
            super(dashitemBinding.getRoot());
            this.dashitemBinding=dashitemBinding;
        }

    }
   /* public void unitdata(){
        apiInterface = ApiClients.getClient().create(RetApis.class);
        Call<ResponseBody> call=apiInterface.Units("Bearer "+ Utlity.get_user(context).getToken());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        apidata = response.body().string();
                        Log.d("responcedesh>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        unitModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<UnitModals>>() {}.getType());
                        if (unitModals.size()==0){
                            respoid1 = "0";
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }*/
}
