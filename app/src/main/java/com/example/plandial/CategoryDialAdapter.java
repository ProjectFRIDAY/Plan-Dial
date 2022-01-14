package com.example.plandial;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

public class CategoryDialAdapter extends RecyclerView.Adapter<CategoryDialAdapter.ItemViewHolder> {
    private Category category;
    private DialManager mDialManager = DialManager.getInstance();

    public CategoryDialAdapter() {
        //region test code
        Dial dial1 = new Dial("빨래", new Period(UnitOfTime.DAY, 1), OffsetDateTime.of(2022, 1, 13, 14, 02, 00, 0, ZoneOffset.ofHours(9)));
        Dial dial2 = new Dial("청소", new Period(UnitOfTime.DAY, 1), OffsetDateTime.of(2022, 1, 13, 14, 01, 00, 0, ZoneOffset.ofHours(9)));
        Dial dial3 = new Dial("공부", new Period(UnitOfTime.DAY, 1), OffsetDateTime.of(2022, 1, 13, 14, 02, 00, 0, ZoneOffset.ofHours(9)));
        Dial dial4 = new Dial("코딩", new Period(UnitOfTime.DAY, 1), OffsetDateTime.of(2022, 1, 13, 14, 02, 00, 0, ZoneOffset.ofHours(9)));
        Category category1 = new Category("나는 바보다");
        category1.addDial(dial1);
        category1.addDial(dial2);
        category1.addDial(dial3);
        category1.addDial(dial4);
        mDialManager.addCategory(category1);

        //endregion
        this.category = mDialManager.getCategoryByName("나는 바보다");
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
        return category.getDialCount();
    }

    /*public void syncDials() {
        // urgentDial을 DialManager와 동기화하는 함수 (개선 필요)
        this.category = mDialManager.getUrgentDials(URGENT_BOUND);
    }*/

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageButton dialIcon;
        private TextView dialName;

        ItemViewHolder(View itemView) {
            super(itemView);

            dialIcon = itemView.findViewById(R.id.dial_icon);
            dialName = itemView.findViewById(R.id.dial_name);
        }

        void onBind(Dial dial) {
            dialIcon.setImageResource(R.drawable.ic_launcher_foreground);
            dialName.setText(dial.getName());
        }
    }
}

