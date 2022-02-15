package com.example.plandial.policy;

import com.example.plandial.Category;
import com.example.plandial.DialManager;

import java.util.Calendar;

public interface ICategoryValidator {
    boolean validateName(String name);
    boolean sameName(DialManager dialManager, String name, Category category);
}