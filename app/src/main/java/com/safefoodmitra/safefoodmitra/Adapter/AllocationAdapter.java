
package com.safefoodmitra.safefoodmitra.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.safefoodmitra.safefoodmitra.Activities.JobAllocation;
import com.safefoodmitra.safefoodmitra.Activities.UsersActivity;
import com.safefoodmitra.safefoodmitra.Apis.ApiClients;
import com.safefoodmitra.safefoodmitra.Apis.RetApis;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.JobTypeModal;
import com.safefoodmitra.safefoodmitra.Modals.LocationModals;
import com.safefoodmitra.safefoodmitra.Modals.RespoModal;
import com.safefoodmitra.safefoodmitra.Modals.UnitModals;
import com.safefoodmitra.safefoodmitra.Modals.UserModals;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.AllocationitemBinding;
import com.safefoodmitra.safefoodmitra.databinding.UsersitemBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.content.Context.MODE_PRIVATE;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.DeleteUser;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.EditInspectRespo;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.EditUser;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Editlocation;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.joballocation;
import static com.safefoodmitra.safefoodmitra.CameraAll.TextEditorDialogFragment.TAG;

public class AllocationAdapter extends RecyclerView.Adapter<AllocationAdapter.Myview> implements Filterable, CheckAdapter.OnItemClickListener {
    LayoutInflater layoutInflater;
    Activity context;
    List<UserModals> userModals;
    List<UserModals> userModals2;
    List<RespoModal> respoModals;
    RetApis apiInterface;
    Spinner department;
    ArrayList<String> departpart;
    String dptid,urlid;
    int check;
    SharedPreferences pref;
    CheckAdapter checkAdapter;
    ArrayList<JobTypeModal> jobTypeModals;
    RecyclerView recyclerView;
    CheckAdapter.OnItemClickListener onItemClickListener;
    Map<String, String> checkdata = new HashMap<String, String>();
    public AllocationAdapter(Activity context, List<UserModals> userModals) {
        this.context = context;
        this.userModals = userModals;
        this.userModals2 = new ArrayList<>(userModals);
    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        AllocationitemBinding allocationitemBinding= DataBindingUtil.inflate(layoutInflater, R.layout.allocationitem,parent,false);
        pref = context.getSharedPreferences("MyG9", MODE_PRIVATE);
        return new Myview(allocationitemBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull final Myview holder, final int position) {
        final UserModals userModal=userModals.get(position);
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
        holder.allocationitemBinding.usernme.setText(userModals.get(position).getFirst_name()+" "+userModals.get(position).getLast_name());
        holder.allocationitemBinding.layoutclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlid=userModal.getId();
                allocatAlert();
            }
        });

        onItemClickListener=new CheckAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, boolean checked) {

                if(checked)
                jobTypeModals.get(position).setJobtype_status("1");
                else
                    jobTypeModals.get(position).setJobtype_status("0");

            }
        };

    }

    @Override
    public int getItemCount() {
        return userModals.size();
    }

    @Override
    public Filter getFilter() {
        return selectfilter;
    }

    private Filter selectfilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<UserModals> filterdlist = new ArrayList<>();

            if (constraint==null || constraint.length() ==0){
                filterdlist.addAll(userModals2);

            }
            else {
                for (UserModals locationModal:userModals2){
                    String textfilter= locationModal.getFirst_name().toLowerCase()+locationModal.getLast_name().toLowerCase();
                    String idfilter= locationModal.getId().toLowerCase();
                    if(textfilter.contains(constraint.toString().toLowerCase())){
                        filterdlist.add(locationModal);
                    }
                    else if (idfilter.contains(constraint.toString().toLowerCase())){
                        filterdlist.add(locationModal);
                    }

                }
            }
            FilterResults results = new FilterResults();
            results.values = filterdlist;
            return  results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            userModals.clear();
            userModals.addAll((ArrayList) results.values);
            notifyDataSetChanged();


        }
    };

    @Override
    public void onItemClick(int position, boolean checked) {

    }


    public class Myview extends RecyclerView.ViewHolder {
        AllocationitemBinding allocationitemBinding;
        public Myview(AllocationitemBinding allocationitemBinding) {
            super(allocationitemBinding.getRoot());
            this.allocationitemBinding=allocationitemBinding;
        }
    }

    public void allocatAlert() {
        final Dialog dialog = new Dialog(context);
        final ImageButton cancle;
        final TextView save;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alocation);
        dialog.getWindow().setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
        cancle=dialog.findViewById(R.id.cancle);
        save=dialog.findViewById(R.id.allocation);
        department=dialog.findViewById(R.id.departmentid);
        recyclerView=dialog.findViewById(R.id.checkboxlist);


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
                    if (department.getSelectedItemPosition()==0){
                        Utlity.show_toast((Activity) context, "Please Select department");
                    }
                    else {

                        String id,value;
                        for (int i = 0; i < jobTypeModals.size(); i++) {
                            id=jobTypeModals.get(i).getId();
                            value=jobTypeModals.get(i).getJobtype_status();
                            checkdata.put(String.valueOf(id), value);
                        }
                        Gson gson = new Gson();
                        System.out.println(gson.toJson(checkdata));
                        addAlocation();
                        dialog.dismiss();
                    }
                }
                else {
                    Utlity.show_toast((Activity) context, context.getResources().getString(R.string.nointernet));

                }

            }
        });

        dialog.show();
        departments();
        jobtypes(urlid);
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
                        departpart.add("Select Deaprtment Id");
                        for (RespoModal respoModal:respoModals){
                            departpart.add(respoModal.getDept_name());
                        }

                        ArrayAdapter<String> adapter1 =
                                new ArrayAdapter<String>(context,  R.layout.spinneritem, departpart);
                        adapter1.setDropDownViewResource( R.layout.spinneritem);
                        department.setAdapter(adapter1);


                        department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private void jobtypes(String urlid) {
        apiInterface = ApiClients.getClient1().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.JobType("Bearer "+ Utlity.get_user(context).getToken(),urlid);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        apidata = response.body().string();
                        Log.d("responce>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        jobTypeModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<JobTypeModal>>() {}.getType());
                        if (jobTypeModals.size()!=0){
                            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                            checkAdapter = new CheckAdapter(context ,jobTypeModals,onItemClickListener);
                            recyclerView.setAdapter(checkAdapter);
                        }

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

    private void addAlocation() {
        Utlity.show_progress(context);
        Gson gson = new Gson();
        HashMap<String, String> keys = new HashMap<>();
        keys.put("users_id", urlid);
        keys.put("departments_id",dptid);
        keys.put("jobtypes", gson.toJson(checkdata));
        Request result= post( keys, joballocation);
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
                                Utlity.show_toast((Activity) context,object.getString("success"));
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
}
