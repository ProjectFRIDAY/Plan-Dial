package com.example.plandial;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.OffsetDateTime;

public class Dial {
    private static int id = 0;

    private String name;
    private int icon;
    private Period period;
    private OffsetDateTime startDateTime;
    private PendingIntent pushIntent;

    @RequiresApi(api = Build.VERSION_CODES.S)
    public Dial(final Context context, final String name, final Period period, final OffsetDateTime startDateTime) {
        assert name != null;
        assert period != null;
        assert startDateTime != null;

        this.name = name;
        this.icon = R.drawable.baseline_question_mark_black;  // 임시로 작성한 코드임. 아이콘 자동 선택 로직으로 대체해야 함.
        this.period = period;
        this.startDateTime = startDateTime;

        makeAlarm(context);
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;

        // 아이콘 자동 선택을 통해 아이콘 변경
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
        long startInMillis = startDateTime.toEpochSecond() * 1000;

        if (nowInMillis > startInMillis) {
            long minus = nowInMillis - startInMillis;
            int times = (int) (minus / period.getPeriodInMillis()) + 1;
            return period.getPeriodInMillis() * times - minus;
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
        intent.putExtra("dial", name);

        pushIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_MUTABLE);

        long addedTimeInMillis = getLeftTimeInMillis();
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + addedTimeInMillis, period.getPeriodInMillis(), pushIntent);
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
        this.startDateTime = OffsetDateTime.now();
        makeAlarm(context);
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