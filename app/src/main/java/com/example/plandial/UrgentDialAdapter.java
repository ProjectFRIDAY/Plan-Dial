package com.example.plandial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;


public class UrgentDialAdapter extends RecyclerView.Adapter<UrgentDialAdapter.ItemViewHolder> {
    private static final int URGENT_BOUND = 3600;
    private ArrayList<Dial> urgentDialList;
    private DialManager mDialManager = DialManager.getInstance();

    public UrgentDialAdapter() {
        //region test code
        Dial dial1 = new Dial("빨래", new Period(UnitOfTime.DAY, 1), OffsetDateTime.of(2022, 1, 13, 14, 02, 00, 0, ZoneOffset.ofHours(9)));
        Dial dial2 = new Dial("청소", new Period(UnitOfTime.DAY, 1), OffsetDateTime.of(2022, 1, 13, 14, 01, 00, 0, ZoneOffset.ofHours(9)));
        Category category = new Category("큐 테스트");
        category.addDial(dial1);
        category.addDial(dial2);
        mDialManager.addCategory(category);
        //endregion

        this.urgentDialList = mDialManager.getUrgentDials(URGENT_BOUND);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.urgent_dial, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(urgentDialList.get(position));
    }

    @Override
    public int getItemCount() {
        return urgentDialList.size();
    }

    public void syncDials() {
        // urgentDial을 DialManager와 동기화하는 함수 (개선 필요)
        this.urgentDialList = mDialManager.getUrgentDials(URGENT_BOUND);
    }

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