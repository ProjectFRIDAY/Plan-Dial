package com.example.plandial;

import java.util.ArrayList;

public class DialManager {
    private static final DialManager dialManager = new DialManager();
    private static final ArrayList<Category> categories = new ArrayList<>();

    private DialManager() {
        // 로컬 DB에서 카테고리, 다이얼 불러오는 코드
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
        /* 인자로 들어온 문자열과 같은 이름의 카테고리들 중 인덱스가 가장 작은 카테고리를 반환한다.
         * 인자로 들어온 문자열과 같은 이름의 카테고리가 없다면 null을 반환한다.
         */

        for (Category category : categories) {
            if (category.getName().equals(name)) {
                return category;
            }
        }

        return null;
    }

    public boolean addCategory(Category category) {
        /* 이미 동일한 카테고리가 있으면 추가하지 않는다.
         * 이미 동일한 카테고리가 있는 경우 false, 없는 경우 true를 반환한다.
         */

        for (Category addedCategory : categories) {
            if (category.equals(addedCategory)) {
                return false;
            }
        }

        categories.add(category);
        saveContent();
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

    private static void saveContent() {
        // 현재 정보를 로컬 DB에 저장하도록 구현해야 함.
    }
}