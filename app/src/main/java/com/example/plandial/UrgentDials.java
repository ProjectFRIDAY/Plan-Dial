package com.example.plandial;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class UrgentDials {
    private static final ArrayList<Dial> UrgentDials = new ArrayList<>();
    private static final ArrayList<Category> categories = new ArrayList<>();

    private UrgentDials() {
        //category, dial를 DB에서 불러옴.
    }

    public void getUrgentDials(){
        for (Category category : categories) {
            for(int i = 0; i < category.getDialCount(); ++i) {
                OffsetDateTime startDateTime = category.getDialByIndex(i).getStartDateTime();
                OffsetDateTime endDateTime = category.getDialByIndex(i).getEndDateTime();
                long amount = startDateTime.until(endDateTime, ChronoUnit.SECONDS);
                if(amount < 3600) {
                    UrgentDials.add(category.getDialByIndex(i));
                }
            }
        }
    }
}
