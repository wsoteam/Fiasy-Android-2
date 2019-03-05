package com.wsoteam.diet.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.wsoteam.diet.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmManagerBR extends BroadcastReceiver {
    public static String TAG_FOR_TEXT = "AlarmManagerBR_TAG_FOR_TEXT";
    public static String TAG_FOR_ID = "AlarmManagerBR_TAG_FOR_ID";

    @Override
    public void onReceive(Context context, Intent intent) {


        createNotification(context, intent.getStringExtra(TAG_FOR_TEXT), intent.getLongExtra(TAG_FOR_ID, 0));
    }

    private void createNotification(Context context, String textOfNotification, long id) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        String CHANNEL_ID = "channel_diet_notification_id";
        CharSequence name = "diets_notifications";
        NotificationCompat.Builder builder;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.enableVibration(true);
            mChannel.setShowBadge(true);
            mChannel.setVibrationPattern(new long[]{100, 100, 100, 100, 100});
            notificationManager.createNotificationChannel(mChannel);
            builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_round))
                    .setContentTitle(textOfNotification);
        } else {
            builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle(textOfNotification)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setVibrate(new long[]{100, 100, 100, 100, 100});
        }


        Notification notification = builder.build();


        notificationManager.notify((int) id, notification);
    }
}
