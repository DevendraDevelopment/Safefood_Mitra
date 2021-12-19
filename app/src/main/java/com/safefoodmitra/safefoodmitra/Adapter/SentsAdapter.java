
package com.safefoodmitra.safefoodmitra.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.reflect.TypeToken;
import com.safefoodmitra.safefoodmitra.Activities.FullScreenImage;
import com.safefoodmitra.safefoodmitra.Activities.InspectionDetails;
import com.safefoodmitra.safefoodmitra.Activities.SubfoodsafetystandardActivity;
import com.safefoodmitra.safefoodmitra.Apis.ApiClients;
import com.safefoodmitra.safefoodmitra.Apis.RetApis;
import com.safefoodmitra.safefoodmitra.Fragments.SentFragment;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.FobUnitTypesModals;
import com.safefoodmitra.safefoodmitra.Modals.SentsModals;
import com.safefoodmitra.safefoodmitra.Modals.UnitModals;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.SentsitemBinding;
import com.safefoodmitra.safefoodmitra.databinding.UnititemBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.safefoodmitra.safefoodmitra.Activities.AdminMainActivity.respoid;
import static com.safefoodmitra.safefoodmitra.Activities.InspectionActivity.converid;
import static com.safefoodmitra.safefoodmitra.Activities.InspectionActivity.locatioid;
import static com.safefoodmitra.safefoodmitra.Activities.InspectionActivity.onefromdate;
import static com.safefoodmitra.safefoodmitra.Activities.InspectionActivity.responseid;
import static com.safefoodmitra.safefoodmitra.Activities.InspectionActivity.spinnervalue;
import static com.safefoodmitra.safefoodmitra.Activities.InspectionActivity.tabvalue;
import static com.safefoodmitra.safefoodmitra.Activities.InspectionActivity.twofromdate;
import static com.safefoodmitra.safefoodmitra.Activities.InspectionActivity.valuesall;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.DeleteInspections;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Deletezones;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Editunit;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.inspections;
import static com.safefoodmitra.safefoodmitra.Fragments.SentFragment.norecord;
import static com.safefoodmitra.safefoodmitra.Fragments.SentFragment.sentlist;

public class SentsAdapter extends RecyclerView.Adapter<SentsAdapter.Myview> {
    LayoutInflater layoutInflater;
    Activity context;
    List<SentsModals> sentsModals;
    String imgurl;
    String creadtid,userid,inspectid,respoiduser;
    public SentsAdapter sentsAdapter;

