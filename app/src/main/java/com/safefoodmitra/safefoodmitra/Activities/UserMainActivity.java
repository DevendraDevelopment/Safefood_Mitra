package com.safefoodmitra.safefoodmitra.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fxn.OnBubbleClickListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.reflect.TypeToken;
import com.safefoodmitra.safefoodmitra.Adapter.AdapterViewPager;
import com.safefoodmitra.safefoodmitra.Apis.ApiClients;
import com.safefoodmitra.safefoodmitra.Apis.RetApis;
import com.safefoodmitra.safefoodmitra.Fragments.Home;
import com.safefoodmitra.safefoodmitra.Fragments.NewsFragment;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Helper.ZoomOutPageTransformer;
import com.safefoodmitra.safefoodmitra.Modals.UnitModals;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.ActivityUserMainBinding;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.MANAGE_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;
import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;
import static com.safefoodmitra.safefoodmitra.Activities.AdminMainActivity.respoid;

import static com.safefoodmitra.safefoodmitra.Helper.Utlity.Uerrolid;

import static org.apache.xmlbeans.impl.common.XBeanDebug.log;

public class UserMainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PERMISSION_REQUEST_CODE = 10;
    ActivityUserMainBinding userMainBinding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    private LinearLayout logoutlayout, editlayout, notificatonLinear;
    boolean doubleBackToExitPressedOnce = false;
    TextView username, usernumbers, useremail, notificationnum;
    Spinner userlist;
    RetApis apiInterface;
    ArrayList<String> unittpart;
    List<UnitModals> unitModals;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String oldVersion;
    String posname, responame;
    ImageView notificationimage;
    public String notikey;

    // NotificationDB notificationDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_main);
        //isStoragePermissionGranted();
        // notificationDB = new NotificationDB(this);
        Uerrolid = Utlity.get_user(UserMainActivity.this).getUserroles_id();
        try {
            VersionChecker versionChecker = new VersionChecker();
            oldVersion = versionChecker.execute().get();
        } catch (ExecutionException | NullPointerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        pref = this.getSharedPreferences("MyG9", MODE_PRIVATE);
        editor = pref.edit();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        toolbar = findViewById(R.id.toolbar);
        notificationimage = toolbar.findViewById(R.id.notification);
        notificationnum = toolbar.findViewById(R.id.notificationnum);
        notificatonLinear = toolbar.findViewById(R.id.notificationLinear);
        setSupportActionBar(toolbar);
        View hView = userMainBinding.navigate.getHeaderView(0);
        logoutlayout = hView.findViewById(R.id.logouts);
        username = hView.findViewById(R.id.username);
        usernumbers = hView.findViewById(R.id.usernumbers);
        useremail = hView.findViewById(R.id.useremail);
        userlist = hView.findViewById(R.id.userlists);
        editlayout = hView.findViewById(R.id.editprofile);
        username.setText(Utlity.get_user(this).getFirst_name());
        usernumbers.setText("+ " + Utlity.get_user(this).getMobile_no());
        useremail.setText(Utlity.get_user(this).getEmail());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        if (Utlity.is_online(this)) {
            unitdata();
        } else {
            Utlity.show_toast(UserMainActivity.this, getResources().getString(R.string.nointernet));

        }
        // loadFragment1(new Home());
        AdapterViewPager adapterViewPager = new AdapterViewPager(getSupportFragmentManager());
        adapterViewPager.addFragment(new Home());
        adapterViewPager.addFragment(new Home());
        adapterViewPager.addFragment(new NewsFragment());
        userMainBinding.viewPager.setAdapter(adapterViewPager);
        userMainBinding.viewPager.setCurrentItem(0);

//        userMainBinding.bubbleTabBar.setSelected(true);
//        userMainBinding.bubbleTabBar.setBottom(0);
        userMainBinding.bubbleTabBar.setupBubbleTabBar(userMainBinding.viewPager);

        userMainBinding.bubbleTabBar.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userMainBinding.appBarLayout.setVisibility(View.VISIBLE);
                userMainBinding.viewPager.setCurrentItem(0); //TODO Profile
            }
        });
        userMainBinding.bubbleTabBar.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userMainBinding.appBarLayout.setVisibility(View.VISIBLE);
                userMainBinding.viewPager.setCurrentItem(1); //TODO Home
            }
        });

        userMainBinding.bubbleTabBar.getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userMainBinding.appBarLayout.setVisibility(View.GONE);
                userMainBinding.viewPager.setCurrentItem(2); //TODO Study Material
            }
        });

        userMainBinding.viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        click();
        requestPermission();

        userlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                respoid = unitModals.get(position).getId();
                posname = unitModals.get(position).getUnit_name();
                if (respoid != null) {
                    editor.putString("respoid", respoid);
                    editor.apply();
                    editor.commit();
                } else {
                    editor.putString("respoid", "0");
                    editor.apply();
                    editor.commit();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            String newversion = pInfo.versionName;
            if (oldVersion != null) {
                if (!newversion.equals(oldVersion)) {
                    playDilogBox();
                }
            } else {
                Utlity.show_toast(UserMainActivity.this, getResources().getString(R.string.nointernet));
            }

        } catch (PackageManager.NameNotFoundException | NullPointerException e) {
            e.printStackTrace();
        }

      /*  AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {
                if (result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && result.isUpdateTypeAllowed(IMMEDIATE)){
                    try {
                        appUpdateManager.startUpdateFlowForResult(result, IMMEDIATE,UserMainActivity.this,RESULT_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });*/
      /*  if (unread!=0){
            notificatonLinear.setVisibility(View.VISIBLE);
            notificationnum.setText(String.valueOf(unread));
        }*/
        notikey = FirebaseDatabase.getInstance().getReference().child("Notification").child(Utlity.getDeviceID(this)).getKey();

    }

    @Override
    protected void onStart() {
        Utlity.appTime = true;
        super.onStart();
    }

    private void playDilogBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserMainActivity.this);
        builder.setTitle(R.string.safefoodmitraaupdate).
                setMessage(R.string.updateverison);
        builder.setPositiveButton(R.string.updatenow,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                        i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.safefoodmitra.safefoodmitra"));
                        startActivity(i);
                    }
                });
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder.create();
        alert11.show();
    }



    /*public Fragment loadFragment1(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.dashframe, fragment);

        try {

            transaction.commit();
        } catch (IllegalStateException e) {
            transaction.commitAllowingStateLoss();
        }
        return fragment;
    }*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    public void click() {
        logoutlayout.setOnClickListener(this);
        editlayout.setOnClickListener(this);
        notificationimage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.logouts) {
            Logout(this);
            drawerLayout.closeDrawers();
        }
        if (view.getId() == R.id.editprofile) {
            startActivity(new Intent(this, EditProfile.class));
            drawerLayout.closeDrawers();
        } else if (view.getId() == R.id.notification) {
            startActivity(new Intent(this, NotificationActivity.class));
        }

    }

    public void Logout(final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(UserMainActivity.this);
        builder.setTitle(R.string.confirmationpopup).
                setMessage(R.string.surepopup);
        builder.setPositiveButton(R.string.yespopup,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Utlity.clear_db(UserMainActivity.this);
                        editor.clear();
                        editor.apply();
                        respoid = null;
                        //notificationDB.remove(notikey);
                        startActivity(new Intent(UserMainActivity.this, LoginActivity.class));
                        finishAffinity();
                    }
                });
        builder.setNegativeButton(R.string.nopopup,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder.create();
        alert11.show();

    }

    @SuppressLint("WrongConstant")
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawers();
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            //Snackbar.make(v, "Forgoted", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }


    private void requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {

            if ((checkSelfPermission(CAMERA)
                    == PackageManager.PERMISSION_GRANTED) && (checkSelfPermission(READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) && (checkSelfPermission(WRITE_EXTERNAL_STORAGE)
                    //   == PackageManager.PERMISSION_GRANTED)&&(checkSelfPermission(MANAGE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
            if (Environment.isExternalStorageManager()) {
                //todo when permission is granted
            } else {
              /*  try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
                    startActivityForResult(intent, 2296);
                } catch (Exception e) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivityForResult(intent, 2296);
                }
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);*/

            }
        } else {
            //below android 11
            ActivityCompat.requestPermissions(UserMainActivity.this, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

/*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2296) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    log("Update flow Result code: " + resultCode);
                    // perform action when allow permission success
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean CAMERA = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean READ_EXTERNAL_STORAGE = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean WRITE_EXTERNAL_STORAGE = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (CAMERA && READ_EXTERNAL_STORAGE && WRITE_EXTERNAL_STORAGE) {
                        log("Update flow Result code: " + requestCode);

                        // perform action when allow permission success
                    } else {
                        Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    public void unitdata() {
        Utlity.show_progress(this);
        apiInterface = ApiClients.getClient().create(RetApis.class);
        Call<ResponseBody> call = apiInterface.Units("Bearer " + Utlity.get_user(this).getToken());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;
                    try {
                        apidata = response.body().string();
                        Log.d("responce>>>>>", apidata);
                        JSONObject object = new JSONObject(apidata);
                        unitModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<UnitModals>>() {
                        }.getType());
                        unittpart = new ArrayList<>();
                        if (unitModals.size() != 0) {
                            String newsid;
                            for (UnitModals categoryModel : unitModals) {
                                unittpart.add(categoryModel.getUnit_name());
                                newsid = categoryModel.getId();
                                try {
                                    if (respoid != null) {
                                        if (respoid.equals(newsid)) {
                                            responame = categoryModel.getUnit_name();
                                        }
                                    }

                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }

                            }

                        } else {
                            editor.putString("respoid", "0");
                            editor.apply();
                            editor.commit();
                        }


                        Utlity.dismiss_dilog(UserMainActivity.this);
                        ArrayAdapter<String> adapter1 =
                                new ArrayAdapter<String>(UserMainActivity.this, R.layout.spinner_item, unittpart);
                        adapter1.setDropDownViewResource(R.layout.spinneritem);
                        userlist.setAdapter(adapter1);
                        if (responame != null) {
                            int spinnerPosition = adapter1.getPosition(responame);
                            userlist.setSelection(spinnerPosition);
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utlity.dismiss_dilog(UserMainActivity.this);
            }
        });
    }

    public class VersionChecker extends AsyncTask<String, String, String> {

        String newVersion;

        @Override
        protected String doInBackground(String... params) {

            try {
                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + "com.safefoodmitra.safefoodmitra" + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                        .first()
                        .ownText();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return newVersion;
        }
    }
}