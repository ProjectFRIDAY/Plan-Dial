package com.example.plandial;

import org.junit.Test;

public class PeriodTest {
    @Test
    public void When_CreateValidPeriod_Expect_ReturnValidInstance() {
        Period period;

        period = Period.createPeriodOrNull(UnitOfTime.HOUR, 1);
        assert period != null;
        assert period.getPeriodInSeconds() == 3600;
        assert period.getPeriodInMillis() == 3600000;

        period = Period.createPeriodOrNull(UnitOfTime.HOUR, 12);
        assert period != null;
        assert period.getPeriodInSeconds() == 43200;
        assert period.getPeriodInMillis() == 43200000;

        period = Period.createPeriodOrNull(UnitOfTime.HOUR, 24);
        assert period != null;
        assert period.getPeriodInSeconds() == 86400;
        assert period.getPeriodInMillis() == 86400000;

        period = Period.createPeriodOrNull(UnitOfTime.HOUR, 100);
        assert period != null;
        assert period.getPeriodInSeconds() == 360000;
        assert period.getPeriodInMillis() == 360000000;
    }


    @Test
    public void When_MakePeriodWithNotPositiveTimes_Expect_ReturnNull() {
        Period period;

        period = Period.createPeriodOrNull(UnitOfTime.HOUR, 0);
        assert period == null;

        period = Period.createPeriodOrNull(UnitOfTime.HOUR, -1);
        assert period == null;

        period = Period.createPeriodOrNull(UnitOfTime.HOUR, Integer.MIN_VALUE);
        assert period == null;

        period = Period.createPeriodOrNull(UnitOfTime.DAY, 0);
        assert period == null;

        period = Period.createPeriodOrNull(UnitOfTime.DAY, -1);
        assert period == null;

        period = Period.createPeriodOrNull(UnitOfTime.DAY, Integer.MIN_VALUE);
        assert period == null;

        period = Period.createPeriodOrNull(UnitOfTime.WEEK, 0);
        assert period == null;

        period = Period.createPeriodOrNull(UnitOfTime.WEEK, -1);
        assert period == null;

        period = Period.createPeriodOrNull(UnitOfTime.WEEK, Integer.MIN_VALUE);
        assert period == null;

        period = Period.createPeriodOrNull(UnitOfTime.MONTH, 0);
        assert period == null;

        period = Period.createPeriodOrNull(UnitOfTime.MONTH, -1);
        assert period == null;

        period = Period.createPeriodOrNull(UnitOfTime.MONTH, Integer.MIN_VALUE);
        assert period == null;
    }
}
