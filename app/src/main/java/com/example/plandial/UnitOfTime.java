package com.example.plandial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public enum UnitOfTime {
    MINUTE(60),
    HOUR(3600),
    DAY(86400),
    WEEK(604800),
    MONTH(2592000);    // 30일 기준으로 환산함

    private final int seconds;

    public final static ArrayList<String> unitNames = new ArrayList<>(Arrays.asList("분", "시간", "일", "주", "개월"));
    public final static ArrayList<String> unitEnglishNames = new ArrayList<>(Arrays.asList("MINUTE", "HOUR", "DAY", "WEEK", "MONTH"));
    public final static HashMap<String, UnitOfTime> nameToUnit = new HashMap<String, UnitOfTime>() {{
        put(unitNames.get(0), MINUTE);
        put(unitNames.get(1), HOUR);
        put(unitNames.get(2), DAY);
        put(unitNames.get(3), WEEK);
        put(unitNames.get(4), MONTH);
    }};
    public final static HashMap<String, UnitOfTime> EnglishNameToUnit = new HashMap<String, UnitOfTime>() {{
        put(unitEnglishNames.get(0), MINUTE);
        put(unitEnglishNames.get(1), HOUR);
        put(unitEnglishNames.get(2), DAY);
        put(unitEnglishNames.get(3), WEEK);
        put(unitEnglishNames.get(4), MONTH);
    }};
    public final static HashMap<Object, Object> unitToIndex = new HashMap<Object, Object>() {{
        put(MINUTE, 0);
        put(HOUR, 1);
        put(DAY, 2);
        put(WEEK, 3);
        put(MONTH, 4);
    }};

    public final static int MILLIS_PER_SECOND = 1000;
    public final static int SECONDS_PER_MINUTE = 60;

    UnitOfTime(final int seconds) {
        this.seconds = seconds;
    }

    public long getMillis() {
        return (long) this.seconds * MILLIS_PER_SECOND;
    }

    public int getSeconds() {
        return this.seconds;
    }

    public int getMinutes() {
        return this.seconds / SECONDS_PER_MINUTE;
    }
}
