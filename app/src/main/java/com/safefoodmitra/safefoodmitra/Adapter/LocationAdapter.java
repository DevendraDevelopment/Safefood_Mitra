package com.safefoodmitra.safefoodmitra.Adapter;

import android.annotation.SuppressLint;
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
import com.safefoodmitra.safefoodmitra.Activities.LocationActivity;
import com.safefoodmitra.safefoodmitra.Apis.ApiClients;
import com.safefoodmitra.safefoodmitra.Apis.RetApis;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.LocationModals;
import com.safefoodmitra.safefoodmitra.Modals.ZoneModals;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.LocationitemBinding;

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
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Deletelocation;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Deletezones;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Editlocation;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Editzones;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.Myview> implements Filterable {
    LayoutInflater layoutInflater;
    Activity context;
    List<LocationModals> locationModals;
    List<LocationModals> locationModals2;
    LocationAdapter locationAdapter;

    String urlid,locationnme,parentid,parentnme;
    EditText locationenmae;
    ArrayList<String> parentpart;
    RetApis apiInterface;
    Spinner parents;

    public LocationAdapter(Activity context, List<LocationModals> locationModals) {
        this.context = context;
        this.locationModals = locationModals;
        this.locationModals2 = new ArrayList<>(locationModals);

    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        LocationitemBinding locationitemBinding= DataBindingUtil.inflate(layoutInflater, R.layout.locationitem,parent,false);
        return new Myview(locationitemBinding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final Myview holder, final int position) {
        final LocationModals locationModal=locationModals.get(position);
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
        if (locationModal.getParent_id().equalsIgnoreCase("0")){
            holder.locationitemBinding.locatparent.setText(locationModals.get(position).getLoc_name());
        }
        else {
            holder.locationitemBinding.locatparent.setText("-- "+locationModals.get(position).getLoc_name());
        }

        holder.locationitemBinding.editlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlid=locationModal.getId();
                locationnme=locationModal.getLoc_name();
                parentnme=locationModal.getParent_name();
                parentid=locationModal.getId();
               // Utlity.show_toast(context,parentid);
                locationAleart();
            }
        });

        holder.locationitemBinding.deletelocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlid=locationModal.getId();
                bottomdialoge();

            }
        });
    }

    @Override
    public int getItemCount() {
        return locationModals.size();
    }

    @Override
    public Filter getFilter() {
        return selectfilter;
    }
    private Filter selectfilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<LocationModals> filterdlist = new ArrayList<>();

            if (constraint==null || constraint.length() ==0){
                filterdlist.addAll(locationModals2);

            }
            else {
                for (LocationModals locationModal:locationModals2){
                    String textfilter= locationModal.getLoc_name().toLowerCase();
                    String idfilter= locationModal.getId().toLowerCase();
                    if(textfilter.contains(constraint.toString().toLowerCase())){
                        filterdlist.add(locationModal);
                    }
                    else if (idfilter.contains(constraint.toString().toLowerCase())){
                        filterdlist.add(locationModal);
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
            locationModals.clear();
            locationModals.addAll((ArrayList) results.values);
            notifyDataSetChanged();


        }
    };

    public void locationAleart() {
        final Dialog dialog = new Dialog(context);
        final ImageButton cancle;
        final TextView save;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.editlocation);
        dialog.getWindow().setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
        locationenmae=dialog.findViewById(R.id.addlocationnme);
        save=dialog.findViewById(R.id.addlocations);
        cancle=dialog.findViewById(R.id.cancle);
        parents=dialog.findViewById(R.id.parentlists1);
        locationenmae.setText(locationnme);
        parentpart=new ArrayList<>();

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        parents.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // if (position!=null){
                // for (LocationModals locationModal:locationModals){
                if (position!=0){
                    parentid=locationModals.get(position-1).getId();
                    //Utlity.show_toast(context,parentid);
                }
                else {
                    parentid="0";
                    //Utlity.show_toast(context,parentid);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationnme=locationenmae.getText().toString();
                if (Utlity.is_online(context)){
                    editlocation(locationnme,parentid);
                    dialog.dismiss();
                }
                else {
                    Utlity.show_toast((Activity) context, context.getResources().getString(R.string.nointernet));
                }

            }

        });
        dialog.show();
        parentstype();
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
                    deletlocaton();
                    bottomSheetDialog.dismiss();
                }
                else {
                    Utlity.show_toast((Activity) context, context.getResources().getString(R.string.nointernet));
                }

            }

        });
        bottomSheetDialog.show();
    }

    private void parentstype() {
        Utlity.show_progress(context);
        apiInterface = ApiClients.getClient().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Locationstype(respoid,"Bearer "+ Utlity.get_user(context).getToken());
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
                        locationModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<LocationModals>>() {}.getType());
                        parentpart=new ArrayList<>();
                        parentpart.add("Select Parent");
                        for (LocationModals locationModal:locationModals){
                            parentpart.add(locationModal.getLoc_name());
                        }
                        ArrayAdapter<String> adapter =
                                new ArrayAdapter<String>(context,  R.layout.spinneritem, parentpart);
                        adapter.setDropDownViewResource( R.layout.spinneritem);
                        parents.setAdapter(adapter);

                        if (parentnme != null) {
                            int spinnerPosition = adapter.getPosition(parentnme);
                            parents.setSelection(spinnerPosition);
                        }


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog(context);
                Utlity.show_toast(context, "Not founded Data");
            }
        });
    }

    private void editlocation(final String locationnme,final String parentid) {
        Utlity.show_progress(context);
        HashMap<String, String> keys = new HashMap<>();
        keys.put("fobunits_id", respoid);
        keys.put("loc_name", locationnme);
        keys.put("parent_id", parentid);
        Request result= post( keys, Editlocation+urlid);
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
                                locationdata();
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
    private void deletlocaton() {
        Utlity.show_progress(context);
        HashMap<String, String> keys = new HashMap<>();
        keys.put("fobunits_id", "1");
        Request result= get( context,Deletelocation+urlid);
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
                                locationModals.remove(urlid);
                                Utlity.show_toast((Activity) context,object.getInt("success")+" Delete Sucessfully");
                                locationdata();
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

    private void locationdata() {
        Utlity.show_progress(context);
        apiInterface = ApiClients.getClient().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Locations(respoid,"Bearer "+ Utlity.get_user(context).getToken());
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
                        locationModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<LocationModals>>() {}.getType());
                        if(locationModals.size()!=0) {
                            LocationActivity.norecord.setVisibility(View.GONE);
                            LocationActivity.recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                            locationAdapter = new LocationAdapter(context, locationModals);
                            LocationActivity.recyclerView.setAdapter(locationAdapter);
                        }
                        else
                        {
                            LocationActivity.norecord.setVisibility(View.VISIBLE);
                            LocationActivity.recyclerView.setVisibility(View.GONE);
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
        LocationitemBinding locationitemBinding;
        public Myview(LocationitemBinding locationitemBinding) {
            super(locationitemBinding.getRoot());
            this.locationitemBinding=locationitemBinding;
        }
    }
}
