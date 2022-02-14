package com.example.plandial.policy;

import com.example.plandial.DialManager;

public class BasicCategoryValidator implements ICategoryValidator {
    @Override
    public boolean validateName(String name) {
        return name != null && NAME_MIN_LENGTH <= name.length() && name.length() <= NAME_MAX_LENGTH;
    }

    public boolean validateName(String name, DialManager dialManager) {
        return dialManager.getCategoryByName(name) == null;
    }
}
