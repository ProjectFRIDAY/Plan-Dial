package com.example.plandial;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.OffsetDateTime;

public class AlertDial extends Dial {
    private static int id = 0;
    private PendingIntent pushIntent;
    private OffsetDateTime startDateTime;

    @RequiresApi(api = Build.VERSION_CODES.S)
    public AlertDial(final Context context, final String name, final Period period, final OffsetDateTime startDateTime) {
        super(name, R.drawable.baseline_question_mark_black, period);
        this.startDateTime = startDateTime;
        makeAlarm(context);
    }

    public OffsetDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(OffsetDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public long getLeftTimeInMillis() {
        long nowInMillis = System.currentTimeMillis();
        long startInMillis = this.startDateTime.toEpochSecond() * UnitOfTime.MILLIS_PER_SECOND;

        if (nowInMillis > startInMillis) {
            long minus = nowInMillis - startInMillis;
            int times = (int) (minus / getPeriod().getPeriodInMillis()) + 1;
            return getPeriod().getPeriodInMillis() * times - minus;
        } else {
            return startInMillis;
        }
    }

    public long getLeftTimeInSeconds() {
        return getLeftTimeInMillis() / UnitOfTime.MILLIS_PER_SECOND;
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void makeAlarm(final Context context) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, PushReceiver.class);
        intent.putExtra("dial", getName());

        pushIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_MUTABLE);

        long addedTimeInMillis = getLeftTimeInMillis();
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + addedTimeInMillis, getPeriod().getPeriodInMillis(), pushIntent);
        id++;
    }

    // 비활성화 on
    public void disable(final Context context) {
        if (pushIntent != null) {
            AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmMgr.cancel(pushIntent);
            pushIntent = null;
        }
    }

    // 비활성화 off
    @RequiresApi(api = Build.VERSION_CODES.S)
    public void restart(final Context context) {
        setStartDateTime(OffsetDateTime.now());
        makeAlarm(context);
    }
}