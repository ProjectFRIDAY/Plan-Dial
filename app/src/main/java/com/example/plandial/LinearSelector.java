package com.example.plandial;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class LinearSelector extends ConstraintLayout {
    private ImageView circle;
    private static final int START_X = 10;
    private static final int END_X = 725;
    private static final int UNIT_COUNT = 4;
    private int selected = 0;


    public LinearSelector(Context context) {
        super(context);
    }

    public LinearSelector(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public int getSelected() {
        return this.selected;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.d("test", "finish inflate selector!");

        circle = findViewById(R.id.unit_circle);
        circle.setX(START_X);   // 초기 위치 설정

        // 터치 이벤트 설정
        circle.setOnTouchListener((view, motionEvent) -> {
            float xPos = motionEvent.getRawX() - circle.getWidth() / 2 - this.getX();

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_MOVE:

                    if (xPos < START_X) {
                        xPos = START_X;
                    } else if (xPos > END_X) {
                        xPos = END_X;
                    }

                    circle.setX(xPos);
                    break;
                case MotionEvent.ACTION_UP:
                    float interval = (END_X - START_X) / (float) (UNIT_COUNT - 1);
                    int index = Math.round(xPos / interval);

                    circle.setX(START_X + interval * index);
                    // 자석 기능 추가해야 함.

                    selected = index;
                    break;
            }

            return true;
        });
    }
}
