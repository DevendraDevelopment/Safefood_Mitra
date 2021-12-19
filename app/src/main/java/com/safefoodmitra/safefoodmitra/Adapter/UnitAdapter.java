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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.reflect.TypeToken;
import com.safefoodmitra.safefoodmitra.Activities.UnitsActivity;
import com.safefoodmitra.safefoodmitra.Apis.ApiClients;
import com.safefoodmitra.safefoodmitra.Apis.RetApis;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.FobUnitTypesModals;
import com.safefoodmitra.safefoodmitra.Modals.UnitModals;
import com.safefoodmitra.safefoodmitra.Modals.Validation_custome;
import com.safefoodmitra.safefoodmitra.Modals.ZoneModals;
import com.safefoodmitra.safefoodmitra.R;
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
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Addfobunits;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Deleterecord;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Deleteunit;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Editunit;

public class UnitAdapter extends RecyclerView.Adapter<UnitAdapter.Myview> implements Filterable {
    LayoutInflater layoutInflater;
    Activity context;
    List<UnitModals> unitModals;
    List<UnitModals> unitModals2;
    public List<FobUnitTypesModals> fobUnitTypesModals;
    ArrayList<String> fobpart;
    Spinner fobunits;
    EditText unitname,citynme,stanme,zipcode;
    String fobtypeid,unitnme,citynmes,stnme,zpcode,urlid;
    RetApis apiInterface;
    public UnitAdapter unitAdapter;

    public UnitAdapter(Activity context, List<UnitModals> unitModals) {
        this.context = context;
        this.unitModals = unitModals;
        this.unitModals2 = new ArrayList<>(unitModals);
    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        UnititemBinding unititemBinding= DataBindingUtil.inflate(layoutInflater, R.layout.unititem,parent,false);
        return new Myview(unititemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final Myview holder, final int position) {
        final UnitModals unitModal=unitModals.get(position);
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
        holder.unititemBinding.titles.setText(unitModals.get(position).getUnit_name());
       // holder.unititemBinding.date.setText(unitModals.get(position).getCreated_at());
        holder.unititemBinding.editunit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlid=unitModal.getId();
                unitnme=unitModal.getUnit_name();
               /* citynmes=unitModal.getId();
                stnme=unitModal.getId();
                zpcode=unitModal.getId();

                */
                fobtypeid=unitModal.getFobtype();
                unitAleart();

            }
        });

        holder.unititemBinding.deleteunit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlid=unitModal.getId();
                bottomdialoge();
            }
        });

    }

    @Override
    public int getItemCount() {
        return unitModals.size();
    }

    @Override
    public Filter getFilter() {
        return selectfilter;
    }
    private Filter selectfilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<UnitModals> filterdlist = new ArrayList<>();

            if (constraint==null || constraint.length() ==0){
                filterdlist.addAll(unitModals2);

            }
            else {
                for (UnitModals zoneModals:unitModals2){
                    String textfilter= zoneModals.getUnit_name().toLowerCase();
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
            unitModals.clear();
            unitModals.addAll((ArrayList) results.values);
            notifyDataSetChanged();


        }
    };

    public void unitAleart() {
        final Dialog dialog = new Dialog(context);
        final TextView save;
        final ImageButton cancle;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.editunit);
        dialog.getWindow().setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
        unitname=dialog.findViewById(R.id.unitnme);
        citynme=dialog.findViewById(R.id.citynme);
        stanme=dialog.findViewById(R.id.statenme);
        zipcode=dialog.findViewById(R.id.zipcode);
        save=dialog.findViewById(R.id.editunits);
        cancle=dialog.findViewById(R.id.cancle);
        fobunits=dialog.findViewById(R.id.fobtype);
        unitname.setText(unitnme);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        fobunits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0){
                    fobtypeid=fobUnitTypesModals.get(position-1).getId();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unitnme=unitname.getText().toString();
                citynmes=citynme.getText().toString();
                stnme=stanme.getText().toString();
                zpcode=zipcode.getText().toString();

                if (Utlity.is_online(context)){
                    editunits();
                    dialog.dismiss();
                }
                else {
                    Utlity.show_toast(context,context.getResources().getString(R.string.nointernet));
                }

            }
        });

        dialog.show();
        fobtypes();
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
                    deletunit();
                    bottomSheetDialog.dismiss();
                }
                else {
                    Utlity.show_toast((Activity) context, context.getResources().getString(R.string.nointernet));
                }

            }

        });
        bottomSheetDialog.show();
    }

    private void fobtypes() {
        Utlity.show_progress(context);
        apiInterface = ApiClients.getClient().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.FobType("Bearer "+ Utlity.get_user(context).getToken());
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
                        fobUnitTypesModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<FobUnitTypesModals>>() {}.getType());
                        fobpart=new ArrayList<>();
                        fobpart.add(fobtypeid);
                        for (FobUnitTypesModals fobUnitTypesModal:fobUnitTypesModals){
                            fobpart.add(fobUnitTypesModal.getFobtype());
                        }

                        ArrayAdapter<String> adapter1 =
                                new ArrayAdapter<String>(context,  R.layout.spinneritem, fobpart);
                        adapter1.setDropDownViewResource( R.layout.spinneritem);
                        fobunits.setAdapter(adapter1);


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog(context);
                Utlity.show_toast(context, "Not found");

            }
        });

    }

    private void editunits(){
        Utlity.show_progress(context);
        HashMap<String, String> keys = new HashMap<>();
        keys.put("fobtypes_id", fobtypeid);
        keys.put("unit_name", unitnme);
        keys.put("city_name", citynmes);
        keys.put("state_name", stnme);
        keys.put("zipcode", zpcode);
        Request result= post( keys, Editunit+urlid);
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
                        try {
                            Utlity.dismiss_dilog(context);
                            String apidata = response.body().string();
                            Log.d("responce>>>>>",apidata);
                            JSONObject object = new JSONObject(apidata);
                            if (response.isSuccessful()){
                                Utlity.show_toast((Activity) context,object.getInt("success")+" Update Sucessfully");
                                unitdata();
                            }
                            else {
                                Utlity.show_toast(context,"Unauthorised");
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
    private void deletunit() {
        Utlity.show_progress(context);
        HashMap<String, String> keys = new HashMap<>();
        keys.put("fobunits_id", "1");
        Request result= get( context,Deleteunit+urlid);
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
                                unitModals.remove(urlid);
                                Utlity.show_toast((Activity) context,object.getInt("success")+" Delete Sucessfully");
                                unitdata();
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

    private void unitdata() {
        Utlity.show_progress(context);
        apiInterface = ApiClients.getClient().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Units("Bearer "+ Utlity.get_user(context).getToken());
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
                        unitModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<UnitModals>>() {}.getType());

                        if(unitModals.size()!=0) {
                            UnitsActivity.norecord.setVisibility(View.GONE);
                            UnitsActivity.recyclerView.setVisibility(View.VISIBLE);
                            UnitsActivity.recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                            unitAdapter = new UnitAdapter(context, unitModals);
                            UnitsActivity.recyclerView.setAdapter(unitAdapter);
                        }
                        else
                        {
                            UnitsActivity.norecord.setVisibility(View.VISIBLE);
                            UnitsActivity.recyclerView.setVisibility(View.GONE);
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
                .header("Authorization","Bearer "+ Utlity.get_user(context).getToken())
                .post(parmetrs)
                .build();
    }


    public class Myview extends RecyclerView.ViewHolder {
        UnititemBinding unititemBinding;
        public Myview(UnititemBinding unititemBinding) {
            super(unititemBinding.getRoot());
            this.unititemBinding=unititemBinding;
        }
    }
}
