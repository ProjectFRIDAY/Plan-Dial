package com.example.plandial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TemplateListAdapter extends BaseAdapter {
    TemplateManager templateManager;
    LayoutInflater layoutInflater;

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
    public Category getItem(int position) {
        return templateManager.getTemplateByIndex(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.template_row, null);

        ImageView templateIcon = view.findViewById(R.id.template_icon);
        TextView title = view.findViewById(R.id.title);
        TextView subtitle = view.findViewById(R.id.subtitle);

        Category template = getItem(position);
        title.setText(template.getName());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GridLayout
            }
        });

        return view;
    }
}
