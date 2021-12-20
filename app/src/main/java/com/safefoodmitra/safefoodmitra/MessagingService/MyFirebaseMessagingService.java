package com.safefoodmitra.safefoodmitra.MessagingService;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.safefoodmitra.safefoodmitra.Activities.AdminMainActivity;
import com.safefoodmitra.safefoodmitra.Activities.AuditActivity;
import com.safefoodmitra.safefoodmitra.Activities.CleaningMaintenanceActivity;
import com.safefoodmitra.safefoodmitra.Activities.FoodsafetystandardActivity;
import com.safefoodmitra.safefoodmitra.Activities.FsmsdocumentActivity;
import com.safefoodmitra.safefoodmitra.Activities.InspectionActivity;
import com.safefoodmitra.safefoodmitra.Activities.NotificationActivity;

import com.safefoodmitra.safefoodmitra.Activities.UserMainActivity;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.Room.DatabaseClient;
import com.safefoodmitra.safefoodmitra.Room.TaskSync;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static com.safefoodmitra.safefoodmitra.Helper.Utlity.Uerrolid;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    Intent intent;
    String title,message,type,date,pdflink,linktype;
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    String channelId = "all_notifications";
    public TaskSync taskSync = new TaskSync();
    Uri sound;
    @SuppressLint("WrongThread")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.getPackageName() + "/" + R.raw.oppo_notification);
         if (remoteMessage.getData()!=null){
            title = remoteMessage.getData().get("title");
            message = remoteMessage.getData().get("description");
            type = remoteMessage.getData().get("type");
            date = remoteMessage.getData().get("date");
            pdflink = remoteMessage.getData().get("pdflink");
            //linktype = remoteMessage.getData().get("linktype");
            getfirebasenotification(title,message,type);

             class SaveTask extends AsyncTask<Void, Void, Void> {

                 @Override
                 protected Void doInBackground(Void... voids) {

                     //adding to database
                     taskSync.setTitle(title);
                     taskSync.setMessage(message);
                     taskSync.setType(type);
                     taskSync.setDate(date);
                     taskSync.setPdflink(pdflink);
                     taskSync.setValue("unread");
                     //taskSync.setLinktype(linktype);

                     DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                             .taskDao()
                             .insert(taskSync);
                     return null;
                 }

                 @Override
                 protected void onPostExecute(Void aVoid) {
                     super.onPostExecute(aVoid);
                     //finish();

                 }
             }

             SaveTask st = new SaveTask();
             st.execute();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getfirebasenotification(String title, String body, String type) {
        //unread++;
        Ringtone r = RingtoneManager.getRingtone(this.getApplicationContext(), sound);
        r.play();
        @SuppressLint("ResourceAsColor") NotificationCompat.Builder builder = new NotificationCompat.Builder(MyFirebaseMessagingService.this,channelId)
                .setSmallIcon(R.drawable.ic_safelogo)
                .setColor(R.color.purple_200)

                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(title))

                .setContentText(body)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setAutoCancel(true);

        if (type.equals("InspectionActivity")){
            intent = new Intent(MyFirebaseMessagingService.this, InspectionActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }else if (type.equals("CleaningMaintenanceActivity")) {
            intent = new Intent(MyFirebaseMessagingService.this,CleaningMaintenanceActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        else if (type.equals("FsmsdocumentActivity")) {
            intent = new Intent(MyFirebaseMessagingService.this, FsmsdocumentActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        else if (type.equals("NotificationActivity")) {
            intent = new Intent(MyFirebaseMessagingService.this, NotificationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }else {
            if (Uerrolid.equalsIgnoreCase("2")){
                intent = new Intent(MyFirebaseMessagingService.this, AdminMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            }
            else if (Uerrolid.equalsIgnoreCase("3")){
                intent = new Intent(MyFirebaseMessagingService.this, UserMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            }
        }
        final int not_nu=generateRandom();
        PendingIntent pendingIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this,
                not_nu,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        int importance = NotificationManager. IMPORTANCE_HIGH ;
        NotificationChannel notificationChannel = new
                NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;

        notificationManager.createNotificationChannel(notificationChannel) ;

        notificationManager.notify(not_nu,builder.build());

    }
    public int generateRandom(){
        Random random = new Random();
        return random.nextInt(9999 - 1000) + 1000;
    }
    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
