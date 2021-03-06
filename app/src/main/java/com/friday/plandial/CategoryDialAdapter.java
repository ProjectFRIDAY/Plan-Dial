package com.friday.plandial;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryDialAdapter extends RecyclerView.Adapter<CategoryDialAdapter.ItemViewHolder> {
    private Category category;
    private final DialManager dialManager = DialManager.getInstance();
    private StatusDisplayLayout statusDisplayLayout;

    public CategoryDialAdapter() {
        this.category = dialManager.getCategoryByIndex(0);
    }

    public void setStatusDisplayLayout(StatusDisplayLayout statusDisplayLayout) {
        this.statusDisplayLayout = statusDisplayLayout;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_dial, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나씩 보여주는(bind) 함수

        if (position == category.getDialCount()) {
            // 다이얼 추가 버튼 생성
            holder.setAsAddButton();
            return;
        }

        holder.onBind((AlertDial) category.getDialByIndex(position));
    }

    @Override
    public int getItemCount() {
        if (category == null) {
            return 0;
        }

        return category.getDialCount() + 1;
    }

    //카테고리 받는 메서드 선언
    @SuppressLint("NotifyDataSetChanged")
    public void setCategory(Category category) {
        this.category = category;
        this.notifyDataSetChanged();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final ImageButton dialIcon;
        private final TextView dialName;

        ItemViewHolder(View itemView) {
            super(itemView);

            dialIcon = itemView.findViewById(R.id.dial_icon);
            dialName = itemView.findViewById(R.id.dial_name);

            dialIcon.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    if (pos < category.getDialCount()) {
                        statusDisplayLayout.displayDial((AlertDial) category.getDialByIndex(pos));
                    } else if (pos == category.getDialCount()) {
                        Intent intent = new Intent(v.getContext().getApplicationContext(), PlusDialActivity.class);
                        intent.putExtra("categoryName", category.getName());
                        v.getContext().startActivity(intent);
                    }
                }
            });
            dialIcon.setOnLongClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    if (pos < category.getDialCount()) {
                        Dial dial = category.getDialByIndex(pos);
                        String dialName = dial.getName();
                        Intent intent = new Intent(v.getContext().getApplicationContext(), EditDialActivity.class);
                        intent.putExtra("dialName", String.valueOf(dialName));
                        intent.putExtra("categoryName", category.getName());
                        v.getContext().startActivity(intent);
                    }
                }
                return true;
            });
        }

        void onBind(AlertDial dial) {
            if (dial.isDisabled()) {
                dialIcon.setBackgroundResource(R.drawable.dial_background_disabled);
            } else {
                dialIcon.setBackgroundResource(R.drawable.dial_background_ripple);
            }
            dialIcon.setImageResource(dial.getIcon());
            dialName.setText(dial.getName());
        }

        void setAsAddButton() {
            dialIcon.setBackgroundResource(R.drawable.dial_background_ripple);
            dialIcon.setImageResource(R.drawable.ic_plus);
            dialName.setText("다이얼 추가");
        }
    }
}

