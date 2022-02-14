package com.example.plandial.policy;

public interface ICategoryValidator {
    int NAME_MIN_LENGTH = 1;
    int NAME_MAX_LENGTH = 9;

    boolean validateName(String name);
}
