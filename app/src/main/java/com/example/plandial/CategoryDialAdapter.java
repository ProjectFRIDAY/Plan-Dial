package com.example.plandial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

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
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_dial, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(category.getDialByIndex(position));
    }

    @Override
    public int getItemCount() {
        if (category == null) {
            return 0;
        }
        return category.getDialCount();
    }

    //카테고리 받는 메서드 선언
    public void setCategory(Category category) {
        if (category != null) {
            this.category = category;
            this.notifyDataSetChanged();
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageButton dialIcon;
        private TextView dialName;

        ItemViewHolder(View itemView) {
            super(itemView);

            dialIcon = itemView.findViewById(R.id.dial_icon);
            dialName = itemView.findViewById(R.id.dial_name);

            dialIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (pos < category.getDialCount()) {
                            statusDisplayLayout.displayDial(category.getDialByIndex(pos));
                        } else if (pos == category.getDialCount()) {
                            // 다이얼 추가 화면으로 이동?
                        }
                    }
                }
            });
        }

        void onBind(Dial dial) {
            dialIcon.setImageResource(R.drawable.ic_launcher_foreground);
            dialName.setText(dial.getName());
        }
    }
}

