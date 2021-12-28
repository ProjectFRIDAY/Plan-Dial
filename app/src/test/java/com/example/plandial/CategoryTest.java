package com.example.plandial;

import org.junit.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

public class CategoryTest {
    @Test
    public void When_CreateCategory_Expect_ReturnValidInstance() {
        Category category;

        Dial dial1 = new Dial("빨래", new Period(UnitOfTime.DAY, 3), OffsetDateTime.of(2021, 12, 28, 12, 36, 43, 0, ZoneOffset.ofHours(9)));
        Dial dial2 = new Dial("청소", new Period(UnitOfTime.DAY, 2), OffsetDateTime.of(2021, 12, 28, 12, 36, 43, 0, ZoneOffset.ofHours(9)));
        Dial dial3 = new Dial("청소", new Period(UnitOfTime.DAY, 2), OffsetDateTime.of(2021, 12, 28, 12, 36, 43, 0, ZoneOffset.ofHours(9)));

        ArrayList<Dial> dials1 = new ArrayList<Dial>() {
            {
                add(dial1);
                add(dial2);
            }
        };

        ArrayList<Dial> dials2 = new ArrayList<Dial>() {
            {
                add(dial2);
                add(dial3);
            }
        };

        category = new Category("코딩");
        assert category.getName().equals("코딩");
        assert category.getDialCount() == 0;

        category = new Category("집안일", new ArrayList<>(dials1));
        assert category.getName().equals("집안일");
        assert category.getDialCount() == 2;
        assert category.getDialByIndex(0).equals(dial1);
        assert category.getDialByIndex(1).equals(dial2);

        category = new Category("집밖일", new ArrayList<>(dials2));
        assert category.getName().equals("집밖일");
        assert category.getDialCount() == 1;
        assert category.getDialByIndex(0).equals(dial3);
    }


    @Test
    public void When_CompareSameCategories_Expect_EqualsFunctionReturnTrue() {
        Category category1;
        Category category2;
        Category category3;

        Dial dial1 = new Dial("빨래", new Period(UnitOfTime.DAY, 3), OffsetDateTime.of(2021, 12, 28, 12, 36, 43, 0, ZoneOffset.ofHours(9)));
        Dial dial2 = new Dial("빨래", new Period(UnitOfTime.DAY, 3), OffsetDateTime.of(2021, 12, 28, 12, 36, 43, 0, ZoneOffset.ofHours(9)));
        Dial dial3 = new Dial("청소", new Period(UnitOfTime.DAY, 2), OffsetDateTime.of(2021, 12, 28, 12, 36, 43, 0, ZoneOffset.ofHours(9)));
        Dial dial4 = new Dial("청소", new Period(UnitOfTime.DAY, 2), OffsetDateTime.of(2021, 12, 28, 12, 36, 43, 0, ZoneOffset.ofHours(9)));

        ArrayList<Dial> dials1 = new ArrayList<Dial>() {
            {
                add(dial1);
                add(dial3);
            }
        };

        ArrayList<Dial> dials2 = new ArrayList<Dial>() {
            {
                add(dial2);
                add(dial4);
            }
        };

        ArrayList<Dial> dials3 = new ArrayList<Dial>() {
            {
                add(dial1);
                add(dial2);
                add(dial3);
                add(dial4);
            }
        };

        category1 = new Category("집안일", dials1);
        category2 = new Category("집안일", dials2);
        category3 = new Category("집안일", dials3);

        assert category1.equals(category2);
        assert category1.equals(category3);
        assert category2.equals(category1);
        assert category2.equals(category3);
        assert category3.equals(category1);
        assert category3.equals(category2);
    }


