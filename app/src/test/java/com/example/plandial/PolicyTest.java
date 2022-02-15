package com.example.plandial;

import com.example.plandial.policy.BasicDialValidator;
import com.example.plandial.policy.EditCategoryValidator;
import com.example.plandial.policy.EditDialValidator;

import org.junit.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class PolicyTest {
    @Test
    public void When_Create_AlertDial() {
        BasicDialValidator basicDialValidator = new BasicDialValidator();

        assert basicDialValidator.validateName("쇼핑");
        assert !basicDialValidator.validateName(null);
        assert !basicDialValidator.validateName("");
        assert !basicDialValidator.validateName("이마트나 홈플러스 가기");

        assert basicDialValidator.validatePeriod(new Period(UnitOfTime.DAY, 1));
        assert !basicDialValidator.validatePeriod(null);
        assert !basicDialValidator.validateStartDay(OffsetDateTime.of(2021, 12, 23, 12, 36, 43, 0, ZoneOffset.ofHours(9)));
        assert basicDialValidator.validateStartDay(OffsetDateTime.of(2022, 12, 23, 12, 36, 43, 0, ZoneOffset.ofHours(9)));
        assert !basicDialValidator.validateStartDay(null);

        Category category = new Category("테스트");
        Dial dial = new Dial("다이얼", 0, new Period(UnitOfTime.DAY, 1));
        category.addDial(dial);

        assert basicDialValidator.validateName("쇼핑", category);
        assert !basicDialValidator.validateName("다이얼", category);
    }

    @Test
    public void When_Edit_AlertDial() {
        EditDialValidator editDialValidator = new EditDialValidator();

        assert editDialValidator.validateName("쇼핑");
        assert !editDialValidator.validateName(null);
        assert !editDialValidator.validateName("");
        assert !editDialValidator.validateName("이마트나 홈플러스 가기");

        assert editDialValidator.validatePeriod(new Period(UnitOfTime.DAY, 1));
        assert !editDialValidator.validatePeriod(null);
        assert !editDialValidator.validateStartDay(OffsetDateTime.of(2021, 12, 23, 12, 36, 43, 0, ZoneOffset.ofHours(9)));
        assert editDialValidator.validateStartDay(OffsetDateTime.of(2022, 12, 23, 12, 36, 43, 0, ZoneOffset.ofHours(9)));
        assert !editDialValidator.validateStartDay(null);

        Category category = new Category("테스트");
        Dial dial = new Dial("다이얼", 0, new Period(UnitOfTime.DAY, 1));
        Dial target_dial = new Dial("아침 식사", 0, new Period(UnitOfTime.DAY, 1));
        category.addDial(dial);
        category.addDial(target_dial);

        assert editDialValidator.validateName("쇼핑", category, target_dial);
        assert !editDialValidator.validateName("다이얼", category, target_dial);
        assert editDialValidator.validateName("아침 식사", category, target_dial);
    }

    @Test
    public void When_Create_Category() {
        // 만들어야 함.
    }

    @Test
    public void When_Edit_Category() {
        EditCategoryValidator editCategoryValidator = new EditCategoryValidator();

        assert editCategoryValidator.validateName("공부 관련 카테고리");
        assert !editCategoryValidator.validateName(null);
        assert !editCategoryValidator.validateName("당신을 향한 나의 사랑은 무조건 무조건이야");
        assert !editCategoryValidator.validateName("");

        DialManager dialManager = DialManager.getInstance();
        Category category1 = new Category("개발자 인생");
        Category category2 = new Category("취미 생활");
        dialManager.addCategory(category1);
        dialManager.addCategory(category2);

        assert editCategoryValidator.sameName(dialManager, "공부 관련 카테고리", category1);
        assert editCategoryValidator.sameName(dialManager, "개발자 인생", category1);
        assert !editCategoryValidator.sameName(dialManager, "취미 생활", category1);

    }
}
