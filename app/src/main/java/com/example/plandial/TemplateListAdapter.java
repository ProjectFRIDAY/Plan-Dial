package com.example.plandial;

import android.content.Context;
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
    private final TemplateManager templateManager;
    private final LayoutInflater layoutInflater;
    private int preClickedPos = -1;

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
        View view = layoutInflater.inflate(R.layout.template_row, null);

        ImageView templateIcon = view.findViewById(R.id.template_icon);
        TextView title = view.findViewById(R.id.title);
        TextView subtitle = view.findViewById(R.id.subtitle);
        RecyclerView presetList = view.findViewById(R.id.preset_list);
        Button nextButton = view.findViewById(R.id.complete_button);

        Template template = getItem(position);
        title.setText(template.getName());
        subtitle.setText(template.getDescription());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position == preClickedPos) {
                    // 클릭한 항목 열고 접기
                    if (presetList.getVisibility() == View.GONE) {
                        presetList.setVisibility(View.VISIBLE);
                        nextButton.setVisibility(View.VISIBLE);
                    } else {
                        presetList.setVisibility(View.GONE);
                        nextButton.setVisibility(View.GONE);
                    }
                } else {
                    // 이전에 클릭한 항목 접기
                    if (preClickedPos != -1) {
                        RecyclerView preClickedView = parent.getChildAt(preClickedPos).findViewById(R.id.preset_list);
                        Button preClickedViewButton = parent.getChildAt(preClickedPos).findViewById(R.id.complete_button);
                        preClickedView.setVisibility(View.GONE);
                        preClickedViewButton.setVisibility(View.GONE);
                    }

                    // 클릭한 항목 열기
                    if (presetList.getVisibility() == View.GONE) {
                        presetList.setVisibility(View.VISIBLE);
                    } else {
                        GridLayoutManager gridlayoutManager = new GridLayoutManager(view.getContext(), 2);
                        presetList.setLayoutManager(gridlayoutManager);

                        PresetListAdapter presetAdapter = new PresetListAdapter(templateManager.getTemplateByIndex(position));
                        presetList.setAdapter(presetAdapter);
                    }

                    nextButton.setVisibility(View.VISIBLE);
                }

                preClickedPos = position;
            }
        });

        return view;
    }
}
