package com.safefoodmitra.safefoodmitra.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.reflect.TypeToken;
import com.safefoodmitra.safefoodmitra.Activities.InspectionActivity;
import com.safefoodmitra.safefoodmitra.Activities.RecordsActivity;
import com.safefoodmitra.safefoodmitra.Apis.ApiClients;
import com.safefoodmitra.safefoodmitra.Apis.RetApis;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.DepartItem;
import com.safefoodmitra.safefoodmitra.Modals.Item;
import com.safefoodmitra.safefoodmitra.Modals.ListItem;
import com.safefoodmitra.safefoodmitra.Modals.LocationModals;
import com.safefoodmitra.safefoodmitra.Modals.RecordModals;
import com.safefoodmitra.safefoodmitra.Modals.RespoModal;
import com.safefoodmitra.safefoodmitra.Modals.UnitModals;
import com.safefoodmitra.safefoodmitra.Modals.ZoneModals;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.ZonitemBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.content.Context.MODE_PRIVATE;
import static com.safefoodmitra.safefoodmitra.Activities.AdminMainActivity.respoid;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Deletequpment;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Deleterecord;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Editequpment;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Editrecord;

public class RecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    LayoutInflater layoutInflater;
    Activity context;
    List<RecordModals> recordModals2;
    String urlid,recordnme,dptid,dptnme;
    EditText editrecords;
    RetApis apiInterface;
    List<RespoModal>respoModals;
    ArrayList<String> fobpart;
    Spinner departments;
    List<ListItem> consolidatedList;
    List<RecordModals> recordModals;
    SharedPreferences pref;
    View v1;

    public RecordAdapter(Activity context, List<ListItem> consolidatedList,List<RecordModals> recordModals) {
        this.context = context;
        this.consolidatedList = consolidatedList;
        this.recordModals = recordModals;
        this.recordModals2 = new ArrayList(recordModals);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       /* if (layoutInflater==null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ZonitemBinding zonitemBinding= DataBindingUtil.inflate(layoutInflater, R.layout.zonitem,parent,false);
        return new Myview(zonitemBinding);

        */

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {

            case ListItem.TYPE_GENERAL:
                v1 = inflater.inflate(R.layout.recorditem, parent, false);
                viewHolder = new GeneralViewHolder(v1);
                break;

            case ListItem.TYPE_DEPART:
                View v2 = inflater.inflate(R.layout.datitem, parent, false);
                viewHolder = new DateViewHolder(v2);
                break;
        }
        pref = context.getSharedPreferences("MyG9", MODE_PRIVATE);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case ListItem.TYPE_GENERAL:
                Item item  = (Item)consolidatedList.get(position);
                GeneralViewHolder generalViewHolder= (GeneralViewHolder) holder;
                   if (((Item) consolidatedList.get(position)).getPojoOfJsonArray().getId().equals("0")){
                       try {
                           generalViewHolder.gonelayout.setVisibility(View.GONE);
                           generalViewHolder.gonelayout.removeViewAt(0);
                       }catch (NullPointerException e){
                           e.printStackTrace();
                       }


                }
                else {
                       generalViewHolder.gonelayout.setVisibility(View.VISIBLE);
                       generalViewHolder.txtTitle.setText(item.getPojoOfJsonArray().getRecord_name());
                }

                generalViewHolder.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        urlid=item.getPojoOfJsonArray().getId();
                        dptid=item.getPojoOfJsonArray().getId();
                        recordnme=item.getPojoOfJsonArray().getRecord_name();
                        dptnme=item.getPojoOfJsonArray().getDept_name();
                        zoneAleart();
                    }
                });
                generalViewHolder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        urlid=item.getPojoOfJsonArray().getId();
                        bottomdialoge();
                    }
                });
                break;

            case ListItem.TYPE_DEPART:
                DepartItem departItem1 = (DepartItem) consolidatedList.get(position);
                DateViewHolder dateViewHolder = (DateViewHolder) holder;
                dateViewHolder.txtTitle.setText(departItem1.getDept_name());
                // Populate date item data here
                break;
        }


    }



    @Override
    public Filter getFilter() {
        return selectfilter;
    }
    private Filter selectfilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<RecordModals> filterdlist = new ArrayList<>();

            if (constraint==null || constraint.length() ==0){
                filterdlist.addAll(recordModals2);

            }
            else {
                for (RecordModals zoneModals:recordModals2){
                        String textfilter=zoneModals.getRecord_name().toLowerCase();
                        String idfilter= zoneModals.getDept_name().toLowerCase();
                        if(textfilter.contains(constraint.toString().toLowerCase())){
                            filterdlist.add(zoneModals);
                        }
                        else if (idfilter.contains(constraint.toString().toLowerCase())){
                            filterdlist.add(zoneModals);
                        }

                }
            }
            FilterResults results = new FilterResults();
            results.values = filterdlist;
            return  results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            recordModals.clear();
            recordModals.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };


    public void zoneAleart() {

        final Dialog dialog = new Dialog(context);
        final ImageButton cancle;
        final TextView save;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.editrecords);
        dialog.getWindow().setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
        editrecords=dialog.findViewById(R.id.editrecordnme);
        departments=dialog.findViewById(R.id.departmentid);
        save=dialog.findViewById(R.id.editrecords);
        cancle=dialog.findViewById(R.id.cancle);
        editrecords.setText(recordnme);

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordnme=editrecords.getText().toString();
                if (Utlity.is_online(context)){
                    editrecord(recordnme,dptid);
                    dialog.dismiss();
                }
                else {
                    Utlity.show_toast((Activity) context, context.getResources().getString(R.string.nointernet));
                }

            }

        });

        dialog.show();
        departments();
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

    private void editrecord(final String recordnme,final String dptid) {
        Utlity.show_progress(context);
        HashMap<String, String> keys = new HashMap<>();
        keys.put("fobunits_id", respoid);
        keys.put("record_name", recordnme);
        keys.put("departments_id", dptid);
        Request result= post( keys, Editrecord+urlid);
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
                                context.startActivity(new Intent(context, RecordsActivity.class));

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
        Request result= get( context,Deleterecord+urlid);
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
                                consolidatedList.remove(urlid);
                                Utlity.show_toast((Activity) context,object.getInt("success")+" Delete Sucessfully");
                                context.startActivity(new Intent(context, RecordsActivity.class));
                                //recorddata();
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

    /*private void recorddata() {
        Utlity.show_progress(context);
        apiInterface = ApiClients.getClient().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Records("Bearer "+ Utlity.get_user(context).getToken());
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
                        consolidatedList = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<RecordModals>>() {}.getType());
                        if(consolidatedList.size()!=0) {
                            try {
                                RecordsActivity.recyclerView.setHasFixedSize(true);
                                RecordsActivity.recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL,false));
                                recordAdapter = new RecordAdapter(context, consolidatedList);
                                RecordsActivity.recyclerView.setAdapter(recordAdapter);

                            }catch (ClassCastException e){
                                e.printStackTrace();
                            }

                        }
                        else
                        {
                            RecordsActivity.norecord.setVisibility(View.VISIBLE);
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

     */

    private void departments() {
        Utlity.show_progress(context);
        String respoid1 = pref.getString("respoid",null);
        apiInterface = ApiClients.getClient1().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Depatlist(respoid1,"Bearer "+ Utlity.get_user(context).getToken());
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
                        respoModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<RespoModal>>() {}.getType());
                        fobpart=new ArrayList<>();
                        //fobpart.add(dptnme);
                        fobpart.add("Select Deaprtment Id");
                        for (RespoModal respoModal:respoModals){
                            fobpart.add(respoModal.getDept_name());
                        }

                        ArrayAdapter<String> adapter1 =
                                new ArrayAdapter<String>(context,  R.layout.spinneritem, fobpart);
                        adapter1.setDropDownViewResource( R.layout.spinneritem);
                        departments.setAdapter(adapter1);

                        if (dptnme != null) {
                            int spinnerPosition = adapter1.getPosition(dptnme);
                            departments.setSelection(spinnerPosition);
                        }

                        departments.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position!=0){
                                    dptid=respoModals.get(position-1).getId();
                                    Utlity.show_toast(context, dptid);

                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }

                        });

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog(context);
                Utlity.show_toast(context, "Not Founded Data");

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


   /* public class Myview extends RecyclerView.ViewHolder {
        ZonitemBinding zonitemBinding;
        public Myview(ZonitemBinding zonitemBinding) {
            super(zonitemBinding.getRoot());
            this.zonitemBinding=zonitemBinding;
        }
    }

    */

   public class DateViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtTitle;

        public DateViewHolder(View v) {
            super(v);
            this.txtTitle = (TextView) v.findViewById(R.id.dptnme);

        }
    }

    // View holder for general row item
    public class GeneralViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtTitle;
        protected LinearLayout gonelayout;
        protected ImageView edit,delete;

        public GeneralViewHolder(View v) {
            super(v);
            this.txtTitle = (TextView) v.findViewById(R.id.recorditem);
            this.gonelayout = (LinearLayout) v.findViewById(R.id.gonelayout);
            this.edit = (ImageView) v.findViewById(R.id.editzone);
            this.delete = (ImageView) v.findViewById(R.id.deletezone);

        }

    }

    @Override
    public int getItemCount() {
        return consolidatedList != null ? consolidatedList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {

        return consolidatedList.get(position).getType();
    }

}
