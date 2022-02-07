package com.example.plandial;

import org.junit.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class AlertDialTest {
    @Test
    public void When_CreateDial_Expect_ReturnValidInstance() {
        OffsetDateTime startDateTime;
        AlertDial alertDial;

        startDateTime = OffsetDateTime.of(2021, 12, 23, 12, 36, 43, 0, ZoneOffset.ofHours(9));
        alertDial = new AlertDial("Test Dial 1", new Period(UnitOfTime.DAY, 3), startDateTime);
        assert alertDial.getName().equals("Test Dial 1");
        assert alertDial.getStartDateTime().equals(OffsetDateTime.of(2021, 12, 23, 12, 36, 43, 0, ZoneOffset.ofHours(9)));
        assert alertDial.getEndDateTime().equals(OffsetDateTime.of(2021, 12, 26, 12, 36, 43, 0, ZoneOffset.ofHours(9)));

        startDateTime = OffsetDateTime.of(2021, 12, 23, 12, 36, 43, 0, ZoneOffset.ofHours(9));
        alertDial = new AlertDial("테스트 다이얼 2", new Period(UnitOfTime.MONTH, 1), startDateTime);
        assert alertDial.getName().equals("테스트 다이얼 2");
        assert alertDial.getStartDateTime().equals(OffsetDateTime.of(2021, 12, 23, 12, 36, 43, 0, ZoneOffset.ofHours(9)));
        assert alertDial.getEndDateTime().equals(OffsetDateTime.of(2022, 1, 22, 12, 36, 43, 0, ZoneOffset.ofHours(9)));
    }


    @Test
    public void When_CompareSameDials_Expect_EqualsFunctionReturnTrue() {
        // 서로 같은 다이얼에 대해 Dial.equals() 함수가 적절하게 작동하는지 확인하는 테스트입니다.

        OffsetDateTime startDateTime1 = OffsetDateTime.of(2002, 5, 31, 6, 32, 2, 10, ZoneOffset.ofHours(9));
        OffsetDateTime startDateTime2 = OffsetDateTime.of(2021, 12, 25, 1, 46, 5, 20, ZoneOffset.ofHours(9));
        OffsetDateTime startDateTime3 = OffsetDateTime.of(2021, 12, 25, 1, 46, 5, 20, ZoneOffset.ofHours(9));

        AlertDial alertDial1;
        AlertDial alertDial2;

        alertDial1 = new AlertDial("코딩공부", new Period(UnitOfTime.HOUR, 1), startDateTime1);
        alertDial2 = new AlertDial("코딩공부", new Period(UnitOfTime.HOUR, 1), startDateTime1);

        assert alertDial1.equals(alertDial2);
        assert alertDial2.equals(alertDial1);

        alertDial1 = new AlertDial("선물 배달", new Period(UnitOfTime.DAY, 365), startDateTime2);
        alertDial2 = new AlertDial("선물 배달", new Period(UnitOfTime.DAY, 365), startDateTime2);

        assert alertDial1.equals(alertDial2);
        assert alertDial2.equals(alertDial1);

        alertDial1 = new AlertDial("쇼핑", new Period(UnitOfTime.WEEK, 2), startDateTime2);
        alertDial2 = new AlertDial("쇼핑", new Period(UnitOfTime.WEEK, 2), startDateTime3);

        assert alertDial1.equals(alertDial2);
        assert alertDial2.equals(alertDial1);

        alertDial1 = new AlertDial("건강검진", new Period(UnitOfTime.MONTH, 6), startDateTime3);
        alertDial2 = new AlertDial("건강검진", new Period(UnitOfTime.MONTH, 6), startDateTime3);

        assert alertDial1.equals(alertDial2);
        assert alertDial2.equals(alertDial1);
    }


    @Test
    public void When_CompareDifferentDials_Expect_EqualsFunctionReturnFalse() {
        // 서로 다른 다이얼에 대해 Dial.equals() 함수가 적절하게 작동하는지 확인하는 테스트입니다.
        OffsetDateTime startDateTime1 = OffsetDateTime.of(2002, 5, 31, 6, 32, 2, 10, ZoneOffset.ofHours(9));
        OffsetDateTime startDateTime2 = OffsetDateTime.of(2021, 12, 25, 1, 46, 5, 20, ZoneOffset.ofHours(9));
        OffsetDateTime startDateTime3 = OffsetDateTime.of(2021, 12, 25, 1, 46, 5, 20, ZoneOffset.ofHours(9));

        AlertDial alertDial1;
        AlertDial alertDial2;

        alertDial1 = new AlertDial("코딩공부", new Period(UnitOfTime.HOUR, 1), startDateTime1);
        alertDial2 = new AlertDial("미적분공부", new Period(UnitOfTime.MONTH, 120), startDateTime1);

        assert !(alertDial1.equals(alertDial2));
        assert !(alertDial2.equals(alertDial1));

        alertDial1 = new AlertDial("선물 배달", new Period(UnitOfTime.DAY, 1), startDateTime2);
        alertDial2 = new AlertDial("선물 배달", new Period(UnitOfTime.DAY, 365), startDateTime2);

        assert !(alertDial1.equals(alertDial2));
        assert !(alertDial2.equals(alertDial1));

        alertDial1 = new AlertDial("쇼핑", new Period(UnitOfTime.DAY, 2), startDateTime2);
        alertDial2 = new AlertDial("쇼핑", new Period(UnitOfTime.WEEK, 2), startDateTime3);

        assert !(alertDial1.equals(alertDial2));
        assert !(alertDial2.equals(alertDial1));

        alertDial1 = new AlertDial("여행", new Period(UnitOfTime.MONTH, 6), startDateTime3);
        alertDial2 = new AlertDial("건강검진", new Period(UnitOfTime.MONTH, 6), startDateTime3);

        assert !(alertDial1.equals(alertDial2));
        assert !(alertDial2.equals(alertDial1));
    }
}
