package com.example.plandial.policy;

import com.example.plandial.Category;
import com.example.plandial.DialManager;

public interface ICategoryValidator {
    int NAME_MIN_LENGTH = 1;
    int NAME_MAX_LENGTH = 15;

    boolean validateName(String name);

    boolean sameName(DialManager dialManager, String name, Category category);
}