package com.example.plandial;

import org.junit.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class DialTest {
    @Test
    public void When_CreateValidDial_Then_ReturnValidInstance() {
        OffsetDateTime startDateTime;
        Dial dial;

        startDateTime = OffsetDateTime.of(2021, 12, 23, 12, 36, 43, 0, ZoneOffset.ofHours(9));
        dial = new Dial("Test Dial 1", Period.createPeriodOrNull(UnitOfTime.DAY, 3), startDateTime);
        assert dial.getName().equals("Test Dial 1");
        assert dial.getStartDateTime().equals(OffsetDateTime.of(2021, 12, 23, 12, 36, 43, 0, ZoneOffset.ofHours(9)));
        assert dial.getEndDateTime().equals(OffsetDateTime.of(2021, 12, 26, 12, 36, 43, 0, ZoneOffset.ofHours(9)));

        startDateTime = OffsetDateTime.of(2021, 12, 23, 12, 36, 43, 0, ZoneOffset.ofHours(9));
        dial = new Dial("테스트 다이얼 2", Period.createPeriodOrNull(UnitOfTime.MONTH, 1), startDateTime);
        assert dial.getName().equals("테스트 다이얼 2");
        assert dial.getStartDateTime().equals(OffsetDateTime.of(2021, 12, 23, 12, 36, 43, 0, ZoneOffset.ofHours(9)));
        assert dial.getEndDateTime().equals(OffsetDateTime.of(2022, 1, 22, 12, 36, 43, 0, ZoneOffset.ofHours(9)));
    }
}
