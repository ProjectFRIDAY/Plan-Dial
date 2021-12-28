package com.example.plandial;

import android.graphics.drawable.Icon;

import java.time.OffsetDateTime;

public class Dial {
    private String name;
    private String iconPath;
    private Period period;
    private OffsetDateTime startDateTime;

    public Dial(final String name, final Period period, final OffsetDateTime startDateTime) {
        assert name != null;
        assert period != null;
        assert startDateTime != null;

        this.name = name;
        this.iconPath = "icon/outline_shopping_cart_black_24dp.png";  // 임시로 작성한 코드임. 아이콘 자동 선택 로직으로 대체해야 함.
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