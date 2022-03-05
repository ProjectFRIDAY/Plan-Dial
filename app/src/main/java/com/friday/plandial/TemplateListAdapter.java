package com.friday.plandial;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TemplateListAdapter extends BaseAdapter {
    private static final int ANIMATION_DURATION = 800;
    private final TemplateManager templateManager;
    private final LayoutInflater layoutInflater;

    public TemplateListAdapter(Context context) {
        templateManager = TemplateManager.getInstance();
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return templateManager.getTemplateCount();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Template getItem(int position) {
        return templateManager.getTemplateByIndex(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.template_row, parent, false);

        ImageView templateIcon = view.findViewById(R.id.template_icon);
        TextView title = view.findViewById(R.id.title);
        TextView subtitle = view.findViewById(R.id.subtitle);
        RecyclerView presetList = view.findViewById(R.id.preset_list);
        Button nextButton = view.findViewById(R.id.complete_button);
        ViewGroup container = view.findViewById(R.id.template_container);

        Template template = getItem(position);
        templateIcon.setImageResource(template.getIcon());
        title.setText(template.getName());
        subtitle.setText(template.getDescription());

        // 생성 화면으로 넘어가기
        nextButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), CategoryCreateActivity.class);
            intent.putExtra("templateIndex", position);
            v.getContext().startActivity(intent);
        });

        GridLayoutManager gridlayoutManager = new GridLayoutManager(view.getContext(), 2);
        presetList.setLayoutManager(gridlayoutManager);

        PresetListAdapter presetAdapter = new PresetListAdapter(templateManager.getTemplateByIndex(position));
        presetList.setAdapter(presetAdapter);

        animate(container, false, false);

        view.setOnClickListener(view1 -> {
            // 수정 필요
            for (int i = 0; i < parent.getChildCount(); ++i) {
                View child = parent.getChildAt(i);
                ViewGroup template_container = child.findViewById(R.id.template_container);
                boolean opened = (template_container.getHeight() > 0);

                if (child.equals(view1)) {
                    animate(container, !opened, true);
                } else {
                    animate(child.findViewById(R.id.template_container), false, opened);
                }
            }
        });

        return view;
    }

    private void animate(ViewGroup container, boolean open, boolean animation) {
        container.measure(0, 0);
        int height = container.getMeasuredHeight();

        ValueAnimator heightAnimation = ValueAnimator.ofInt(0, height);

        heightAnimation.addUpdateListener(valueAnimator -> {
            int val = (Integer) valueAnimator.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = container.getLayoutParams();
            layoutParams.height = open ? val : height - val;
            container.setLayoutParams(layoutParams);
        });
        heightAnimation.setDuration(animation ? ANIMATION_DURATION : 0);
        heightAnimation.start();
    }
}
