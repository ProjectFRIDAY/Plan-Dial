package com.friday.plandial;

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

import com.example.plandial.R;

public class TemplateListAdapter extends BaseAdapter {
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

        presetList.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);

        view.setOnClickListener(view1 -> {
            // 수정 필요
            for (int i = 0; i < parent.getChildCount(); ++i) {
                View child = parent.getChildAt(i);
                if (child.equals(view1)) {
                    if (presetList.getVisibility() == View.VISIBLE) {
                        presetList.setVisibility(View.GONE);
                        nextButton.setVisibility(View.GONE);
                    } else {
                        presetList.setVisibility(View.VISIBLE);
                        nextButton.setVisibility(View.VISIBLE);
                    }

                } else {
                    child.findViewById(R.id.preset_list).setVisibility(View.GONE);
                    child.findViewById(R.id.complete_button).setVisibility(View.GONE);
                }
            }
        });

        return view;
    }
}
