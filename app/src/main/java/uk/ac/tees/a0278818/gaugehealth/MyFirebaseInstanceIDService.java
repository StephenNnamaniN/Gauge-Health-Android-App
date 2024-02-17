package uk.ac.tees.a0278818.gaugehealth;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class MyFirebaseInstanceIDService  {

//    @Override
//    public void onMessageReceived(@NonNull RemoteMessage message) {
//        super.onMessageReceived(message);
//        getFirebaseMessage(message.getNotification().getTitle(), message.getNotification().getBody());
//    }
//
//    public void getFirebaseMessage(String title, String msg) {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "myFirebaseChannel")
//                .setSmallIcon(R.drawable.sharp_circle_notifications_24)
//                .setContentTitle(title)
//                .setContentText(msg)
//                .setAutoCancel(true);
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            NotificationChannel notificationChannel = new NotificationChannel(
//                    channel_id,
//            )
//        }
//
//        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
//        manager.notify(101, builder.build());
//    }
}
