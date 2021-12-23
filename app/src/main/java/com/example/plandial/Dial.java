package com.example.plandial;

import android.graphics.drawable.Icon;

import java.time.OffsetDateTime;

public class Dial {
    private String name;
    private Icon icon;
    private Period period;
    private OffsetDateTime startDateTime;

    public Dial(final String name, final Period period, final OffsetDateTime startDateTime) {
        assert name != null;
        assert period != null;
        assert startDateTime != null;

        this.name = name;
        this.icon = null;   // 임시로 작성한 코드임. 아이콘 자동 선택 로직으로 대체해야 함.
        this.period = period;
        this.startDateTime = startDateTime;
    }

    public void restart() {
        this.startDateTime = OffsetDateTime.now();
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;

        // 아이콘 자동 선택을 통해 아이콘 변경
    }

    public Icon getIcon() {
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

    public OffsetDateTime getEndDateTime() {
        return startDateTime.plusSeconds(period.getPeriodInSeconds());
    }
}