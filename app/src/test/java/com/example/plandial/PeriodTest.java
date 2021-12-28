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


    @Test
    public void When_CompareSamePeriods_Expect_EqualsFunctionReturnTrue() {
        // 서로 같은 Period 인스턴스에 대해 Period.equals()가 제대로 동작하는지 확인하는 테스트

        Period period1;
        Period period2;

        period1 = Period.createPeriodOrNull(UnitOfTime.HOUR, 5);
        period2 = Period.createPeriodOrNull(UnitOfTime.HOUR, 5);

        assert period1 != null;
        assert period2 != null;
        assert period1.equals(period2);
        assert period2.equals(period1);

        period1 = Period.createPeriodOrNull(UnitOfTime.DAY, 3);
        period2 = Period.createPeriodOrNull(UnitOfTime.DAY, 3);

        assert period1 != null;
        assert period2 != null;
        assert period1.equals(period2);
        assert period2.equals(period1);

        period1 = Period.createPeriodOrNull(UnitOfTime.WEEK, 2);
        period2 = Period.createPeriodOrNull(UnitOfTime.WEEK, 2);

        assert period1 != null;
        assert period2 != null;
        assert period1.equals(period2);
        assert period2.equals(period1);

        period1 = Period.createPeriodOrNull(UnitOfTime.MONTH, 1);
        period2 = Period.createPeriodOrNull(UnitOfTime.MONTH, 1);

        assert period1 != null;
        assert period2 != null;
        assert period1.equals(period2);
        assert period2.equals(period1);

        period1 = Period.createPeriodOrNull(UnitOfTime.MONTH, Integer.MAX_VALUE);
        period2 = Period.createPeriodOrNull(UnitOfTime.MONTH, Integer.MAX_VALUE);

        assert period1 != null;
        assert period2 != null;
        assert period1.equals(period2);
        assert period2.equals(period1);
    }


    @Test
    public void When_CompareDifferentPeriods_Expect_EqualsFunctionReturnFalse() {
        // 서로 다른 Period 인스턴스에 대해 Period.equals()가 제대로 동작하는지 확인하는 테스트

        Period period1;
        Period period2;

        period1 = Period.createPeriodOrNull(UnitOfTime.HOUR, 3);
        period2 = Period.createPeriodOrNull(UnitOfTime.DAY, 3);

        assert period1 != null;
        assert period2 != null;
        assert !(period1.equals(period2));
        assert !(period2.equals(period1));

        period1 = Period.createPeriodOrNull(UnitOfTime.HOUR, 24);
        period2 = Period.createPeriodOrNull(UnitOfTime.DAY, 1);

        assert period1 != null;
        assert period2 != null;
        assert !(period1.equals(period2));
        assert !(period2.equals(period1));

        period1 = Period.createPeriodOrNull(UnitOfTime.HOUR, 48);
        period2 = Period.createPeriodOrNull(UnitOfTime.DAY, 2);

        assert period1 != null;
        assert period2 != null;
        assert !(period1.equals(period2));
        assert !(period2.equals(period1));

        period1 = Period.createPeriodOrNull(UnitOfTime.DAY, 1);
        period2 = Period.createPeriodOrNull(UnitOfTime.DAY, 2);

        assert period1 != null;
        assert period2 != null;
        assert !(period1.equals(period2));
        assert !(period2.equals(period1));

        period1 = Period.createPeriodOrNull(UnitOfTime.HOUR, 168);
        period2 = Period.createPeriodOrNull(UnitOfTime.WEEK, 1);

        assert period1 != null;
        assert period2 != null;
        assert !(period1.equals(period2));
        assert !(period2.equals(period1));

        period1 = Period.createPeriodOrNull(UnitOfTime.DAY, 7);
        period2 = Period.createPeriodOrNull(UnitOfTime.WEEK, 4);

        assert period1 != null;
        assert period2 != null;
        assert !(period1.equals(period2));
        assert !(period2.equals(period1));

        period1 = Period.createPeriodOrNull(UnitOfTime.WEEK, 512);
        period2 = Period.createPeriodOrNull(UnitOfTime.WEEK, 1024);

        assert period1 != null;
        assert period2 != null;
        assert !(period1.equals(period2));
        assert !(period2.equals(period1));

        period1 = Period.createPeriodOrNull(UnitOfTime.MONTH, 2);
        period2 = Period.createPeriodOrNull(UnitOfTime.WEEK, 1);

        assert period1 != null;
        assert period2 != null;
        assert !(period1.equals(period2));
        assert !(period2.equals(period1));

        period1 = Period.createPeriodOrNull(UnitOfTime.MONTH, 1);
        period2 = Period.createPeriodOrNull(UnitOfTime.WEEK, 4);

        assert period1 != null;
        assert period2 != null;
        assert !(period1.equals(period2));
        assert !(period2.equals(period1));

        period1 = Period.createPeriodOrNull(UnitOfTime.MONTH, 1);
        period2 = Period.createPeriodOrNull(UnitOfTime.DAY, 30);

        assert period1 != null;
        assert period2 != null;
        assert !(period1.equals(period2));
        assert !(period2.equals(period1));

        period1 = Period.createPeriodOrNull(UnitOfTime.MONTH, 1);
        period2 = Period.createPeriodOrNull(UnitOfTime.MONTH, 3);

        assert period1 != null;
        assert period2 != null;
        assert !(period1.equals(period2));
        assert !(period2.equals(period1));

        period1 = Period.createPeriodOrNull(UnitOfTime.MONTH, 1);
        period2 = Period.createPeriodOrNull(UnitOfTime.MONTH, Integer.MAX_VALUE);

        assert period1 != null;
        assert period2 != null;
        assert !(period1.equals(period2));
        assert !(period2.equals(period1));
    }
}
