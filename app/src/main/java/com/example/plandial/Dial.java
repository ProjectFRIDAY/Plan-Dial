package com.example.plandial;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.time.OffsetDateTime;
import java.util.Calendar;

public class Dial{
    private static int id = 0;
    private String name;
    private String iconPath;
    private Period period;
    private OffsetDateTime startDateTime;
    private PendingIntent pushIntent;

    public Dial(final Context context, final String name, final Period period, final OffsetDateTime startDateTime)  {
        assert name != null;
        assert period != null;
        assert startDateTime != null;

        this.name = name;
        this.iconPath = "icon/outline_shopping_cart_black_24dp.png";  // 임시로 작성한 코드임. 아이콘 자동 선택 로직으로 대체해야 함.
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

    public String getIconPath() {
        return this.iconPath;
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

    public OffsetDateTime getEndDateTime() {
        return startDateTime.plusSeconds(period.getPeriodInSeconds());
    }

    private void makeAlarm(final Context context){
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, PushReceiver.class);
        intent.putExtra("dial", name);
        pushIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_IMMUTABLE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, startDateTime.getHour());
        calendar.set(Calendar.MINUTE, startDateTime.getMinute());
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), period.getPeriodInMillis(), pushIntent);
        id++;
    }

    // 비활성화 on
    public void disable(final Context context){
        if(pushIntent != null){
            AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            alarmMgr.cancel(pushIntent);
            pushIntent = null;
        }
    }

    // 비활성화 off
    public void restart(final Context context) {
        this.startDateTime = OffsetDateTime.now();
        makeAlarm(context);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + name.hashCode();
        result = 31 * result + iconPath.hashCode();
        result = 31 * result + period.hashCode();
        result = 31 * result + startDateTime.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || !(obj instanceof Dial) || this.hashCode() != obj.hashCode()) {
            return false;
        }

        Dial other = (Dial) obj;
        return this.name.equals(other.name)
                && this.iconPath.equals(other.iconPath)
                && this.period.equals(other.period)
                && this.startDateTime.equals(other.startDateTime);
    }
}