package com.example.plandial;

import android.content.Context;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class SpinnableDialView extends SpinnableImageView {
    private static final double DEGREE_BETWEEN_CATEGORY = 30;
    private static final int MAGNET_STRENGTH = 50;
    private static final int VIBRATE_STRENGTH = 4;
    private static final double DISTANCE_TO_CIRCLE = 210.0;

    private ArrayList<ImageView> mCircles;
    private final DialManager mDialManager = DialManager.getInstance();
    private int mSelectedCategoryIndex = 0;

    Vibrator mVibrator = (Vibrator) this.getContext().getSystemService(Context.VIBRATOR_SERVICE);

    public SpinnableDialView(Context context) {
        super(context);
    }

    public SpinnableDialView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!super.onTouchEvent(event)) {
            return false;
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            // 가장 가까운 각도의 카테고리를 찾음
            int categoryIndex = (int) (mCurrentDegree / DEGREE_BETWEEN_CATEGORY)
                    + (int) ((mCurrentDegree % DEGREE_BETWEEN_CATEGORY) / (DEGREE_BETWEEN_CATEGORY / 2));

            this.mSelectedCategoryIndex = (categoryIndex - (int) (INITIAL_DEGREE / DEGREE_BETWEEN_CATEGORY) + 12) % 12;
            double lastDegree = categoryIndex * DEGREE_BETWEEN_CATEGORY;
            super.rotate(mCurrentDegree, lastDegree, MAGNET_STRENGTH);
            mCurrentDegree = lastDegree;

            this.mVibrator.vibrate(VIBRATE_STRENGTH); // 0.004초간 진동}
        }

        arrangeCircles();

        return true;
    }

    public int getSelectedCategoryNumber() {
        return this.mSelectedCategoryIndex;
    }

    public void setCircles(Context context, ArrayList<ImageView> circles) {
        this.mCircles = circles;

        circles.get(0).setColorFilter(ContextCompat.getColor(context, R.color.category_color_0));
        circles.get(1).setColorFilter(ContextCompat.getColor(context, R.color.category_color_1));
        circles.get(2).setColorFilter(ContextCompat.getColor(context, R.color.category_color_2));
        circles.get(3).setColorFilter(ContextCompat.getColor(context, R.color.category_color_3));
        circles.get(4).setColorFilter(ContextCompat.getColor(context, R.color.category_color_4));
        circles.get(5).setColorFilter(ContextCompat.getColor(context, R.color.category_color_5));
        circles.get(6).setColorFilter(ContextCompat.getColor(context, R.color.category_color_6));
        circles.get(7).setColorFilter(ContextCompat.getColor(context, R.color.category_color_7));
        circles.get(8).setColorFilter(ContextCompat.getColor(context, R.color.category_color_8));
        circles.get(8).setColorFilter(ContextCompat.getColor(context, R.color.category_color_9));

        for (int i = mDialManager.getCategoryCount(); i < mCircles.size(); ++i) {
            circles.get(i).setColorFilter(ContextCompat.getColor(context, R.color.empty_color));
        }

        arrangeCircles();
    }

    private void arrangeCircles() {
        for (int i = 0; i < mCircles.size(); ++i) {
            mCircles.get(i).setX((float) (-mCircles.get(i).getWidth() / 2 + this.getX() + getWidth() / 2 + DISTANCE_TO_CIRCLE * Math.sin(Math.toRadians(mCurrentDegree - i * DEGREE_BETWEEN_CATEGORY))));
            mCircles.get(i).setY((float) (-mCircles.get(i).getHeight() / 2 + this.getY() + getHeight() / 2 - DISTANCE_TO_CIRCLE * Math.cos(Math.toRadians(mCurrentDegree - i * DEGREE_BETWEEN_CATEGORY))));
        }
    }
}