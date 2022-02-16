package com.friday.plandial;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import java.time.OffsetDateTime;

public class DateTimePickerDialog {
    private OffsetDateTime selectedDateTime;

    private final DatePickerDialog datePickerDialog;
    private final TimePickerDialog timePickerDialog;

    public DateTimePickerDialog(Context context, IOffsetDateTimeRequester offsetDateTimeRequester, OffsetDateTime originalDateTime) {

        DatePickerDialog.OnDateSetListener datePickerCallbackMethod = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                selectedDateTime = originalDateTime.withYear(year).withMonth(month + 1).withDayOfMonth(dayOfMonth);

                // TimePickerDialog를 연달아 호출함
                timePickerDialog.show();
            }
        };

        TimePickerDialog.OnTimeSetListener timePickerCallbackMethod = (timePicker, hour, minute) -> {
            selectedDateTime = selectedDateTime.withHour(hour).withMinute(minute);

            offsetDateTimeRequester.setByOffsetDateTime(selectedDateTime);
        };

        datePickerDialog = new DatePickerDialog(
                context,
                datePickerCallbackMethod,
                OffsetDateTime.now().getYear(),
                OffsetDateTime.now().getMonthValue() - 1,
                OffsetDateTime.now().getDayOfMonth());

        timePickerDialog = new TimePickerDialog(
                context,
                timePickerCallbackMethod,
                OffsetDateTime.now().getHour(),
                OffsetDateTime.now().getMinute(),
                false);
    }

    public DateTimePickerDialog(Activity activity, DateTimeTextView startDayInput) {
        this(activity, startDayInput, OffsetDateTime.now());
    }

    public void show() {
        datePickerDialog.show();
    }
}
