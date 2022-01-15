package com.example.plandial;

import android.content.Context;
import android.os.VibrationEffect;
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

    private ArrayList<ImageView> circles;
    private final DialManager dialManager = DialManager.getInstance();
    private int selectedCategoryIndex = 0;

    Vibrator vibrator = (Vibrator) this.getContext().getSystemService(Context.VIBRATOR_SERVICE);

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
            int categoryIndex = (int) (currentDegree / DEGREE_BETWEEN_CATEGORY)
                    + (int) ((currentDegree % DEGREE_BETWEEN_CATEGORY) / (DEGREE_BETWEEN_CATEGORY / 2));

            this.selectedCategoryIndex = (categoryIndex - (int) (INITIAL_DEGREE / DEGREE_BETWEEN_CATEGORY) + 12) % 12;
            CategoryDialAdapter.setCategory(dialManager.getCategoryByIndex(this.selectedCategoryIndex));

            double lastDegree = categoryIndex * DEGREE_BETWEEN_CATEGORY;
            super.rotate(currentDegree, lastDegree, MAGNET_STRENGTH);
            currentDegree = lastDegree;

            // 약한 진동을 발생함
            vibrator.vibrate(VibrationEffect.createOneShot(VIBRATE_STRENGTH, VibrationEffect.DEFAULT_AMPLITUDE));
        }

        arrangeCircles();

        return true;
    }

    public int getSelectedCategoryIndex() {
        // 선택된 카테고리의 인덱스를 반환함
        return this.selectedCategoryIndex;
    }

    public void setCircles(Context context, ArrayList<ImageView> circles) {
        this.circles = circles;

        circles.get(0).setColorFilter(ContextCompat.getColor(context, R.color.category_color_0));
        circles.get(1).setColorFilter(ContextCompat.getColor(context, R.color.category_color_1));
        circles.get(2).setColorFilter(ContextCompat.getColor(context, R.color.category_color_2));
        circles.get(3).setColorFilter(ContextCompat.getColor(context, R.color.category_color_3));
        circles.get(4).setColorFilter(ContextCompat.getColor(context, R.color.category_color_4));
        circles.get(5).setColorFilter(ContextCompat.getColor(context, R.color.category_color_5));
        circles.get(6).setColorFilter(ContextCompat.getColor(context, R.color.category_color_6));
        circles.get(7).setColorFilter(ContextCompat.getColor(context, R.color.category_color_7));
        circles.get(8).setColorFilter(ContextCompat.getColor(context, R.color.category_color_8));
        circles.get(9).setColorFilter(ContextCompat.getColor(context, R.color.category_color_9));

        for (int i = dialManager.getCategoryCount(); i < this.circles.size(); ++i) {
            circles.get(i).setColorFilter(ContextCompat.getColor(context, R.color.empty_color));
        }

        arrangeCircles();
    }

    private void arrangeCircles() {

        double distanceToCircle = 1.6 * this.getX() / 2;
        for (int i = 0; i < circles.size(); ++i) {
            circles.get(i).setX((float) (-circles.get(i).getWidth() / 2 + this.getX() + getWidth() / 2 + distanceToCircle * Math.sin(Math.toRadians(currentDegree - i * DEGREE_BETWEEN_CATEGORY))));
            circles.get(i).setY((float) (-circles.get(i).getHeight() / 2 + this.getY() + getHeight() / 2 - distanceToCircle * Math.cos(Math.toRadians(currentDegree - i * DEGREE_BETWEEN_CATEGORY))));
        }
    }
}