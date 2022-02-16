package com.friday.plandial;

import android.content.Context;
import android.util.AttributeSet;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.IllegalFormatException;
import java.util.Locale;

public class DateTimeTextView extends FormatTextView implements IOffsetDateTimeRequester {

    private OffsetDateTime dateTime;

    public DateTimeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean setByOffsetDateTime(OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null) {
            return false;
        }

        this.dateTime = offsetDateTime;

        int year = offsetDateTime.getYear();
        int month = offsetDateTime.getMonthValue();
        int day = offsetDateTime.getDayOfMonth();
        int hour = offsetDateTime.getHour();
        int minute = offsetDateTime.getMinute();

        String weekOfDay = offsetDateTime.format(DateTimeFormatter.ofPattern("E"));
        int hour12 = hour == 12 ? 12 : hour % 12;
        String amPm = offsetDateTime.getHour() < 12 ? "오전" : "오후";

        try {
            this.setText(String.format(formatString, year, month, day, weekOfDay, amPm, hour12, minute));
        } catch (IllegalFormatException | NullPointerException exception) {
            this.setText(String.format(Locale.KOREA, "%d %d %d %d %d", year, month, day, hour, minute));
            assert (false);
            return false;
        }

        return true;
    }

    public OffsetDateTime getDateTime() {
        return dateTime;
    }
}
