package com.safefoodmitra.safefoodmitra.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.reflect.TypeToken;
import com.safefoodmitra.safefoodmitra.Activities.InspectionResponsibility;
import com.safefoodmitra.safefoodmitra.Activities.RecordsActivity;
import com.safefoodmitra.safefoodmitra.Apis.ApiClients;
import com.safefoodmitra.safefoodmitra.Apis.RetApis;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.DepartItem;
import com.safefoodmitra.safefoodmitra.Modals.Item;
import com.safefoodmitra.safefoodmitra.Modals.ListItem;
import com.safefoodmitra.safefoodmitra.Modals.RespoModal;
import com.safefoodmitra.safefoodmitra.Modals.UserItem;
import com.safefoodmitra.safefoodmitra.Modals.UserModals;
import com.safefoodmitra.safefoodmitra.R;

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

import static android.content.Context.MODE_PRIVATE;
import static com.safefoodmitra.safefoodmitra.Activities.AdminMainActivity.respoid;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Deleteinspectrespo;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Deleterecord;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.EditInspectRespo;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Editrecord;

public class InspectRespoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Activity context;
    String urlid,dptid,dptnme,usernme,userid,depart2;
    RetApis apiInterface;
    List<RespoModal>respoModals;
    public List<UserModals>userModals;
    ArrayList<String> departpart;
    ArrayList<String> userpart;
    Spinner departments,users;
    List<ListItem> consolidatedList;
    View v1;
    SharedPreferences pref;
    public InspectRespoAdapter(Activity context, List<ListItem> consolidatedList) {
        this.context = context;
        this.consolidatedList = consolidatedList;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

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
                UserItem item  = (UserItem)consolidatedList.get(position);
                GeneralViewHolder generalViewHolder= (GeneralViewHolder) holder;
                   if (((UserItem) consolidatedList.get(position)).getPojoOfJsonArray().getId().equals("0")){

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
                        userid=item.getPojoOfJsonArray().getId();
                        usernme=item.getPojoOfJsonArray().getRecord_name();
                        depart2=item.getPojoOfJsonArray().getDept_name();
                        inspectAleart();
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
                //dptnme=departItem1.getDept_name();
                DateViewHolder dateViewHolder = (DateViewHolder) holder;
                dateViewHolder.txtTitle.setText(departItem1.getDept_name());
                // Populate date item data here
                break;
        }

       // holder.zonitemBinding.dptnme.setText(dateItem.getDate());
        //holder.zonitemBinding.titles.setText(generalItem.getPojoOfJsonArray().getRecord_name());
           // else {
          //  }

       // }\

        /* if (recordModals.get(position).getId().equals("0")){
            holder.zonitemBinding.edidelet.setVisibility(View.GONE);
            holder.zonitemBinding.titles.setText(recordModals.get(position).getDept_name());
        }
        else {
          if (dptnme!=recordModals.get(position).getDept_name()) {
                dptnme=recordModals.get(position).getDept_name();
                holder.zonitemBinding.titles.setText(dptnme);
            }
            else if (dptnme==recordModals.get(position).getDept_name()){

                holder.zonitemBinding.edidelet.setVisibility(View.VISIBLE);
                holder.zonitemBinding.titles.setText(recordModals.get(position).getRecord_name());
            }



            holder.zonitemBinding.edidelet.setVisibility(View.VISIBLE);
            holder.zonitemBinding.titles.setText(recordModals.get(position).getRecord_name());


        }



        holder.zonitemBinding.editzone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlid=recordModal.getId();
                recordnme=recordModal.getRecord_name();
                zoneAleart();

            }
        });
        holder.zonitemBinding.deletezone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlid=recordModal.getId();
                bottomdialoge();
            }
        });
        */

    }

    public void inspectAleart() {

        final Dialog dialog = new Dialog(context);
        final ImageButton cancle;
        final TextView save;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.editinspectrespo);
        dialog.getWindow().setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
        users=dialog.findViewById(R.id.userid);
        departments=dialog.findViewById(R.id.departmentid);
        save=dialog.findViewById(R.id.editrecords);
        cancle=dialog.findViewById(R.id.cancle);

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utlity.is_online(context)){
                    editinspect(dptid,userid);
                    dialog.dismiss();
                }
                else {
                    Utlity.show_toast((Activity) context, context.getResources().getString(R.string.nointernet));
                }
            }

        });
        dialog.show();
        departments();
        users();
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

    private void editinspect(final String dptid,final String userid) {
        Utlity.show_progress(context);
        HashMap<String, String> keys = new HashMap<>();
        keys.put("fobunits_id", respoid);
        keys.put("departments_id", dptid);
        keys.put("users_id", userid);
        Request result= post( keys, EditInspectRespo+urlid);
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
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
                                context.startActivity(new Intent(context, InspectionResponsibility.class));

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
        Request result= get( context,Deleteinspectrespo+urlid);
        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
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
                        departpart=new ArrayList<>();
                        departpart.add("Select Deapartment Id");
                        for (RespoModal respoModal:respoModals){
                            departpart.add(respoModal.getDept_name());
                        }

                        ArrayAdapter<String> adapter1 =
                                new ArrayAdapter<String>(context,  R.layout.spinneritem, departpart);
                        adapter1.setDropDownViewResource( R.layout.spinneritem);
                        departments.setAdapter(adapter1);

                        if (depart2 != null) {
                            int spinnerPosition = adapter1.getPosition(depart2);
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
    private void users() {
        apiInterface = ApiClients.getClient().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Users(respoid,"Bearer "+ Utlity.get_user(context).getToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        userModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<UserModals>>() {}.getType());
                        userpart=new ArrayList<>();
                        userpart.add("Select Users Id");
                        for (UserModals userModals:userModals){
                            userpart.add(userModals.getFirst_name()+ " "+ userModals.getLast_name());
                        }

                        ArrayAdapter<String> adapter1 =
                                new ArrayAdapter<String>(context,  R.layout.spinneritem, userpart);
                        adapter1.setDropDownViewResource( R.layout.spinneritem);
                        users.setAdapter(adapter1);

                        if (usernme != null) {
                            int spinnerPosition = adapter1.getPosition(usernme);
                            users.setSelection(spinnerPosition);
                        }

                        users.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position!=0){
                                    userid=userModals.get(position-1).getId();
                                    Utlity.show_toast(context, userid);

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
