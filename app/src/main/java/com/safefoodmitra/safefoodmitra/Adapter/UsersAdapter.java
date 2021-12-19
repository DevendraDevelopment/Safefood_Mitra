package com.safefoodmitra.safefoodmitra.Adapter;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.reflect.TypeToken;
import com.safefoodmitra.safefoodmitra.Activities.UsersActivity;
import com.safefoodmitra.safefoodmitra.Apis.ApiClients;
import com.safefoodmitra.safefoodmitra.Apis.RetApis;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.UnitModals;
import com.safefoodmitra.safefoodmitra.Modals.UserModals;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.UsersitemBinding;

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
import static com.safefoodmitra.safefoodmitra.Apis.Apis.DeleteUser;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Deletezones;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.EditUser;
import static com.safefoodmitra.safefoodmitra.Apis.Apis.Editzones;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.Myview> implements Filterable {
    LayoutInflater layoutInflater;
    Activity context;
    List<UserModals> userModals;
    List<UserModals> userModals2;
    String urlid,firstnme,lastnme,email,mobilenum,fobunitid,unitnme;
    RetApis apiInterface;
    UsersAdapter usersAdapter;
    Spinner fobunit;
    EditText firstname,lastname,emails,mobilenumber;
    ArrayList<String> fobpart;
    List<UnitModals>unitModals;
    SharedPreferences pref;
    public UsersAdapter(Activity context, List<UserModals> userModals) {
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
        UsersitemBinding usersitemBinding= DataBindingUtil.inflate(layoutInflater, R.layout.usersitem,parent,false);
        pref = context.getSharedPreferences("MyG9", MODE_PRIVATE);
        fobunitid = pref.getString("respoid",null);
        return new Myview(usersitemBinding);
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
        holder.usersitemBinding.usernme.setText(userModals.get(position).getFirst_name()+" "+userModals.get(position).getLast_name());
        holder.usersitemBinding.emails.setText(userModals.get(position).getEmail());
        holder.usersitemBinding.numbers.setText(userModals.get(position).getMobile_no());

        holder.usersitemBinding.edituser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlid=userModal.getId();
                firstnme=userModal.getFirst_name();
                lastnme=userModal.getLast_name();
                email=userModal.getEmail();
                mobilenum=userModal.getMobile_no();
               // fobunitid=userModal.getFobunits_id();
                Utlity.show_toast(context,userModal.getFobunits_id());
                //unitnme=userModal.getFirst_name();
                userAlert();

            }
        });
        holder.usersitemBinding.deleteuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlid=userModal.getId();
                bottomdialoge();
            }
        });

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
                for (UserModals userModals:userModals2){
                    String textfilter= userModals.getFirst_name().toLowerCase();
                    String idfilter= userModals.getId().toLowerCase();
                    String number= userModals.getMobile_no();
                    String email= userModals.getEmail();
                    if(textfilter.contains(constraint.toString().toLowerCase())){
                        filterdlist.add(userModals);
                    }
                    else if (idfilter.contains(constraint.toString().toLowerCase())){
                        filterdlist.add(userModals);
                    }

                    else if (number.contains(constraint.toString())){
                        filterdlist.add(userModals);
                    }

                    else if (email.contains(constraint.toString().toLowerCase())){
                        filterdlist.add(userModals);
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


    public class Myview extends RecyclerView.ViewHolder {
        UsersitemBinding usersitemBinding;
        public Myview(UsersitemBinding usersitemBinding) {
            super(usersitemBinding.getRoot());
            this.usersitemBinding=usersitemBinding;
        }
    }

    public void userAlert() {

        final Dialog dialog = new Dialog(context);
        final ImageButton cancle;
        final TextView save;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.edituser);
        dialog.getWindow().setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
        firstname=dialog.findViewById(R.id.editfirstnme);
        lastname=dialog.findViewById(R.id.editlastnme);
        emails=dialog.findViewById(R.id.editemaildi);
        mobilenumber=dialog.findViewById(R.id.editmobilenum);
       // fobunit=dialog.findViewById(R.id.fobunit);
        save=dialog.findViewById(R.id.edituser);
        cancle=dialog.findViewById(R.id.cancle);
        firstname.setText(firstnme);
        lastname.setText(lastnme);
        emails.setText(email);
        mobilenumber.setText(mobilenum);

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstnme=firstname.getText().toString();
                lastnme=lastname.getText().toString();
                email=emails.getText().toString();
                mobilenum=mobilenumber.getText().toString();
                if (Utlity.is_online(context)){
                    edituser(firstnme,lastnme,email,mobilenum);
                    dialog.dismiss();
                }
                else {
                    Utlity.show_toast((Activity) context, context.getResources().getString(R.string.nointernet));
                }

            }

        });

      /*  fobunit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0){
                    fobunitid=unitModals.get(position-1).getId();
                    Utlity.show_toast(context,fobunitid);
                }
              *//*  else {
                    fobunitid="0";
                    Utlity.show_toast(context,fobunitid);
                }

               *//*
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
*/
        dialog.show();
       // unitdata();

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
                    deletuser();
                    bottomSheetDialog.dismiss();
                }
                else {
                    Utlity.show_toast((Activity) context, context.getResources().getString(R.string.nointernet));
                }

            }

        });
        bottomSheetDialog.show();
    }

    private void deletuser() {
        Utlity.show_progress(context);
        HashMap<String, String> keys = new HashMap<>();
       // keys.put("fobunits_id", fobunitid);
        Request result= get( context,DeleteUser+urlid+"/"+fobunitid);
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
                                userModals.remove(urlid);
                                Utlity.show_toast((Activity) context,object.getInt("success")+" Delete Sucessfully");
                                usersdata();
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

    private void edituser(final String ft,final String lt,final String eid,final String mno) {
        Utlity.show_progress(context);
        String respoid1 = pref.getString("respoid",null);
        HashMap<String, String> keys = new HashMap<>();
        keys.put("fobunits_id", respoid1);
        keys.put("first_name", ft);
        keys.put("last_name", lt);
        keys.put("email", eid);
        keys.put("mobile_no", mno);
        Request result= post( keys, EditUser+urlid);
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
                                usersdata();

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
   /* private void unitdata() {
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
                        fobpart=new ArrayList<>();
                        fobpart.add("Select Unit");
                        String newsid;
                        for (UnitModals unitModal:unitModals){
                            fobpart.add(unitModal.getUnit_name());
                            newsid=unitModal.getId();
                            if (fobunitid.equals(newsid)){
                                unitnme=unitModal.getUnit_name();

                            }
                        }

                        ArrayAdapter<String> adapter =
                                new ArrayAdapter<String>(context,  R.layout.spinneritem, fobpart);
                        adapter.setDropDownViewResource( R.layout.spinneritem);
                        fobunit.setAdapter(adapter);

                        if (unitnme != null) {
                            int spinnerPosition = adapter.getPosition(unitnme);
                            fobunit.setSelection(spinnerPosition);
                        }





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

    }*/
    private void usersdata() {
        Utlity.show_progress(context);
        apiInterface = ApiClients.getClient().create(RetApis.class);
        retrofit2.Call<ResponseBody> call=apiInterface.Users(respoid,"Bearer "+ Utlity.get_user(context).getToken());
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
                        userModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<UserModals>>() {}.getType());

                        if(userModals.size()!=0) {
                            UsersActivity.norecord.setVisibility(View.GONE);
                            UsersActivity.recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                            usersAdapter = new UsersAdapter(context, userModals);
                            UsersActivity.recyclerView.setAdapter(usersAdapter);
                        }
                        else
                        {
                            UsersActivity.norecord.setVisibility(View.VISIBLE);

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
}
