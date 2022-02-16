package com.friday.plandial.policy;

import com.friday.plandial.Category;
import com.friday.plandial.DialManager;

public class BasicCategoryValidator implements ICategoryValidator {
    private static final int CATEGORY_LIMIT = 10;

    @Override
    public boolean validateName(String name) {
        return name != null && NAME_MIN_LENGTH <= name.length() && name.length() <= NAME_MAX_LENGTH;
    }

    @Override
    public boolean sameName(DialManager dialManager, String name, Category category) {
        return dialManager.getCategoryByName(name) == null;
    }

    public boolean numCategoryLimit(DialManager dialManager) {
        return dialManager.getCategoryCount() < CATEGORY_LIMIT;
    }
}
