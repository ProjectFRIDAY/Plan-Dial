package com.friday.plandial.policy;

import com.friday.plandial.Category;
import com.friday.plandial.DialManager;

public interface ICategoryValidator {
    int NAME_MIN_LENGTH = 1;
    int NAME_MAX_LENGTH = 15;

    boolean validateName(String name);

    boolean sameName(DialManager dialManager, String name, Category category);
}