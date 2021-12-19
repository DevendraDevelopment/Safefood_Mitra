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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.reflect.TypeToken;
import com.safefoodmitra.safefoodmitra.Activities.InspectionResponsibility;
import com.safefoodmitra.safefoodmitra.Activities.RecordsActivity;
import com.safefoodmitra.safefoodmitra.Apis.ApiClients;
import com.safefoodmitra.safefoodmitra.Apis.RetApis;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.InspectrespoModals2;
import com.safefoodmitra.safefoodmitra.Modals.RespoModal;
import com.safefoodmitra.safefoodmitra.Modals.UserModals;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.RecorditemBinding;

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
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Deleteinspectrespo;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Deleterecord;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.EditInspectRespo;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Editrecord;

public class RecordAdapter2 extends RecyclerView.Adapter<RecordAdapter2.Myview> {
    LayoutInflater layoutInflater;
    Activity context;
    List<InspectrespoModals2> inspectrespoModals2s;
    String urlid,dptid,dptnme,recordnme;
    RetApis apiInterface;
    Spinner departments;
    List<RespoModal>respoModals;
    ArrayList<String> departpart;
    EditText editrecords;
    SharedPreferences pref;
    public RecordAdapter2(Activity context, List<InspectrespoModals2> inspectrespoModals2s) {
        this.context = context;
        this.inspectrespoModals2s = inspectrespoModals2s;

    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        RecorditemBinding recorditemBinding= DataBindingUtil.inflate(layoutInflater, R.layout.recorditem,parent,false);
        pref = context.getSharedPreferences("MyG9", MODE_PRIVATE);
        return new Myview(recorditemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final Myview holder, final int position) {
        final InspectrespoModals2 inspectrespoModals2=inspectrespoModals2s.get(position);

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
        if (inspectrespoModals2.getRecord_name().equals("")){
            holder.recorditemBinding.gonelayout.setVisibility(View.GONE);
            holder.recorditemBinding.gonelayout.removeViewAt(0);

        }
        else {
            holder.recorditemBinding.gonelayout.setVisibility(View.VISIBLE);
            holder.recorditemBinding.recorditem.setText(inspectrespoModals2s.get(position).getRecord_name());
        }

        holder.recorditemBinding.editzone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlid=inspectrespoModals2s.get(position).getId();
                dptid=inspectrespoModals2s.get(position).getId();
                dptnme=inspectrespoModals2s.get(position).getDept_name();
                recordnme=inspectrespoModals2s.get(position).getRecord_name();
                inspectAleart();

            }
        });

        holder.recorditemBinding.deletezone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlid=inspectrespoModals2s.get(position).getId();
                bottomdialoge();
            }
        });

    }

    @Override
    public int getItemCount() {
        return inspectrespoModals2s.size();
    }


    public class Myview extends RecyclerView.ViewHolder {
        RecorditemBinding recorditemBinding;
        public Myview(RecorditemBinding recorditemBinding) {
            super(recorditemBinding.getRoot());
            this.recorditemBinding=recorditemBinding;
        }
    }


    public void inspectAleart() {

        final Dialog dialog = new Dialog(context);
        final ImageButton cancle;
        final TextView save;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.editrecords);
        dialog.getWindow().setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
        departments=dialog.findViewById(R.id.departmentid);
        editrecords=dialog.findViewById(R.id.editrecordnme);
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
                    editrecord(dptid,recordnme);
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
                    deletrecord();
                    bottomSheetDialog.dismiss();
                }
                else {
                    Utlity.show_toast((Activity) context, context.getResources().getString(R.string.nointernet));
                }

            }

        });
        bottomSheetDialog.show();
    }

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

                        if (dptnme != null) {
                            int spinnerPosition = adapter1.getPosition(dptnme);
                            departments.setSelection(spinnerPosition);
                        }

                        departments.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position!=0){
                                    dptid=respoModals.get(position-1).getId();
                      //              Utlity.show_toast(context, dptid);

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

    private void editrecord(final String dptid,final String recordnme) {
        Utlity.show_progress(context);
        HashMap<String, String> keys = new HashMap<>();
        keys.put("fobunits_id", "1");
        keys.put("departments_id", dptid);
        keys.put("record_name", recordnme);
        Request result= post( keys, Editrecord+urlid);
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

    private void deletrecord() {
        Utlity.show_progress(context);
        HashMap<String, String> keys = new HashMap<>();
        keys.put("fobunits_id", "1");
        Request result= get( context,Deleterecord+urlid);
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
                                inspectrespoModals2s.remove(urlid);
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

}
