package com.example.plandial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PresetListAdapter extends RecyclerView.Adapter<PresetListAdapter.ItemViewHolder> {
    private Template template;

    public PresetListAdapter(Template template) {
        this.template = template;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.preset_block, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(template.getDialByIndex(position));
    }

    @Override
    public int getItemCount() {
        if (template == null) {
            return 0;
        }
        return template.getDialCount();
    }

    //카테고리 받는 메서드 선언
    public void setCategory(Template template) {
        this.template = template;
        this.notifyDataSetChanged();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final ImageView presetIcon;
        private final TextView presetName;

        ItemViewHolder(View itemView) {
            super(itemView);

            presetIcon = itemView.findViewById(R.id.preset_icon);
            presetName = itemView.findViewById(R.id.preset_name);
        }

        void onBind(Dial preset) {
            presetIcon.setImageResource(R.drawable.ic_launcher_foreground);
            presetName.setText(preset.getName());
        }
    }
}
