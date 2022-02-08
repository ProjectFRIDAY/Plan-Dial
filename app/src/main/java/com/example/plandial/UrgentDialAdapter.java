package com.example.plandial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class UrgentDialAdapter extends RecyclerView.Adapter<UrgentDialAdapter.ItemViewHolder> {
    private static final int URGENT_BOUND = 60 * UnitOfTime.SECONDS_PER_MINUTE * UnitOfTime.MILLIS_PER_SECOND;
    private ArrayList<AlertDial> urgentAlertDialList;
    private final DialManager dialManager = DialManager.getInstance();

    public UrgentDialAdapter() {

        this.urgentAlertDialList = dialManager.getUrgentDials(URGENT_BOUND);
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
        holder.onBind(urgentAlertDialList.get(position));
    }

    @Override
    public int getItemCount() {
        return urgentAlertDialList.size();
    }

    public void syncDials() {
        // urgentDial을 DialManager와 동기화하는 함수 (개선 필요)
        this.urgentAlertDialList = dialManager.getUrgentDials(URGENT_BOUND);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final ImageButton dialIcon;
        private final TextView dialName;

        ItemViewHolder(View itemView) {
            super(itemView);

            dialIcon = itemView.findViewById(R.id.dial_icon);
            dialName = itemView.findViewById(R.id.dial_name);
        }

        void onBind(AlertDial alertDial) {
            dialIcon.setImageResource(alertDial.getIcon());
            dialName.setText(alertDial.getName());
        }
    }
}