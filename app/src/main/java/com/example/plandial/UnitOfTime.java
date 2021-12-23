package com.example.plandial;

public enum UnitOfTime {
    HOUR(3600),
    DAY(86400),
    WEEK(604800),
    MONTH(2592000);    // 30일 기준으로 환산함

    private final int seconds;

    public final static int MILLIS_PER_SECOND = 1000;
    public final static int SECONDS_PER_MINUTE = 60;

    UnitOfTime(final int seconds) {
        this.seconds = seconds;
    }

    public long getMillis() {
        return this.seconds * MILLIS_PER_SECOND;
    }

    public int getSeconds() {
        return this.seconds;
    }

    public int getMinutes() {
        return this.seconds / SECONDS_PER_MINUTE;
    }
}
