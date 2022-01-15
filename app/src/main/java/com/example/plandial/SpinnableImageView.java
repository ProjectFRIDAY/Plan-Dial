package com.example.plandial;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import androidx.annotation.Nullable;

public class SpinnableImageView extends androidx.appcompat.widget.AppCompatImageView {
    private static final double INITIAL_DEGREE = 0;

    private double mCurrentDegree = 0;
    private Pair<Double, Double> lastTouchLocation = new Pair<>(0.0, 0.0);

    public SpinnableImageView(Context context) {
        super(context);
    }

    public SpinnableImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void reset() {
        this.mCurrentDegree = INITIAL_DEGREE;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        double x = event.getX() - (double) this.getWidth() / 2;
        double y = (double) this.getHeight() / 2 - event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                double dotResult = lastTouchLocation.first * x + lastTouchLocation.second * y;
                double crossResult = lastTouchLocation.first * y - lastTouchLocation.second * x;

                int direction = (crossResult > 0) ? -1 : 1;

                double theta = direction * Math.toDegrees(Math.acos(dotResult / (Math.sqrt(x * x + y * y) * Math.sqrt(lastTouchLocation.first * lastTouchLocation.first + lastTouchLocation.second * lastTouchLocation.second))));
                double newDegree = this.mCurrentDegree + theta;

                rotate(mCurrentDegree, newDegree);
                mCurrentDegree = newDegree;

                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                assert (false);
                break;
        }

        lastTouchLocation = new Pair<>(x, y);
        return true;
    }

    private void rotate(double fromDegree, double toDegree) {
        final RotateAnimation rotateAnimation = new RotateAnimation(
                (float) fromDegree,
                (float) toDegree,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        rotateAnimation.setDuration(0);
        rotateAnimation.setFillAfter(true);

        this.startAnimation(rotateAnimation);
    }
}