package com.friday.plandial;

import android.util.Pair;

import com.friday.plandial.db.WorkDatabase;

import java.util.ArrayList;
import java.util.Comparator;

public class DialManager {
    public static final int URGENT_BOUND = 60 * UnitOfTime.SECONDS_PER_MINUTE * UnitOfTime.MILLIS_PER_SECOND;

    private static final DialManager dialManager = new DialManager();
    private static final ArrayList<Category> categories = new ArrayList<>();

    private DialManager() {
    }

    public static DialManager getInstance() {
        return dialManager;
    }

    public int getCategoryCount() {
        return categories.size();
    }

    public Category getCategoryByIndex(int index) {
        try {
            return categories.get(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public Category getCategoryByName(String name) {
        /* 인자로 들어온 문자열과 같은 이름의 카테고리들 중 인덱스가 가장 작은 카테고리를 반환함.
         * 인자로 들어온 문자열과 같은 이름의 카테고리가 없다면 null을 반환함.
         */

        for (Category category : categories) {
            if (category.getName().equals(name)) {
                return category;
            }
        }

        return null;
    }

    public int getIndexByCategory(Category targetCategory) {
        /* 인자로 들어온 카테고리의 index를 반환함.
         * 인자로 들어온 카테고리와 같은 카테고리가 없다면 0을 반환함.
         */

        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);
            if (category.equals(targetCategory)) return i;
        }

        return -1;
    }

    public boolean addCategory(Category category) {
        /* 이미 동일한 카테고리가 있으면 추가하지 않음.
         * 이미 동일한 카테고리가 있는 경우 false, 없는 경우 true를 반환함.
         */

        for (Category addedCategory : categories) {
            if (category.equals(addedCategory)) {
                return false;
            }
        }

        categories.add(category);
        return true;
    }

    public Category removeCategoryByIndex(int index) {
        try {
            return categories.remove(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public boolean removeCategoryByObject(Category category) {
        return categories.remove(category);
    }

    public ArrayList<AlertDial> getUrgentDials(long urgentBound) {
        ArrayList<Pair<Long, AlertDial>> urgentDials = new ArrayList<>();

        for (Category category : categories) {

            for (Dial dial : category.getAllDials()) {
                AlertDial alertDial = (AlertDial) dial;

                // 남은 시간 구하기
                long leftTime = alertDial.getLeftTimeInMillis();

                if (0 <= leftTime && leftTime < urgentBound && !alertDial.isDisabled()) {
                    urgentDials.add(new Pair<>(leftTime, alertDial));
                }
            }
        }

        urgentDials.sort(Comparator.comparing(lhs -> lhs.first));

        ArrayList<AlertDial> result = new ArrayList<>();

        for (int i = 0; i < urgentDials.size(); ++i) {
            result.add(urgentDials.get(i).second);
        }

        return result;
    }

    public void resetAll() {
        categories.clear();
    }

    public int getNextDialId() {
        return WorkDatabase.getInstance().getNextDialId();
    }
}
