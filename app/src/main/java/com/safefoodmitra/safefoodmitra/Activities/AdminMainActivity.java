package com.safefoodmitra.safefoodmitra.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.reflect.TypeToken;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.safefoodmitra.safefoodmitra.Adapter.AdapterViewPager;
import com.safefoodmitra.safefoodmitra.Adapter.NotificationAdapter;
import com.safefoodmitra.safefoodmitra.Apis.ApiClients;
import com.safefoodmitra.safefoodmitra.Apis.RetApis;
import com.safefoodmitra.safefoodmitra.Fragments.Home;
import com.safefoodmitra.safefoodmitra.Fragments.NewsFragment;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Helper.ZoomOutPageTransformer;
import com.safefoodmitra.safefoodmitra.Modals.Notification;
import com.safefoodmitra.safefoodmitra.Modals.UnitModals;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.ActivityMainBinding;

import org.jetbrains.annotations.NotNull;
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


import static com.safefoodmitra.safefoodmitra.Helper.Utlity.Uerrolid;

import static org.apache.xmlbeans.impl.common.XBeanDebug.log;

public class AdminMainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PERMISSION_REQUEST_CODE = 10;
    ActivityMainBinding mainBinding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    LinearLayout zonelayout,locatiolayout,departmentlayout,equipmentlayout,recordlayout,userlayout,unitlayout,logoutlayout,inspectrespo,joballocation,notificationLenear,howtouselayout;
    boolean doubleBackToExitPressedOnce = false;
    TextView adminname,adminumbers,adminemail,notificationnum;
    Spinner adminlist;
    RetApis apiInterface;
    ArrayList<String> unittpart;
    List<UnitModals> unitModals;
    public static String respoid ="";
    String posname,responame;
    String oldVersion;
    SharedPreferences pref,intentpref;
    SharedPreferences.Editor editor,inteteditor;
    public String notikey;
    public ImageView notificationimg;
    Dialog dialog;
    YouTubePlayerView youTubePlayerView;
    //NotificationDB notificationDB;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        //notificationDB = new NotificationDB(this);

        Uerrolid = Utlity.get_user(AdminMainActivity.this).getUserroles_id();
        try {
            VersionChecker versionChecker = new VersionChecker();
            oldVersion = versionChecker.execute().get();
        } catch (ExecutionException | InterruptedException | NullPointerException e) {
            e.printStackTrace();
        }
        pref = this.getSharedPreferences("MyG9", MODE_PRIVATE);
        editor = pref.edit();

        intentpref = this.getSharedPreferences("filesvalues", MODE_PRIVATE);
        inteteditor = intentpref.edit();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        Toolbar toolbar = findViewById(R.id.toolbar);
        notificationimg = toolbar.findViewById(R.id.notification);
        notificationnum = toolbar.findViewById(R.id.notificationnum);
        notificationLenear = toolbar.findViewById(R.id.notificationLinear);
        setSupportActionBar(toolbar);
        View hView =  mainBinding.navigate.getHeaderView(0);
        //laypouts
        adminname=hView.findViewById(R.id.adminname);
        adminumbers=hView.findViewById(R.id.adminumbers);
        adminemail=hView.findViewById(R.id.adminemail);
        adminlist=hView.findViewById(R.id.adminlists);
        adminname.setText(Utlity.get_user(this).getFirst_name());
        adminumbers.setText("+ "+Utlity.get_user(this).getMobile_no());
        adminemail.setText(Utlity.get_user(this).getEmail());
        zonelayout=hView.findViewById(R.id.zones);
        locatiolayout=hView.findViewById(R.id.locations);
        departmentlayout=hView.findViewById(R.id.department);
        equipmentlayout=hView.findViewById(R.id.equipment);
        recordlayout=hView.findViewById(R.id.records);
        userlayout=hView.findViewById(R.id.users);
        unitlayout=hView.findViewById(R.id.unit);
        logoutlayout=hView.findViewById(R.id.logout);
        inspectrespo=hView.findViewById(R.id.inspectrespo);
        joballocation=hView.findViewById(R.id.joballocation);
        howtouselayout=hView.findViewById(R.id.howtouse);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        if (Utlity.is_online(this)){
            unitdata();
        }
        else {
            Utlity.show_toast(AdminMainActivity.this,getResources().getString(R.string.nointernet));

        }

      //  loadFragment1(new Home());
        AdapterViewPager adapterViewPager = new AdapterViewPager(getSupportFragmentManager());
        adapterViewPager.addFragment(new Home());
        adapterViewPager.addFragment(new Home());
        adapterViewPager.addFragment(new NewsFragment());
        mainBinding.viewPager.setAdapter(adapterViewPager);
        mainBinding.viewPager.setCurrentItem(1);
        mainBinding.bubbleTabBar.setupBubbleTabBar(mainBinding.viewPager);
        mainBinding.bubbleTabBar.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainBinding.viewPager.setCurrentItem(0); //TODO Profile
            }
        });
        mainBinding.bubbleTabBar.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainBinding.viewPager.setCurrentItem(1); //TODO Home
            }
        });
        mainBinding.bubbleTabBar.getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainBinding.viewPager.setCurrentItem(2); //TODO Study Material
            }
        });

        mainBinding.viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        click();

        requestPermission();



        adminlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                respoid=unitModals.get(position).getId();
                posname=unitModals.get(position).getUnit_name();
                if (respoid!=null){
                    editor.putString("respoid",respoid);
                    editor.apply();
                    editor.commit();
                }else {
                    editor.putString("respoid","0");
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
            if (oldVersion!=null){
                if (!newversion.equals(oldVersion)){
                    playDilogBox();
                }
            }else {
                Utlity.show_toast(AdminMainActivity.this, getResources().getString(R.string.nointernet));
            }


        } catch (PackageManager.NameNotFoundException |NullPointerException e) {
            e.printStackTrace();
        }

        notikey = FirebaseDatabase.getInstance().getReference().child("Notification").child(Utlity.getDeviceID(this)).getKey();

    }

    @Override
    protected void onStart() {
        Utlity.appTime=true;
        super.onStart();
    }

    public void click(){
        zonelayout.setOnClickListener(this);
        locatiolayout.setOnClickListener(this);
        departmentlayout.setOnClickListener(this);
        equipmentlayout.setOnClickListener(this);
        recordlayout.setOnClickListener(this);
        userlayout.setOnClickListener(this);
        unitlayout.setOnClickListener(this);
        logoutlayout.setOnClickListener(this);
        inspectrespo.setOnClickListener(this);
        joballocation.setOnClickListener(this);
        notificationimg.setOnClickListener(this);
        howtouselayout.setOnClickListener(this);
    }

   /* public void loadFragment1(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.dashframe, fragment);

        try {

            transaction.commit();
        } catch (IllegalStateException e) {
            transaction.commitAllowingStateLoss();
        }
    }*/



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        AlphaAnimation buttonClick = new AlphaAnimation(0.0f, 1.0f);
        buttonClick.setDuration(100);

        if (view.getId()==R.id.zones){
            // view.startAnimation(buttonClick);
            startActivity(new Intent(this,ZoneActivity.class));
            drawerLayout.closeDrawers();

        }
        else if (view.getId()==R.id.locations){
            startActivity(new Intent(this,LocationActivity.class));
            drawerLayout.closeDrawers();
        }
        else if (view.getId()==R.id.department){
            startActivity(new Intent(this,DeparmentActivity.class));
            drawerLayout.closeDrawers();
        }
        else if (view.getId()==R.id.equipment){
            startActivity(new Intent(this,EqupmentActivity.class));
            drawerLayout.closeDrawers();
        }
        else if (view.getId()==R.id.records){
            startActivity(new Intent(this,RecordsActivity.class));
            drawerLayout.closeDrawers();
        }

        else if (view.getId()==R.id.unit){
            startActivity(new Intent(this,UnitsActivity.class));
            drawerLayout.closeDrawers();
        }

        else if (view.getId()==R.id.users){
            startActivity(new Intent(this,UsersActivity.class));
            drawerLayout.closeDrawers();
        }

        else if (view.getId()==R.id.inspectrespo){
            startActivity(new Intent(this,InspectionResponsibility.class));
            drawerLayout.closeDrawers();
        }

        else if (view.getId()==R.id.joballocation){
            startActivity(new Intent(this,JobAllocation.class));
            drawerLayout.closeDrawers();
        }
        else if (view.getId()==R.id.howtouse){
            youtubeData("https://www.youtube.com/watch?v=tXwl4Sa5G6o");
            drawerLayout.closeDrawers();
        }

        else if (view.getId()==R.id.logout){
            Logout(this);
            drawerLayout.closeDrawers();
        }else if (view.getId()==R.id.notification){
            startActivity(new Intent(this, NotificationActivity.class));
        }

    }

    @SuppressLint("WrongConstant")
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawers();
        }else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }
    private void playDilogBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminMainActivity.this);
        builder.setTitle(R.string.safefoodmitraaupdate).
                setMessage(R.string.updateverison).setIcon(R.drawable.safelogo);
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

    public  void Logout(final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(AdminMainActivity.this);
        builder.setTitle(R.string.confirmationpopup).
                setMessage(R.string.surepopup);
        builder.setPositiveButton(R.string.yespopup,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Utlity.clear_db(AdminMainActivity.this);
                        editor.clear();
                        editor.apply();
                        respoid= null;
                        startActivity(new Intent(AdminMainActivity.this, LoginActivity.class));
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


    private void requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {

            if ((checkSelfPermission(CAMERA)
                    == PackageManager.PERMISSION_GRANTED)&&(checkSelfPermission(READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED)&&(checkSelfPermission(WRITE_EXTERNAL_STORAGE)
                  //  == PackageManager.PERMISSION_GRANTED)&&(checkSelfPermission(MANAGE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{CAMERA, READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }


            if (Environment.isExternalStorageManager()) {
                //todo when permission is granted
            } else {
               /* try {
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
            ActivityCompat.requestPermissions(AdminMainActivity.this, new String[]{CAMERA,WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

   /* @Override
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
                        //filesvlue=intentpref.getString("values",null);
                        // perform action when allow permission success
                    } else {
                        Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    public void unitdata(){
        Utlity.show_progress(this);
        apiInterface = ApiClients.getClient().create(RetApis.class);
        Call<ResponseBody> call=apiInterface.Units("Bearer "+ Utlity.get_user(this).getToken());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String apidata = null;

                    try {
                        apidata = response.body().string();
                        Log.d("responceunit>>>>>",apidata);
                        JSONObject object = new JSONObject(apidata);
                        unitModals = Utlity.gson.fromJson(object.getJSONArray("success").toString(), new TypeToken<List<UnitModals>>() {}.getType());
                        unittpart=new ArrayList<>();
                        // unittpart.add("Select Unit");
                        if (unitModals.size()!=0){
                            String newsid;
                            for (UnitModals categoryModel:unitModals){
                                unittpart.add(categoryModel.getUnit_name());
                                newsid=categoryModel.getId();
                                try {
                                    if (respoid != null){
                                        if (respoid.equals(newsid)){
                                            responame=categoryModel.getUnit_name();
                                        }
                                    }

                                }catch (NullPointerException e){
                                    e.printStackTrace();
                                }

                            }
                        }else {
                            editor.putString("respoid","0");
                            editor.apply();
                            editor.commit();

                        }
                       /* if (unitModals.size()!=0){
                            for (UnitModals categoryModel:unitModals){
                                unittpart.add(categoryModel.getUnit_name());
                                respoid=categoryModel.getId();
                                editor.putString("respoid",respoid);
                                editor.apply();
                                editor.commit();
                            }
                        }else {
                            editor.putString("respoid","0");
                            editor.apply();
                            editor.commit();

                        }*/

                        Utlity.dismiss_dilog(AdminMainActivity.this);
                        ArrayAdapter<String> adapter1 =
                                new ArrayAdapter<String>(AdminMainActivity.this,  R.layout.spinner_item, unittpart);
                        adapter1.setDropDownViewResource( R.layout.spinneritem);
                        adminlist.setAdapter(adapter1);

                        if (responame != null) {
                            int spinnerPosition = adapter1.getPosition(responame);
                            adminlist.setSelection(spinnerPosition);
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Utlity.dismiss_dilog(AdminMainActivity.this);
            }
        });
    }
    public static class VersionChecker extends AsyncTask<String, String, String> {

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
    public void youtubeData(String videoId){
        dialog = new Dialog(AdminMainActivity.this,R.style.full_screen_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.youtube);
        dialog.getWindow().setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
        youTubePlayerView=dialog.findViewById(R.id.youtube_player_view);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NotNull YouTubePlayer youTubePlayer) {
                // String videoId = "https://www.youtube.com/watch?v=hoNb6HuNmU0";
                youTubePlayer.loadVideo(videoId, 0);

                if (!videoId.isEmpty()){
                    try {
                        String[] arrOfStr = videoId.split("=", 2);
                        String part1= arrOfStr[1];
                        // for (String a : arrOfStr)
                        System.out.println(part1);
                        youTubePlayer.loadVideo(part1, 0);
                    }catch (IndexOutOfBoundsException e){
                        e.printStackTrace();
                    }

                }
                else {
                    Toast.makeText(AdminMainActivity.this,"Not good url your video",Toast.LENGTH_LONG).show();
                }
            }
        });

        youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {

            }

            @Override
            public void onYouTubePlayerExitFullScreen() {

            }
        });
        dialog.show();
    }
}

