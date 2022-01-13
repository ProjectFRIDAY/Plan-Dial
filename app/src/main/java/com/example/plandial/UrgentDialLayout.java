package com.example.plandial;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.widget.LinearLayout;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class UrgentDialLayout extends LinearLayout {
    private static final int URGENT_BOUND = 3600;

    public UrgentDialLayout(Context context) {
        super(context);
        //category, dial를 불러와 화면에 출력함.
        showUrgentDials();
    }

    public void showUrgentDials() {
        ArrayList<Pair<Long, Dial>> urgentDials = new ArrayList<>();

        DialManager dm = DialManager.getInstance();
        int categoryCount = dm.getCategoryCount();

        for (int i = 0; i < categoryCount; ++i) {
            Category category = dm.getCategoryByIndex(i);
            int dialCount = category.getDialCount();

            for (int j = 0; j < dialCount; ++j) {
                Dial dial = category.getDialByIndex(j);

                // 남은 시간 구하기
                OffsetDateTime endDateTime = dial.getEndDateTime();
                long leftTime = OffsetDateTime.now().until(endDateTime, ChronoUnit.SECONDS);
                Log.d("queue test", String.format("left time : %d", leftTime));

                if (leftTime < URGENT_BOUND) {
                    urgentDials.add(new Pair<Long, Dial>(leftTime, dial));
                }
            }
        }

        // test code
        Log.d("queue test", "quque test start");
        while(urgentDials.size() > 0) {
            // Log.d("queue test", String.format("%d", urgentDials.poll()));
        }
    }
}