    @Test
    public void When_CompareDifferentCategories_Expect_EqualsFunctionReturnFalse() {
        Category category1;
        Category category2;
        Category category3;

        Dial dial1 = new Dial("빨래", new Period(UnitOfTime.DAY, 3), OffsetDateTime.of(2021, 12, 28, 12, 36, 43, 0, ZoneOffset.ofHours(9)));
        Dial dial2 = new Dial("빨래", new Period(UnitOfTime.DAY, 3), OffsetDateTime.of(2021, 12, 28, 12, 36, 43, 0, ZoneOffset.ofHours(9)));
        Dial dial3 = new Dial("청소", new Period(UnitOfTime.DAY, 2), OffsetDateTime.of(2021, 12, 28, 12, 36, 43, 0, ZoneOffset.ofHours(9)));
        Dial dial4 = new Dial("청소", new Period(UnitOfTime.DAY, 2), OffsetDateTime.of(2021, 12, 28, 12, 36, 43, 0, ZoneOffset.ofHours(9)));

        ArrayList<Dial> dials1 = new ArrayList<Dial>() {
            {
                add(dial1);
                add(dial3);
            }
        };

        ArrayList<Dial> dials2 = new ArrayList<Dial>() {
            {
                add(dial2);
                add(dial4);
            }
        };

        ArrayList<Dial> dials3 = new ArrayList<Dial>() {
            {
                add(dial2);
            }
        };

        category1 = new Category("집안일", dials1);
        category2 = new Category("집안이", dials2);
        category3 = new Category("집안일", dials3);

        assert !(category1.equals(category2));
        assert !(category2.equals(category1));

        assert !(category1.equals(category3));
        assert !(category3.equals(category1));
    }


    @Test
    public void When_UpdateCategory_Expect_ReflectedWell() {
        Category category1;
        Category category2;
        Category category3;
        Category category4;

        Dial dial1 = new Dial("도서 대출 연장", new Period(UnitOfTime.WEEK, 3), OffsetDateTime.of(2021, 12, 28, 12, 36, 43, 0, ZoneOffset.ofHours(9)));
        Dial dial2 = new Dial("도서 대출 연장", new Period(UnitOfTime.WEEK, 3), OffsetDateTime.of(2021, 12, 28, 12, 36, 43, 0, ZoneOffset.ofHours(9)));
        Dial dial3 = new Dial("산책", new Period(UnitOfTime.DAY, 3), OffsetDateTime.of(2021, 12, 28, 12, 36, 43, 0, ZoneOffset.ofHours(9)));
        Dial dial4 = new Dial("산책", new Period(UnitOfTime.DAY, 3), OffsetDateTime.of(2021, 12, 28, 12, 36, 43, 0, ZoneOffset.ofHours(9)));
        Dial dial5 = new Dial("판책", new Period(UnitOfTime.DAY, 3), OffsetDateTime.of(2021, 12, 28, 12, 36, 43, 0, ZoneOffset.ofHours(9)));

        ArrayList<Dial> dials1 = new ArrayList<Dial>() {
            {
                add(dial1);
                add(dial3);
            }
        };

        ArrayList<Dial> dials2 = new ArrayList<Dial>() {
            {
                add(dial2);
                add(dial4);
            }
        };

        ArrayList<Dial> dials3 = new ArrayList<Dial>() {
            {
                add(dial2);
            }
        };

        category1 = new Category("카테고리", dials1);
        category2 = new Category("카테고리", dials2);
        category3 = new Category("카테고리", dials3);
        category4 = new Category("category", dials2);

        assert !category1.addDial(dial2);
        assert category1.equals(category2);

        assert category1.addDials(dials2) == 0;
        assert category1.equals(category2);

        assert category1.removeDialByIndex(1) == dial3;
        assert category1.getDialCount() == 1;
        assert category1.equals(category3);

        assert category1.addDial(dial3);
        assert category1.getDialCount() == 2;
        assert category1.equals(category2);

        assert !category1.removeDialByObject(dial5);
        assert category1.equals(category2);

        assert category1.removeDialByObject(dial4);
        assert category1.equals(category3);
        assert category1.getDialCount() == 1;

        category4.setName("카테고리");
        assert category2.equals(category4);
    }
}