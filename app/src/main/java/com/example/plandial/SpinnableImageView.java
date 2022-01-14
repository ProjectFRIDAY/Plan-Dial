package com.example.plandial;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import androidx.annotation.Nullable;

public class SpinnableImageView extends androidx.appcompat.widget.AppCompatImageView {
    protected static final double INITIAL_DEGREE = 180;

    protected double mCurrentDegree = 0;
    protected Pair<Double, Double> mLastTouchLocation = new Pair<>(0.0, 0.0);

    public SpinnableImageView(Context context) {
        super(context);
        reset(0);
    }

    public SpinnableImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        reset(0);
    }

    public void reset(final long duration) {
        rotate(mCurrentDegree, INITIAL_DEGREE, duration);
        this.mCurrentDegree = INITIAL_DEGREE;
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        // 그림의 중심을 원점으로 하여 터치된 위치의 벡터를 구함.
        double x = event.getX() - (double) this.getWidth() / 2;
        double y = (double) this.getHeight() / 2 - event.getY();

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            // 벡터의 내적을 구해 각도를 판정함.
            double dotResult = mLastTouchLocation.first * x + mLastTouchLocation.second * y;
            double theta = Math.toDegrees(Math.acos(dotResult / (Math.sqrt(x * x + y * y) * Math.sqrt(mLastTouchLocation.first * mLastTouchLocation.first + mLastTouchLocation.second * mLastTouchLocation.second))));

            // 벡터의 외적을 구해 회전 방향을 판정함.
            double crossResult = mLastTouchLocation.first * y - mLastTouchLocation.second * x;
            int direction = (crossResult > 0) ? -1 : 1;

            double nextDegree = this.mCurrentDegree + direction * theta;

            // nextDegree를 [0, 360)의 값으로 만듦.
            if (nextDegree < 0) {
                nextDegree += (1 - (int) (nextDegree / 360)) * 360;
            } else if (nextDegree >= 360) {
                nextDegree %= 360;
            }

            rotate(mCurrentDegree, nextDegree, 0);
            mCurrentDegree = nextDegree;
        }

        mLastTouchLocation = new Pair<>(x, y);
        return true;
    }

    protected void rotate(final double fromDegree, final double toDegree, final long duration) {
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