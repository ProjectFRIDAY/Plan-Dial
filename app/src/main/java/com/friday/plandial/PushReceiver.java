package com.friday.plandial;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.NotificationCompat;

import com.example.plandial.R;

public class PushReceiver extends BroadcastReceiver {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {

        // 다이얼 이름 받음
        String dialName = intent.getStringExtra("dial");

        if (dialName != null) {
            // 푸쉬알림에 대한 내용
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(
                    new NotificationChannel("push", "다이얼 알림", NotificationManager.IMPORTANCE_HIGH)
            );
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "push");

            Intent goMainIntent = new Intent(context, SplashActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, -1, goMainIntent, PendingIntent.FLAG_IMMUTABLE);

            builder.setContentTitle(dialName);
            builder.setContentText("\uD83D\uDCE2 지금은 " + dialName + "을(를) 시작할 시간입니다.");
            builder.setSmallIcon(R.drawable.ic_logo);
            builder.setAutoCancel(true);

            builder.setContentIntent(pendingIntent);

            Notification notification = builder.build();
            manager.notify(1, notification);
        }
    }

    // 재부팅 대응 필요
    public static class ReBootReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

                ComponentName receiver = new ComponentName(context, ReBootReceiver.class);
                PackageManager pm = context.getPackageManager();

                pm.setComponentEnabledSetting(receiver,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);

            }
        }
    }
}
