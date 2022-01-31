package com.example.plandial;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import java.time.OffsetDateTime;
import java.util.IllegalFormatException;

import androidx.annotation.Nullable;

public class FormatTextView extends androidx.appcompat.widget.AppCompatTextView {
    private final String formatString;

    public FormatTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedAttrs = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.FormatString,
                0,
                0
        );

        this.formatString = typedAttrs.getString(R.styleable.FormatString_formatString);

        // recycle TypedArray
        typedAttrs.recycle();
    }

    public boolean setTextByOffsetDateTime(OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null) {
            return false;
        }

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
