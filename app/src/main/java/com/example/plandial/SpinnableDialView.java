package com.example.plandial;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;

public class SpinnableDialView extends SpinnableImageView {

    private static final float DEGREE_BETWEEN_CATEGORY = 30;
    private static final int MAGNET_STRENGTH = 50;
    private static final int VIBRATE_STRENGTH = 4;
    private static final float CIRCLE_SIZE = 32.0f;     // dp
    private static final float SLIDER_SIZE = 205.0f;    // dp

    private ArrayList<ImageView> circles;
    private final DialManager dialManager = DialManager.getInstance();
    private int selectedCategoryIndex = 0;
    private CategoryDialAdapter categoryDialAdapter;
    private TextView categoryNameView;

    private float distanceToCircle;
    private float baseX;
    private float baseY;

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
            categoryDialAdapter.setCategory(dialManager.getCategoryByIndex(this.selectedCategoryIndex));

            float lastDegree = categoryIndex * DEGREE_BETWEEN_CATEGORY;
            super.rotate(currentDegree, lastDegree, MAGNET_STRENGTH);
            currentDegree = lastDegree;

            // 약한 진동을 발생함
            vibrator.vibrate(VibrationEffect.createOneShot(VIBRATE_STRENGTH, VibrationEffect.DEFAULT_AMPLITUDE));

            syncCategoryName();
        }

        arrangeCircles();

        return true;
    }

    public int getSelectedCategoryIndex() {
        // 선택된 카테고리의 인덱스를 반환함
        return this.selectedCategoryIndex;
    }

    public void setCategoryDialAdapter(CategoryDialAdapter categoryDialAdapter) {
        this.categoryDialAdapter = categoryDialAdapter;
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
    }

    public void setCategoryNameView(TextView categoryNameView) {
        this.categoryNameView = categoryNameView;
        syncCategoryName();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        this.distanceToCircle = 0.76f * (convertDpToPx(SLIDER_SIZE)) / 2;

        this.baseX = -convertDpToPx(CIRCLE_SIZE) / 2 + this.getX() + convertDpToPx(SLIDER_SIZE) / 2;
        this.baseY = -convertDpToPx(CIRCLE_SIZE) / 2 + this.getY() + convertDpToPx(SLIDER_SIZE) / 2;
    }

    public void arrangeCircles() {
        for (int i = 0; i < circles.size(); ++i) {
            circles.get(i).setX((float) (baseX + distanceToCircle * Math.sin(Math.toRadians(currentDegree - i * DEGREE_BETWEEN_CATEGORY))));
            circles.get(i).setY((float) (baseY - distanceToCircle * Math.cos(Math.toRadians(currentDegree - i * DEGREE_BETWEEN_CATEGORY))));
        }
    }

    private void syncCategoryName() {
        int selectedCategoryIndex = this.getSelectedCategoryIndex();
        Resources resources = getResources();
        Drawable categoryBackground = ResourcesCompat.getDrawable(resources, R.drawable.category_background_ripple, null);
        int color = ContextCompat.getColor(getContext(), R.color.empty_bg_color);

        assert categoryBackground != null;
        if (selectedCategoryIndex < dialManager.getCategoryCount()) {
            color = ContextCompat.getColor(getContext(), resources.getIdentifier("category_bg_color_" + selectedCategoryIndex, "color", this.getContext().getPackageName()));
            this.categoryNameView.setText(dialManager.getCategoryByIndex(selectedCategoryIndex).getName());
        } else {
            this.categoryNameView.setText("빈 카테고리");
        }

        categoryBackground.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        this.categoryNameView.setBackground(categoryBackground);
    }

    private float convertDpToPx(float dp) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                this.getContext().getResources().getDisplayMetrics());
    }
}