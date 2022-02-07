package com.example.plandial;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.time.OffsetDateTime;

public class Dial {
    private static int id = 0;

    private String name;
    private int icon = -1;
    private Period period;
    private OffsetDateTime startDateTime;
    private boolean disable = false;

    private PendingIntent pushIntent;
    private static final IconRecommendation iconRecommendation = new IconRecommendation();

    // 다이얼 첫 생성시 -> 아이콘 추천
    public Dial(final Context context, final String name, final Period period, final OffsetDateTime startDateTime) {
        makeDial(name, period, startDateTime);
        icon = iconRecommendation.getIconByName(context, name);
        makeAlarm(context);
    }

    // 다이얼 DB에서 가져올 시 -> 아이콘 불러오기
    public Dial(final Context context, final String name, final Period period, final OffsetDateTime startDateTime, int icon) {
        makeDial(name, period, startDateTime);
        this.icon = icon;
        makeAlarm(context);
    }

    // for test
    public Dial(final String name, final Period period, final OffsetDateTime startDateTime) {
        makeDial(name, period, startDateTime);
    }

    private void makeDial(final String name, final Period period, final OffsetDateTime startDateTime) {
        assert name != null;
        assert period != null;
        assert startDateTime != null;

        this.name = name;
        this.period = period;
        this.startDateTime = startDateTime;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final Context context, final String name) {
        icon = iconRecommendation.getIconByName(context, name);
        this.name = name;
    }

    public int getIcon() {
        return this.icon;
    }

    public void setPeriod(final Period period) {
        this.period = period;
    }

    public OffsetDateTime getStartDateTime() {
        return this.startDateTime;
    }

    public void setStartDateTime(final OffsetDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public long getLeftTimeInMillis() {
        long nowInMillis = System.currentTimeMillis();
        long startInMillis = startDateTime.toEpochSecond() * UnitOfTime.MILLIS_PER_SECOND;

        if (nowInMillis > startInMillis) {
            long minus = nowInMillis - startInMillis;
            long times = minus / period.getPeriodInMillis() + 1;
            return period.getPeriodInMillis() * times - minus;
        } else {
            return startInMillis - nowInMillis;
        }
    }

    public int getLeftTimeInSeconds() {
        return (int) (getLeftTimeInMillis() / UnitOfTime.MILLIS_PER_SECOND);
    }


    private void makeAlarm(final Context context) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, PushReceiver.class);
        intent.putExtra("dial", name);

        pushIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_MUTABLE);

        long addedTimeInMillis = getLeftTimeInMillis();
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + addedTimeInMillis, period.getPeriodInMillis(), pushIntent);
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
    public void restart(final Context context) {
        disable = false;
        this.startDateTime = OffsetDateTime.now();
        makeAlarm(context);
    }

    public boolean isDisabled() {
        return disable;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + name.hashCode();
        result = 31 * result + icon;
        result = 31 * result + period.hashCode();
        result = 31 * result + startDateTime.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Dial) || this.hashCode() != obj.hashCode()) {
            return false;
        }

        Dial other = (Dial) obj;
        return this.name.equals(other.name)
                && this.icon == other.icon
                && this.period.equals(other.period)
                && this.startDateTime.equals(other.startDateTime);
    }
}