    public SentsAdapter(Activity context, List<SentsModals> sentsModals) {
        this.context = context;
        this.sentsModals = sentsModals;
    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        SentsitemBinding sentsitemBinding= DataBindingUtil.inflate(layoutInflater, R.layout.sentsitem,parent,false);
        return new Myview(sentsitemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final Myview holder, final int position) {
        final SentsModals sentsModal=sentsModals.get(position);
        holder.sentsitemBinding.dtevalue.setText(sentsModals.get(position).getCreated_at());
        holder.sentsitemBinding.status.setText(sentsModals.get(position).getStatus()+":");
        holder.sentsitemBinding.inprocess.setText(sentsModals.get(position).getInprocessdate());
        holder.sentsitemBinding.department.setText(sentsModals.get(position).getDept_name());
        holder.sentsitemBinding.locations.setText(sentsModals.get(position).getLoc_name());
        holder.sentsitemBinding.sendrnme.setText(sentsModals.get(position).getCreated_by_name());
        holder.sentsitemBinding.consarea.setText(sentsModals.get(position).getArea_name());
        holder.sentsitemBinding.comment.setText(sentsModals.get(position).getBefore_comment());
        Utlity.Set_image1(sentsModals.get(position).getBefore_img(),holder.sentsitemBinding.beforimg);
        Utlity.Set_image1(sentsModals.get(position).getAfter_img(),holder.sentsitemBinding.afterimg);
        creadtid=sentsModal.getCreated_by();
        userid=Utlity.get_user(context).getId();
         if (sentsModals.get(position).getClosed_at()!=null){
            holder.sentsitemBinding.linearclose.setVisibility(View.VISIBLE);
             holder.sentsitemBinding.closedate.setText(sentsModals.get(position).getClosed_at());
             holder.sentsitemBinding.closeranme.setText(sentsModals.get(position).getClosed_by_name());
         }else {
             holder.sentsitemBinding.linearclose.setVisibility(View.GONE);
         }
        if (sentsModal.getIns_status().equals("1")){
            if (creadtid.equals(userid)){
                holder.sentsitemBinding.delete.setVisibility(View.VISIBLE);
            }
            else {
                holder.sentsitemBinding.delete.setVisibility(View.GONE);
            }
        }

        else if (sentsModal.getIns_status().equals("2")){
            holder.sentsitemBinding.delete.setVisibility(View.GONE);
        }

        else if (sentsModal.getIns_status().equals("3")){
            holder.sentsitemBinding.delete.setVisibility(View.GONE);
        }
        else {
            holder.sentsitemBinding.delete.setVisibility(View.GONE);
        }


        holder.sentsitemBinding.beforimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgurl=sentsModals.get(position).getBefore_img();
               // bottomdialoge();
                Intent intent= new Intent(context, FullScreenImage.class);
                intent.putExtra("image_url", imgurl);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.sentsitemBinding.afterimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgurl=sentsModals.get(position).getAfter_img();
               // bottomdialoge();
                Intent intent= new Intent(context, FullScreenImage.class);
                intent.putExtra("image_url", imgurl);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.sentsitemBinding.detailayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if (tabvalue==0){
                    if (valuesall==0){

                    }
                    else if (valuesall==1){
                        context.startActivity(new Intent(context, InspectionDetails.class).putExtra("details",Utlity.gson.toJson(sentsModal)));

                    }
                }*/
                context.startActivity(new Intent(context, InspectionDetails.class).putExtra("details",Utlity.gson.toJson(sentsModal)));


            }
        });

        holder.sentsitemBinding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inspectid=sentsModals.get(position).getId();
                bottomdialoge();
            }
        });


    }

    @Override
    public int getItemCount() {
        return sentsModals.size();
    }

    public void bottomdialoge(){
        BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(context);
        final ImageButton cancle;
        final TextView deletes;
        bottomSheetDialog.setContentView(R.layout.deleteconfirm);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        cancle=bottomSheetDialog.findViewById(R.id.cancle);
        deletes=bottomSheetDialog.findViewById(R.id.deletes);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });

        deletes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletinspection();
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();
    }

    public class Myview extends RecyclerView.ViewHolder {
        SentsitemBinding sentsitemBinding;
        public Myview(SentsitemBinding sentsitemBinding) {
            super(sentsitemBinding.getRoot());
            this.sentsitemBinding=sentsitemBinding;
        }
    }

    private void deletinspection() {
        Utlity.show_progress(context);
        Request result= get( context,DeleteInspections+inspectid);
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Utlity.dismiss_dilog((Activity) context);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Utlity.dismiss_dilog((Activity) context);
                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            if (response.isSuccessful()){
                                sentsModals.remove(inspectid);
                                Utlity.show_toast((Activity) context,object.getInt("success")+" Delete Sucessfully");
                                //inspectionsdata();
                                if (Utlity.get_user(context).getUserroles_id().equalsIgnoreCase("2")){
                                    inspectionsdataadmin();
                                }
                                else if (Utlity.get_user(context).getUserroles_id().equalsIgnoreCase("3")){
                                    inspectionsdatauser();
                                }
                            }
                            else {
                                Utlity.show_toast((Activity) context,"Unauthorised");
                            }
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

        });

    }

    public Request get(Activity activity, String api_name) {
        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        return new Request.Builder()
                .url(api_name)
                .get()
                .header("Authorization","Bearer "+ Utlity.get_user((Activity) context).getToken())
                .build();
    }

    private void inspectionsdata() {
        Utlity.show_progress(context);
      // String respoiduser=Utlity.get_user(context).getFobunits_id();
        Request result= get(inspections+respoid+"/"+"1");
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Utlity.dismiss_dilog(context);
            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {

                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            String apidata = null;
                            try {
                                Utlity.dismiss_dilog(context);
                                apidata = response.body().string();
                                Log.d("responce>>>>>",apidata);
                                JSONObject object = new JSONObject(apidata);
                                sentsModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SentsModals>>() {}.getType());
                                if(sentsModals.size()!=0) {
                                    norecord.setVisibility(View.GONE);
                                    sentlist.setVisibility(View.VISIBLE);
                                    sentlist.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                                    sentsAdapter = new SentsAdapter(context, sentsModals);
                                    sentlist.setAdapter(sentsAdapter);
                                }
                                else
                                {
                                    norecord.setVisibility(View.VISIBLE);
                                    sentlist.setVisibility(View.GONE);
                                }

                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });

            }

        });

    }
    public Request get(String api_name) {
        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        return new Request.Builder()
                .url(api_name)
                .get()
                .header("Authorization","Bearer "+ Utlity.get_user((Activity) context).getToken())
                .build();
    }
    private void inspectionsdataadmin() {
        Utlity.show_progress(context);
        Request result= get(inspections+respoid+"/"+"1");
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Utlity.dismiss_dilog(context);
                try {
                    // Utlity.show_toast(getActivity(), "Not Founded");
                }catch (RuntimeException e1){
                    e1.printStackTrace();
                }

            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {

                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            String apidata = null;
                            try {
                                Utlity.dismiss_dilog(context);
                                apidata = response.body().string();
                                Log.d("responce>>>>>",apidata);
                                JSONObject object = new JSONObject(apidata);
                                sentsModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SentsModals>>() {}.getType());
                                if(sentsModals.size()!=0) {
                                    norecord.setVisibility(View.GONE);
                                    sentlist.setVisibility(View.VISIBLE);
                                    sentlist.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                                    sentsAdapter = new SentsAdapter(context, sentsModals);
                                    sentlist.setAdapter(sentsAdapter);
                                }
                                else
                                {
                                    norecord.setVisibility(View.VISIBLE);
                                    sentlist.setVisibility(View.GONE);
                                }

                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });

            }

        });

    }
    private void inspectionsdatauser() {
        Utlity.show_progress(context);
        respoiduser=Utlity.get_user(context).getFobunits_id();
        Request result= get(inspections+respoiduser+"/"+"1");
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Utlity.dismiss_dilog(context);
                try {
                    Utlity.show_toast(context, "Not Founded");
                }catch (RuntimeException e1){
                    e1.printStackTrace();
                }

            }

            @Override
            public void onResponse(okhttp3.Call call, final Response response) throws IOException {

                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            String apidata = null;
                            try {
                                Utlity.dismiss_dilog(context);
                                apidata = response.body().string();
                                Log.d("responce>>>>>",apidata);
                                JSONObject object = new JSONObject(apidata);
                                sentsModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<SentsModals>>() {}.getType());
                                if(sentsModals.size()!=0) {
                                    norecord.setVisibility(View.GONE);
                                    sentlist.setVisibility(View.VISIBLE);
                                    sentlist.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                                    sentsAdapter = new SentsAdapter(context, sentsModals);
                                    sentlist.setAdapter(sentsAdapter);
                                }
                                else
                                {
                                    norecord.setVisibility(View.VISIBLE);
                                    sentlist.setVisibility(View.GONE);
                                }

                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });

            }

        });

    }
}
