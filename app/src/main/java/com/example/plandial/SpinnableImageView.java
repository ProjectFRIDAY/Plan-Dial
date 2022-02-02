package com.example.plandial;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import androidx.annotation.Nullable;

public class SpinnableImageView extends androidx.appcompat.widget.AppCompatImageView {
    protected static final float INITIAL_DEGREE = 180;

    protected float currentDegree = 0;
    protected float lastX = 0.0f;
    protected float lastY = 0.0f;

    public SpinnableImageView(Context context) {
        super(context);
        reset(0);
    }

    public SpinnableImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        reset(0);
    }

    public void reset(final long duration) {
        rotate(currentDegree, INITIAL_DEGREE, duration);
        this.currentDegree = INITIAL_DEGREE;
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        // 그림의 중심을 원점으로 하여 터치된 위치의 벡터를 구함.
        float x = event.getX() - (float) this.getWidth() / 2;
        float y = (float) this.getHeight() / 2 - event.getY();

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            // 벡터의 내적을 구해 각도를 판정함.
            float theta = (float) Math.toDegrees(Math.acos((lastX * x + lastY * y) / (Math.sqrt(x * x + y * y) * Math.sqrt(lastX * lastX + lastY * lastY))));

            // 벡터의 외적을 구해 회전 방향을 판정함.
            int direction = (lastX * y - lastY * x > 0) ? -1 : 1;

            float nextDegree = this.currentDegree + direction * theta;

            // nextDegree를 [0, 360)의 값으로 만듦.
            if (nextDegree < 0) {
                nextDegree += (1 - (int) (nextDegree / 360)) * 360;
            } else if (nextDegree >= 360) {
                nextDegree %= 360;
            }

            rotate(currentDegree, nextDegree, 0);
            currentDegree = nextDegree;
        }

        this.lastX = x;
        this.lastY = y;

        return true;
    }

    protected void rotate(final float fromDegree, final float toDegree, final long duration) {
        final RotateAnimation rotateAnimation = new RotateAnimation(
                (float) fromDegree,
                (float) toDegree,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        rotateAnimation.setDuration(duration);
        rotateAnimation.setFillAfter(true);

        this.startAnimation(rotateAnimation);
    }
}