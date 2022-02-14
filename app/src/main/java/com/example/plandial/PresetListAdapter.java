package com.example.plandial;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PresetListAdapter extends RecyclerView.Adapter<PresetListAdapter.ItemViewHolder> {
    private Template template;
    private final boolean isSelectable;
    private ArrayList<Boolean> isSelected;

    public PresetListAdapter(Template template) {
        // 프리셋을 선택할 수 없는 리스트 생성
        this(template, false);
    }

    public PresetListAdapter(Template template, boolean isSelectable) {
        this.template = template;
        this.isSelectable = isSelectable;

        if (isSelectable) {
            isSelected = new ArrayList<>(8);

            for (int i = 0; i < template.getDialCount(); ++i) {
                isSelected.add(false);
            }
        }
    }

    public ArrayList<Preset> getSelectedPresets() {
        ArrayList<Preset> selectedPresets = new ArrayList<>(8);

        for (int i = 0; i < isSelected.size(); ++i) {
            if (isSelected.get(i)) {
                selectedPresets.add((Preset) template.getDialByIndex(i));
            }
        }

        return selectedPresets;
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

            if (isSelectable) {
                itemView.setOnClickListener(view -> {
                    int pos = getAdapterPosition();

                    if (isSelected.get(pos)) {
                        // 선택 해제 효과
                        itemView.setBackgroundColor(Color.parseColor("#00000000"));
                    } else {
                        // 선택 효과
                        itemView.setBackgroundColor(Color.parseColor("#306200EE"));
                    }


                    // 선택 반전
                    isSelected.set(pos, !isSelected.get(pos));
                });
            }
        }

        void onBind(Dial preset) {
            presetIcon.setImageResource(preset.getIcon());
            presetName.setText(preset.getName());
        }
    }
}
