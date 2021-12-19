package com.safefoodmitra.safefoodmitra.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.reflect.TypeToken;
import com.safefoodmitra.safefoodmitra.Activities.EqupmentActivity;
import com.safefoodmitra.safefoodmitra.Apis.ApiClients;
import com.safefoodmitra.safefoodmitra.Apis.RetApis;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.EqupmentModals;
import com.safefoodmitra.safefoodmitra.Modals.ZoneModals;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.EqupmentitemBinding;

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
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Deletedepartment;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Deletequpment;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Deletezones;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Editequpment;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Editzones;

public class EqupmentAdapter extends RecyclerView.Adapter<EqupmentAdapter.Myview> implements Filterable {
    LayoutInflater layoutInflater;
    Activity context;
    List<EqupmentModals> equpmentModals;
    List<EqupmentModals> equpmentModals2;
    RetApis apiInterface;
    public EqupmentAdapter equpmentAdapter;

    String urlid,equpmentnme;
    EditText equpmentnmes;
    public EqupmentAdapter(Activity context, List<EqupmentModals> equpmentModals) {
        this.context = context;
        this.equpmentModals = equpmentModals;
        this.equpmentModals2 = new ArrayList<>(equpmentModals);
    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        EqupmentitemBinding equpmentitemBinding= DataBindingUtil.inflate(layoutInflater, R.layout.equpmentitem,parent,false);
        return new Myview(equpmentitemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final Myview holder, final int position) {
        final EqupmentModals equpmentModal=equpmentModals.get(position);
        if(position %2 == 1)
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        else
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#FAFAFA"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }
        holder.equpmentitemBinding.titles.setText(equpmentModals.get(position).getEquip_name());

        holder.equpmentitemBinding.editequpment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlid=equpmentModal.getId();
                equpmentnme=equpmentModal.getEquip_name();
                zoneAleart();

            }
        });

        holder.equpmentitemBinding.deletequpment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlid=equpmentModal.getId();
                bottomdialoge();
            }
        });
    }

    @Override
    public int getItemCount() {
        return equpmentModals.size();
    }

    @Override
    public Filter getFilter() {
        return selectfilter;
    }
    private Filter selectfilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<EqupmentModals> filterdlist = new ArrayList<>();

            if (constraint==null || constraint.length() ==0){
                filterdlist.addAll(equpmentModals2);

            }
            else {
                for (EqupmentModals zoneModals:equpmentModals2){
                    String textfilter= zoneModals.getEquip_name().toLowerCase();
                    String idfilter= zoneModals.getId().toLowerCase();
                    if(textfilter.contains(constraint.toString().toLowerCase())){
                        filterdlist.add(zoneModals);
                    }
                    else if (idfilter.contains(constraint.toString().toLowerCase())){
                        filterdlist.add(zoneModals);
                    }
                  /*  if (zoneModals.getZone_name().equals(filterpattern)||zoneModals.getId().equals(filterpattern)){
                        filterdlist.add(zoneModals);
                    }

                   */


                }
            }
            FilterResults results = new FilterResults();
            results.values = filterdlist;
            return  results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            equpmentModals.clear();
            equpmentModals.addAll((ArrayList) results.values);
            notifyDataSetChanged();


        }
    };

    public void zoneAleart() {
        final Dialog dialog = new Dialog(context);
        final ImageButton cancle;
        final TextView save;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.editequpment);
        dialog.getWindow().setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
        equpmentnmes=dialog.findViewById(R.id.editequpmentnme);
        save=dialog.findViewById(R.id.editequpment);
        cancle=dialog.findViewById(R.id.cancle);
        equpmentnmes.setText(equpmentnme);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equpmentnme=equpmentnmes.getText().toString();
                if (Utlity.is_online(context)){
                    addequpment(equpmentnme);
                    dialog.dismiss();
                }
                else {
                    Utlity.show_toast((Activity) context, context.getResources().getString(R.string.nointernet));
                }

            }

        });
        dialog.show();
    }
    public void bottomdialoge(){
        BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(context);
        final ImageButton cancle;
        final TextView delete;
        bottomSheetDialog.setContentView(R.layout.deleteconfirm);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        delete=bottomSheetDialog.findViewById(R.id.deletes);
        cancle=bottomSheetDialog.findViewById(R.id.cancle);

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utlity.is_online(context)){
                    deletzone();
                    bottomSheetDialog.dismiss();
                }
                else {
                    Utlity.show_toast((Activity) context, context.getResources().getString(R.string.nointernet));
                }

            }

        });
        bottomSheetDialog.show();
    }

    private void addequpment(final String equpnme) {
        Utlity.show_progress(context);
        HashMap<String, String> keys = new HashMap<>();
        keys.put("fobunits_id", respoid);
        keys.put("equip_name", equpnme);
        Request result= post( keys, Editequpment+urlid);
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
                                Utlity.show_toast((Activity) context,object.getInt("success")+" Update Sucessfully");
                                equpmenttdata();

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

    private void deletzone() {
        Utlity.show_progress(context);
        HashMap<String, String> keys = new HashMap<>();
        keys.put("fobunits_id", "1");
        Request result= get( context,Deletequpment+urlid);
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
                                equpmentModals.remove(urlid);
                                Utlity.show_toast((Activity) context,object.getInt("success")+" Delete Sucessfully");
                                equpmenttdata();
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
    private void equpmenttdata() {
        Utlity.show_progress(context);
        apiInterface = ApiClients.getClient().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Equpments(respoid,"Bearer "+ Utlity.get_user(context).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        Utlity.dismiss_dilog(context);
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        equpmentModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<EqupmentModals>>() {}.getType());
                        if(equpmentModals.size()!=0) {
                            EqupmentActivity.recyclerView.setHasFixedSize(true);
                            EqupmentActivity.recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL,false));
                            equpmentAdapter = new EqupmentAdapter(context, equpmentModals);
                            EqupmentActivity.recyclerView.setAdapter(equpmentAdapter);
                        }
                        else
                        {
                            EqupmentActivity.norecord.setVisibility(View.VISIBLE);
                            EqupmentActivity.recyclerView.setVisibility(View.GONE);
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog(context);

            }
        });

    }


    public Request post(HashMap<String, String> keys, String api_name) {
        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        FormBody.Builder body = new FormBody.Builder();
        for (Object key : keys.keySet()) {
            String value = keys.get(key);
            if(!TextUtils.isEmpty(value)) {
                body.add(key.toString(), value);
            }
            else {
                body.add(key.toString(), "");
            }
        }
        RequestBody parmetrs = body.build();
        return new Request.Builder()
                .url(api_name)
                .header("Authorization","Bearer "+ Utlity.get_user((Activity) context).getToken())
                .post(parmetrs)
                .build();
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

    public class Myview extends RecyclerView.ViewHolder {
        EqupmentitemBinding equpmentitemBinding;
        public Myview(EqupmentitemBinding equpmentitemBinding) {
            super(equpmentitemBinding.getRoot());
            this.equpmentitemBinding=equpmentitemBinding;
        }
    }
}
