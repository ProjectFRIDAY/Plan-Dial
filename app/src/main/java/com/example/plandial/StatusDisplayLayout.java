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

    public void displayDial(Dial dial) {
        this.nameView.setText(dial.getName());
        this.timeView.setText(dial.getEndDateTime().toString());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.nameView = findViewById(R.id.selected_dial_name);
        this.timeView = findViewById(R.id.selected_dial_time);
    }
}
