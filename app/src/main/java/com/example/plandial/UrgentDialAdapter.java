package com.example.plandial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;


public class UrgentDialAdapter extends RecyclerView.Adapter<UrgentDialAdapter.ItemViewHolder> {

    private ArrayList<Dial> urgentDialList = new ArrayList<>();

    public UrgentDialAdapter() {
        DialManager dm = DialManager.getInstance();

        Dial dial1 = new Dial("빨래", new Period(UnitOfTime.DAY, 1), OffsetDateTime.of(2022, 1, 12, 18, 00, 00, 0, ZoneOffset.ofHours(9)));

        Dial dial2 = new Dial("청소", new Period(UnitOfTime.DAY, 1), OffsetDateTime.of(2022, 1, 12, 18, 01, 00, 0, ZoneOffset.ofHours(9)));

        Category category = new Category("큐 테스트");

        category.addDial(dial1);

        category.addDial(dial2);

        dm.addCategory(category);

        this.urgentDialList = dm.getUrgentDials(3600);

    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_urgentdial, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(urgentDialList.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return urgentDialList.size();
    }

    void addItem(Dial dial) {
        // 외부에서 dial을 추가시킬 함수입니다. 사용할 일이 없을 것입니다.
        urgentDialList.add(dial);
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView textView1;
        private TextView textView2;

        ItemViewHolder(View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
        }

        void onBind(Dial dial) {
            textView1.setText(dial.getName());
            textView2.setText(dial.getName());
        }
    }
}