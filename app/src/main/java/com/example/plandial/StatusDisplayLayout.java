package com.example.plandial;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class StatusDisplayLayout extends LinearLayout {
    TextView nameView;
    TextView timeView;

    public StatusDisplayLayout(Context context) {
        super(context);
    }

    public StatusDisplayLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void displayDial(AlertDial alertDial) {
        this.nameView.setText(alertDial.getName());
        this.timeView.setText(String.format("%d분 남음", alertDial.getLeftTimeInSeconds() / UnitOfTime.SECONDS_PER_MINUTE));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.nameView = findViewById(R.id.selected_dial_name);
        this.timeView = findViewById(R.id.selected_dial_time);
    }
}
