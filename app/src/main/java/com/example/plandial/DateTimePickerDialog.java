package com.example.plandial;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.time.OffsetDateTime;

public class DateTimePickerDialog {
    private OffsetDateTime selectedDateTime = OffsetDateTime.now();

    private final DatePickerDialog datePickerDialog;
    private final TimePickerDialog timePickerDialog;

    public DateTimePickerDialog(Context context, IOffsetDateTimeRequester offsetDateTimeRequester) {

        DatePickerDialog.OnDateSetListener datePickerCallbackMethod = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                selectedDateTime = selectedDateTime.withYear(year).withMonth(month + 1).withDayOfMonth(dayOfMonth);

                // TimePickerDialog를 연달아 호출함
                timePickerDialog.show();
            }
        };

        TimePickerDialog.OnTimeSetListener timePickerCallbackMethod = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                selectedDateTime = selectedDateTime.withHour(hour).withMinute(minute);

                offsetDateTimeRequester.setByOffsetDateTime(selectedDateTime);
            }
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
                true);
    }

    public void show() {
        datePickerDialog.show();
    }
}
