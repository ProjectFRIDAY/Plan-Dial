package com.example.plandial;

import android.content.Context;
import android.util.AttributeSet;

import java.time.OffsetDateTime;
import java.util.IllegalFormatException;

public class DateTimeTextView extends FormatTextView implements IOffsetDateTimeRequester{
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

        try {
            this.setText(String.format(formatString, year, month, day, hour, minute));
        } catch (IllegalFormatException | NullPointerException exception) {
            this.setText(String.format("%d %d %d %d %d", year, month, day, hour, minute));
            assert (false);
            return false;
        }

        return true;
    }
}
