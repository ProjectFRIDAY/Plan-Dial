package com.example.plandial;

import android.app.Activity;
import android.widget.EditText;

public class DialSettingViewModel implements ISettingViewModel {
    private static final String FORMAT_STRING = "선택하신 내용대로 %s 다이얼을 생성할게요!";

    private final Activity activity;
    private final Category category;

    public DialSettingViewModel(Activity activity, Category category) {
        this.activity = activity;
        this.category = category;
    }

    @Override
    public boolean complete() {
        EditText dialName = activity.findViewById(R.id.Input_DialName);

        if (validate()) {
            // 구현해야 함
        }

        ConfirmDialogPresenter confirmDialogPresenter = new ConfirmDialogPresenter(this, this.activity, String.format(FORMAT_STRING, dialName.getText()));
        confirmDialogPresenter.show();

        return false;
    }

    @Override
    public void finish() {
        save();
        activity.finish();
    }

    @Override
    public void save() {
        EditText dialName = activity.findViewById(R.id.Input_DialName);
        LinearSelector timeUnit = activity.findViewById(R.id.unit_selector);
        DateTimeTextView startDay = activity.findViewById(R.id.Input_Startday);

        Dial dial = new Dial(
                dialName.getText().toString(),
                new Period(UnitOfTime.values()[timeUnit.getSelectedIndex() + 1], 1),
                startDay.getDateTime());

        category.addDial(dial);
    }

    private boolean validate() {
        // 입력이 유효한지 판단
        return true;
    }
}
