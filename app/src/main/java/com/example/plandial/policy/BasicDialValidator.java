package com.example.plandial.policy;

import com.example.plandial.Category;
import com.example.plandial.Period;

import java.time.OffsetDateTime;

public class BasicDialValidator implements IDialValidator {
    @Override
    public boolean validateName(String name) {
        return name != null && NAME_MIN_LENGTH <= name.length() && name.length() <= NAME_MAX_LENGTH;
    }

    public boolean validateName(String name, Category category) {
        return category.getDialByName(name) == null;
    }

    @Override
    public boolean validatePeriod(Period period) {
        return period != null && 0 < period.getPeriodInSeconds();
    }

    @Override
    public boolean validateStartDay(OffsetDateTime startDay) {
        return startDay != null;
    }
}
