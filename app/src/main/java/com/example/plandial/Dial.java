package com.example.plandial;

import java.time.OffsetDateTime;

public class Dial {
    private String name;
    private String iconPath;
    private Period period;
    private OffsetDateTime startDateTime;

    public Dial(final Period period, final OffsetDateTime startDateTime, final String name) {
        assert name != null;
        assert period != null;
        assert startDateTime != null;

        this.name = name;
        this.iconPath = null;   // 임시로 작성한 코드임. 아이콘 자동 선택 로직으로 대체해야 함.
        this.period = period;
        this.startDateTime = startDateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;

        // 아이콘 자동 선택을 통해 아이콘 변경
    }

    public String getIconPath() {
        return iconPath;
    }

    public OffsetDateTime getStartDateTime() {
        return startDateTime;
    }

    public OffsetDateTime getEndDateTime() {
        return startDateTime.plusSeconds(period.getPeriodInSeconds());
    }
}