package com.friday.plandial.policy;

import com.friday.plandial.Category;
import com.friday.plandial.Dial;
import com.friday.plandial.Period;

import java.time.OffsetDateTime;

public class EditDialValidator implements IDialValidator {
    @Override
    public boolean validateName(String name) {
        return name != null && NAME_MIN_LENGTH <= name.length() && name.length() <= NAME_MAX_LENGTH;
    }

    public boolean sameName(String name, Category category, Dial dial) {
        Dial sameNamedDial = category.getDialByName(name);
        return sameNamedDial == null | sameNamedDial == dial;
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
