package com.example.plandial.policy;

import com.example.plandial.Period;

import java.time.OffsetDateTime;

public interface IDialValidator {
    int NAME_MIN_LENGTH = 1;
    int NAME_MAX_LENGTH = 9;

    boolean validateName(String name);

    boolean validatePeriod(Period period);

    boolean validateStartDay(OffsetDateTime startDay);
}
