package com.example.plandial;

import android.content.Context;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import androidx.annotation.Nullable;

public class SpinnableImageView extends androidx.appcompat.widget.AppCompatImageView {
    private static final double INITIAL_DEGREE = 0;
    private static final int DEGREE_BETWEEN_CATEGORY = 30;
    private static final int MAGNET_STRENGTH = 50;

    private double mCurrentDegree = 0;
    private Pair<Double, Double> lastTouchLocation = new Pair<>(0.0, 0.0);

    public SpinnableImageView(Context context) {
        super(context);
    }

    public SpinnableImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void reset(final long duration) {
        rotate(mCurrentDegree, INITIAL_DEGREE, duration);
        this.mCurrentDegree = INITIAL_DEGREE;
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        // 메인 다이얼 그림의 중심을 원점으로 하여 터치된 위치의 벡터를 구함.
        double x = event.getX() - (double) this.getWidth() / 2;
        double y = (double) this.getHeight() / 2 - event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                // 벡터의 내적을 구해 각도를 판정함.
                double dotResult = lastTouchLocation.first * x + lastTouchLocation.second * y;
                double theta = Math.toDegrees(Math.acos(dotResult / (Math.sqrt(x * x + y * y) * Math.sqrt(lastTouchLocation.first * lastTouchLocation.first + lastTouchLocation.second * lastTouchLocation.second))));

                // 벡터의 외적을 구해 회전 방향을 판정함.
                double crossResult = lastTouchLocation.first * y - lastTouchLocation.second * x;
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

                break;
            case MotionEvent.ACTION_UP: {
                // 가장 가까운 각도의 카테고리를 찾음
                double categoryIndex = (int) (mCurrentDegree / DEGREE_BETWEEN_CATEGORY)
                        + (int) ((mCurrentDegree % DEGREE_BETWEEN_CATEGORY) / (DEGREE_BETWEEN_CATEGORY / 2));

                double lastDegree = categoryIndex * DEGREE_BETWEEN_CATEGORY;
                rotate(mCurrentDegree, lastDegree, MAGNET_STRENGTH);
                mCurrentDegree = lastDegree;

                Vibrator vibrator = (Vibrator) this.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(5); // 0.005초간 진동

                break;
            }
        }

        lastTouchLocation = new Pair<>(x, y);
        return true;
    }

    private void rotate(final double fromDegree, final double toDegree, final long duration) {
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