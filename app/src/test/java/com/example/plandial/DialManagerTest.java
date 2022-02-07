package com.example.plandial;

import org.junit.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

public class DialManagerTest {
    @Test
    public void When_UpdateDialManager_Expect_ReflectedWell() {
        DialManager dialManager = DialManager.getInstance();

        Category category1;
        Category category2;
        Category category3;

        AlertDial alertDial1 = new AlertDial("빨래", new Period(UnitOfTime.DAY, 3), OffsetDateTime.of(2021, 12, 28, 12, 36, 43, 0, ZoneOffset.ofHours(9)));
        AlertDial alertDial2 = new AlertDial("빨래", new Period(UnitOfTime.DAY, 3), OffsetDateTime.of(2021, 12, 28, 12, 36, 43, 0, ZoneOffset.ofHours(9)));
        AlertDial alertDial3 = new AlertDial("청소", new Period(UnitOfTime.DAY, 2), OffsetDateTime.of(2021, 12, 28, 12, 36, 43, 0, ZoneOffset.ofHours(9)));
        AlertDial alertDial4 = new AlertDial("청소", new Period(UnitOfTime.DAY, 2), OffsetDateTime.of(2021, 12, 28, 12, 36, 43, 0, ZoneOffset.ofHours(9)));

        ArrayList<Dial> dials1 = new ArrayList<Dial>() {
            {
                add(alertDial1);
                add(alertDial3);
            }
        };

        ArrayList<Dial> dials2 = new ArrayList<Dial>() {
            {
                add(alertDial2);
                add(alertDial4);
            }
        };

        ArrayList<Dial> dials3 = new ArrayList<Dial>() {
            {
                add(alertDial2);
            }
        };

        category1 = new Category("집안일", dials1);
        category2 = new Category("집안일", dials2);
        category3 = new Category("집안일", dials3);

        assert dialManager.getCategoryCount() == 0;
        assert dialManager.addCategory(category1);
        assert dialManager.getCategoryCount() == 1;
        assert dialManager.getCategoryByIndex(0).equals(category2);

        assert !dialManager.addCategory(category2);
        assert dialManager.getCategoryCount() == 1;
        assert dialManager.getCategoryByIndex(0).equals(category1);

        assert dialManager.addCategory(category3);
        assert dialManager.getCategoryCount() == 2;
        assert dialManager.getCategoryByIndex(1).equals(category3);
        assert dialManager.getCategoryByName(category3.getName()).equals(category1);

        assert dialManager.removeCategoryByIndex(0) == category1;
        assert dialManager.getCategoryCount() == 1;
        assert dialManager.getCategoryByIndex(0) == category3;

        assert dialManager.removeCategoryByIndex(1) == null;
        assert dialManager.getCategoryCount() == 1;
        assert dialManager.getCategoryByIndex(0) == category3;

        assert !(dialManager.removeCategoryByObject(category2));
        assert dialManager.getCategoryCount() == 1;
        assert dialManager.getCategoryByName(category3.getName()) == category3;

        assert dialManager.removeCategoryByObject(category3);
        assert dialManager.getCategoryCount() == 0;
        assert dialManager.getCategoryByIndex(0) == null;
    }
}
