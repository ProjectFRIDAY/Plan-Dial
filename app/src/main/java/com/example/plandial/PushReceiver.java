package com.example.plandial;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class PushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // 재부팅 대응 필요
        String dialName = intent.getStringExtra("dial");

        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(
                new NotificationChannel("push", "push-alarm", NotificationManager.IMPORTANCE_HIGH)
        );
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "push");

        Intent goMainIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,101, goMainIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentTitle(dialName);
        builder.setContentText("지금은 " + dialName + "을(를) 시작할 시간입니다.");
        builder.setSmallIcon(R.drawable.ic_logo);
        builder.setAutoCancel(true);

        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        manager.notify(1, notification);
    }
}
