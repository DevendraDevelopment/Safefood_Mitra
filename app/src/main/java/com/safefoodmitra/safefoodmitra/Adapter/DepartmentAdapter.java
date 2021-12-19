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
import com.safefoodmitra.safefoodmitra.Activities.DeparmentActivity;
import com.safefoodmitra.safefoodmitra.Apis.ApiClients;
import com.safefoodmitra.safefoodmitra.Apis.RetApis;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.DeparmentModals;
import com.safefoodmitra.safefoodmitra.Modals.LocationModals;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.DeparmentitemBinding;

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
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Deletelocation;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Editdepartment;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Editlocation;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.Myview> implements Filterable {
    LayoutInflater layoutInflater;
    Activity context;
    List<DeparmentModals> deparmentModals;
    List<DeparmentModals> deparmentModals2;

    String urlid,departsnnme,parentid,parentnme;
    EditText departmentnmae;
    ArrayList<String> parentpart;
    RetApis apiInterface;
    Spinner parents;
    DepartmentAdapter departmentAdapter;
    public DepartmentAdapter(Activity context, List<DeparmentModals> deparmentModals) {
        this.context = context;
        this.deparmentModals = deparmentModals;
        this.deparmentModals2 = new ArrayList<>(deparmentModals);
    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        DeparmentitemBinding deparmentitemBinding= DataBindingUtil.inflate(layoutInflater, R.layout.deparmentitem,parent,false);
        return new Myview(deparmentitemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final Myview holder, final int position) {
        final DeparmentModals deparmentModal=deparmentModals.get(position);
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

        if (deparmentModal.getParent_id().equalsIgnoreCase("0")){
            holder.deparmentitemBinding.locatparent.setText(deparmentModals.get(position).getDept_name());
        }
        else {
            holder.deparmentitemBinding.locatparent.setText("-- "+deparmentModals.get(position).getDept_name());
        }

        holder.deparmentitemBinding.editdepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlid=deparmentModal.getId();
                parentnme=deparmentModal.getParent_name();
                departsnnme=deparmentModal.getDept_name();
                parentid=deparmentModal.getId();
                departAleart();

            }
        });

        holder.deparmentitemBinding.deletdepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlid=deparmentModal.getId();
                bottomdialoge();
            }
        });


    }

    @Override
    public int getItemCount() {
        return deparmentModals.size();
    }

    @Override
    public Filter getFilter() {
        return selectfilter;
    }
    private Filter selectfilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<DeparmentModals> filterdlist = new ArrayList<>();

            if (constraint==null || constraint.length() ==0){
                filterdlist.addAll(deparmentModals2);

            }
            else {
                for (DeparmentModals locationModal:deparmentModals2){
                    String textfilter= locationModal.getDept_name().toLowerCase();
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
            deparmentModals.clear();
            deparmentModals.addAll((ArrayList) results.values);
            notifyDataSetChanged();


        }
    };


    public void departAleart() {
        final Dialog dialog = new Dialog(context);
        final ImageButton cancle;
        final TextView save;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.editdeparment);
        dialog.getWindow().setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
        departmentnmae=dialog.findViewById(R.id.editedparmentsnme);
        save=dialog.findViewById(R.id.editdeparments);
        cancle=dialog.findViewById(R.id.cancle);
        parents=dialog.findViewById(R.id.parentlists2);
        departmentnmae.setText(departsnnme);
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
                if (position!=0){
                    parentid=deparmentModals.get(position-1).getId();
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
                departsnnme=departmentnmae.getText().toString();
                if (Utlity.is_online(context)){
                    editdepartment(departsnnme,parentid);
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
                    deletdepartment();
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
        retrofit2.Call<ResponseBody> call=apiInterface.Departmentype(respoid,"Bearer "+ Utlity.get_user(context).getToken());
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
                        deparmentModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<DeparmentModals>>() {}.getType());
                        parentpart=new ArrayList<>();
                        parentpart.add("Select Parent");
                        for (DeparmentModals deparmentModal:deparmentModals){
                           // if (deparmentModal.getParent_id().equalsIgnoreCase("0")){
                                parentpart.add(deparmentModal.getDept_name());

                           // }
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

    private void editdepartment(final String locationnme,final String parentid) {
        Utlity.show_progress(context);
        HashMap<String, String> keys = new HashMap<>();
        keys.put("fobunits_id", respoid);
        keys.put("dept_name", locationnme);
        keys.put("parent_id", parentid);
        Request result= post( keys, Editdepartment+urlid);
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
                                departmentdata();

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
    private void deletdepartment() {
        Utlity.show_progress(context);
        HashMap<String, String> keys = new HashMap<>();
        keys.put("fobunits_id", "1");
        Request result= get( context,Deletedepartment+urlid);
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
                                deparmentModals.remove(urlid);
                                Utlity.show_toast((Activity) context,object.getInt("success")+" Delete Sucessfully");
                                departmentdata();
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
    private void departmentdata() {
        Utlity.show_progress(context);
        apiInterface = ApiClients.getClient().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Deparments(respoid,"Bearer "+ Utlity.get_user(context).getToken());
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
                        deparmentModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<DeparmentModals>>() {}.getType());
                        if(deparmentModals.size()!=0) {
                            DeparmentActivity.recyclerView.setHasFixedSize(true);
                            DeparmentActivity.recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL,false));
                            departmentAdapter = new DepartmentAdapter(context, deparmentModals);
                            DeparmentActivity.recyclerView.setAdapter(departmentAdapter);
                        }
                        else
                        {
                            DeparmentActivity.norecord.setVisibility(View.VISIBLE);
                            DeparmentActivity.recyclerView.setVisibility(View.GONE);
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
                .header("Authorization","Bearer "+ Utlity.get_user((Activity) context).getToken())
                .post(parmetrs)
                .build();
    }



    public class Myview extends RecyclerView.ViewHolder {
        DeparmentitemBinding deparmentitemBinding;
        public Myview(DeparmentitemBinding deparmentitemBinding) {
            super(deparmentitemBinding.getRoot());
            this.deparmentitemBinding=deparmentitemBinding;
        }
    }
}
