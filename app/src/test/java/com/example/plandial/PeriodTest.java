package com.example.plandial;

import org.junit.Test;

public class PeriodTest {
    @Test
    public void When_CreatePeriod_Expect_ReturnValidInstance() {
        Period period;

        period = new Period(UnitOfTime.HOUR, 1);
        assert period.getPeriodInSeconds() == 3600;
        assert period.getPeriodInMillis() == 3600000;

        period = new Period(UnitOfTime.HOUR, 12);
        assert period.getPeriodInSeconds() == 43200;
        assert period.getPeriodInMillis() == 43200000;

        period = new Period(UnitOfTime.HOUR, 24);
        assert period.getPeriodInSeconds() == 86400;
        assert period.getPeriodInMillis() == 86400000;

        period = new Period(UnitOfTime.HOUR, 100);
        assert period.getPeriodInSeconds() == 360000;
        assert period.getPeriodInMillis() == 360000000;
    }


    @Test
    public void When_CompareSamePeriods_Expect_EqualsFunctionReturnTrue() {
        // 서로 같은 Period 인스턴스에 대해 Period.equals()가 제대로 동작하는지 확인하는 테스트

        Period period1;
        Period period2;

        period1 = new Period(UnitOfTime.HOUR, 5);
        period2 = new Period(UnitOfTime.HOUR, 5);

        assert period1.equals(period2);
        assert period2.equals(period1);

        period1 = new Period(UnitOfTime.DAY, 3);
        period2 = new Period(UnitOfTime.DAY, 3);

        assert period1.equals(period2);
        assert period2.equals(period1);

        period1 = new Period(UnitOfTime.WEEK, 2);
        period2 = new Period(UnitOfTime.WEEK, 2);

        assert period1.equals(period2);
        assert period2.equals(period1);

        period1 = new Period(UnitOfTime.MONTH, 1);
        period2 = new Period(UnitOfTime.MONTH, 1);

        assert period1.equals(period2);
        assert period2.equals(period1);

        period1 = new Period(UnitOfTime.MONTH, Integer.MAX_VALUE);
        period2 = new Period(UnitOfTime.MONTH, Integer.MAX_VALUE);

        assert period1.equals(period2);
        assert period2.equals(period1);
    }


    @Test
    public void When_CompareDifferentPeriods_Expect_EqualsFunctionReturnFalse() {
        // 서로 다른 Period 인스턴스에 대해 Period.equals()가 제대로 동작하는지 확인하는 테스트

        Period period1;
        Period period2;

        period1 = new Period(UnitOfTime.HOUR, 3);
        period2 = new Period(UnitOfTime.DAY, 3);

        assert !(period1.equals(period2));
        assert !(period2.equals(period1));

        period1 = new Period(UnitOfTime.HOUR, 24);
        period2 = new Period(UnitOfTime.DAY, 1);

        assert !(period1.equals(period2));
        assert !(period2.equals(period1));

        period1 = new Period(UnitOfTime.HOUR, 48);
        period2 = new Period(UnitOfTime.DAY, 2);

        assert !(period1.equals(period2));
        assert !(period2.equals(period1));

        period1 = new Period(UnitOfTime.DAY, 1);
        period2 = new Period(UnitOfTime.DAY, 2);

        assert !(period1.equals(period2));
        assert !(period2.equals(period1));

        period1 = new Period(UnitOfTime.HOUR, 168);
        period2 = new Period(UnitOfTime.WEEK, 1);

        assert !(period1.equals(period2));
        assert !(period2.equals(period1));

        period1 = new Period(UnitOfTime.DAY, 7);
        period2 = new Period(UnitOfTime.WEEK, 4);

        assert !(period1.equals(period2));
        assert !(period2.equals(period1));

        period1 = new Period(UnitOfTime.WEEK, 512);
        period2 = new Period(UnitOfTime.WEEK, 1024);

        assert !(period1.equals(period2));
        assert !(period2.equals(period1));

        period1 = new Period(UnitOfTime.MONTH, 2);
        period2 = new Period(UnitOfTime.WEEK, 1);

        assert !(period1.equals(period2));
        assert !(period2.equals(period1));

        period1 = new Period(UnitOfTime.MONTH, 1);
        period2 = new Period(UnitOfTime.WEEK, 4);

        assert !(period1.equals(period2));
        assert !(period2.equals(period1));

        period1 = new Period(UnitOfTime.MONTH, 1);
        period2 = new Period(UnitOfTime.DAY, 30);

        assert !(period1.equals(period2));
        assert !(period2.equals(period1));

        period1 = new Period(UnitOfTime.MONTH, 1);
        period2 = new Period(UnitOfTime.MONTH, 3);

        assert !(period1.equals(period2));
        assert !(period2.equals(period1));

        period1 = new Period(UnitOfTime.MONTH, 1);
        period2 = new Period(UnitOfTime.MONTH, Integer.MAX_VALUE);

        assert !(period1.equals(period2));
        assert !(period2.equals(period1));
    }
}
