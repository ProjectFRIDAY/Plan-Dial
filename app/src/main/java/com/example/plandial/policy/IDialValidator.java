package com.example.plandial.policy;

import com.example.plandial.Period;
import com.example.plandial.UnitOfTime;

import java.time.OffsetDateTime;

public interface IDialValidator {
    boolean validateName(String name);

    boolean validatePeriod(Period period);

    boolean validateStartDay(OffsetDateTime startDay);
}
