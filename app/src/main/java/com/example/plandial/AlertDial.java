package com.example.plandial;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.plandial.db.WorkDatabase;

import java.time.OffsetDateTime;

public class AlertDial extends Dial {
    private static final IconRecommendation iconRecommendation = new IconRecommendation();

    private boolean disable = false;
    private static int id = 0;
    private PendingIntent pushIntent;
    private OffsetDateTime startDateTime;

    // 다이얼 첫 생성시 -> 아이콘 추천
    @RequiresApi(api = Build.VERSION_CODES.S)
    public AlertDial(final Context context, final String name, final Period period, final OffsetDateTime startDateTime) {
        super(name, iconRecommendation.getIconByName(context, name), period);
        this.startDateTime = startDateTime;
        makeAlarm(context);
    }

    // 다이얼 DB에서 가져올 시 -> 아이콘 불러오기
    @RequiresApi(api = Build.VERSION_CODES.S)
    public AlertDial(final Context context, final String name, final Period period, final OffsetDateTime startDateTime, int icon) {
        super(name, icon, period);
        this.startDateTime = startDateTime;
        makeAlarm(context);
    }

    // for test
    public AlertDial(final String name, final Period period, final OffsetDateTime startDateTime) {
        super(name, R.drawable.baseline_question_mark_black, period);
        this.startDateTime = startDateTime;
    }

    public void setName(final Context context, final String name) {
        setIcon(iconRecommendation.getIconByName(context, name));
        setName(name);
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
        disable = true;
        if (pushIntent != null) {
            AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmMgr.cancel(pushIntent);
            pushIntent = null;
        }
    }

    // 비활성화 off
    @RequiresApi(api = Build.VERSION_CODES.S)
    public void restart(final Context context) {
        disable = false;
        this.startDateTime = OffsetDateTime.now();
        makeAlarm(context);
    }

    public boolean isDisabled() {
        return disable;
    }
}