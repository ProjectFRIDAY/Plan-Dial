package com.example.plandial;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.Locale;

public class StatusDisplayLayout extends LinearLayout {
    TextView nameView;
    TextView timeView;

    public StatusDisplayLayout(Context context) {
        super(context);
    }

    public StatusDisplayLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void displayDial(AlertDial dial) {
        this.nameView.setText(dial.getName());
        int leftTime = Long.valueOf(dial.getLeftTimeInSeconds()).intValue();

        if (dial.isDisabled()) {
            this.timeView.setText("비활성화됨");
        } else if (leftTime > UnitOfTime.MONTH.getSeconds()) {
            this.timeView.setText(String.format(Locale.KOREA, "%d개월 남음", leftTime / UnitOfTime.MONTH.getSeconds()));
        } else if (UnitOfTime.WEEK.getSeconds() <= leftTime) {
            this.timeView.setText(String.format(Locale.KOREA, "%d주 남음", leftTime / UnitOfTime.WEEK.getSeconds()));
        } else if (UnitOfTime.DAY.getSeconds() <= leftTime) {
            this.timeView.setText(String.format(Locale.KOREA, "%d일 남음", leftTime / UnitOfTime.DAY.getSeconds()));
            Log.e("asd", String.valueOf(UnitOfTime.DAY.getSeconds()));
        } else if (UnitOfTime.HOUR.getSeconds() <= leftTime) {
            this.timeView.setText(String.format(Locale.KOREA, "%d시간 남음", leftTime / UnitOfTime.HOUR.getSeconds()));
        } else if (UnitOfTime.MINUTE.getSeconds() <= leftTime) {
            this.timeView.setText(String.format(Locale.KOREA, "%d분 남음", leftTime / UnitOfTime.MINUTE.getSeconds()));
        } else if (0 <= leftTime) {
            this.timeView.setText(String.format(Locale.KOREA, "%d초 남음", leftTime));
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.nameView = findViewById(R.id.selected_dial_name);
        this.timeView = findViewById(R.id.selected_dial_time);
    }
}
