/*package com.example.plandial;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class LinearSelector extends ConstraintLayout {
    private ImageView circle;
    private static final int START_X = 10;
    private static final int END_X = 725;
    private static final int UNIT_COUNT = 4;
    private int selectedIndex = 0;


    public LinearSelector(Context context) {
        super(context);
    }

    public LinearSelector(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public int getSelectedIndex() {
        return this.selectedIndex;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        circle = findViewById(R.id.unit_circle);

        // 터치 이벤트 설정
        circle.setOnTouchListener((view, motionEvent) -> {
            float xPos = getViewCenteredPostion(view, motionEvent.getRawX() - (this.getX() + START_X));

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    xPos = boundPosition(xPos);

                    // 손가락 따라 이동
                    circle.setX(xPos);
                    break;
                case MotionEvent.ACTION_UP:
                    xPos = boundPosition(xPos);

                    float interval = (END_X - START_X) / (float) (UNIT_COUNT - 1);
                    int index = Math.round(xPos / interval);
                    
                    // 자석 효과 애니메이션
                    ObjectAnimator magnetAnimation = ObjectAnimator.ofFloat(view, "translationX", interval * index);
                    magnetAnimation.setDuration(400);
                    magnetAnimation.start();

                    selectedIndex = index;
                    break;
            }

            return true;
        });
    }

    private float boundPosition(float pos) {
        // 좌표의 최소값과 최대값 제한
        if (pos < START_X) {
            return START_X;
        } else if (pos > END_X) {
            return END_X;
        }

        return pos;
    }

    private float getViewCenteredPostion(View view, float pos) {
        return pos - view.getWidth() / 2 + view.getPaddingLeft();
    }
}
*/