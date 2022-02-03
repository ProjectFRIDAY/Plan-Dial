package com.example.plandial;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Switch;

import com.example.plandial.policy.BasicDialValidator;
import com.example.plandial.policy.IDialValidator;

import java.time.OffsetDateTime;

public class DialEditingViewModel implements ISettingViewModel {
    private static final String FORMAT_STRING = "선택하신 내용대로 %s 다이얼을 수정할게요!";
    private static final IDialValidator dialValidator = new BasicDialValidator();

    private final Activity activity;
    private final Dial dial;

    private final EditText dialNameView;
    private final LinearSelector timeUnitView;
    private final Switch unableSwitchView;

    private String dialNameData;
    private Period periodData;
    private boolean unableData;

    public DialEditingViewModel(Activity activity, Dial dial) {
        this.activity = activity;
        this.dial = dial;

        this.dialNameView = activity.findViewById(R.id.Input_DialName);
        this.timeUnitView = activity.findViewById(R.id.unit_selector);
        this.unableSwitchView = activity.findViewById(R.id.switchButton);
    }

    @Override
    public boolean complete() {
        // 사용자가 값 입력을 완료했다고 알릴 때 호출되는 함수임

        //region 뷰로부터 값 불러오기
        this.dialNameData = dialNameView.getText().toString();
        this.periodData = new Period(UnitOfTime.values()[timeUnitView.getSelectedIndex() + 1], 1);
        this.unableData = unableSwitchView.isChecked();
        //endregion

        //region 값이 유효한 지 판단함
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // nothing
                }
            });


            if (!dialValidator.validateName(dialNameData)) {
                builder.setMessage("다이얼 이름은 1자 이상 10자 미만이어야 합니다.");
                builder.show();
                return false;
            } else if (!dialValidator.validatePeriod(periodData)) {
                builder.setMessage("주기는 0보다 커야 합니다.");
                builder.show();
                return false;
            }
        }
        //endregion

        // 다이얼을 정말 수정할 지 사용자에게 확인함
        ConfirmDialogPresenter confirmDialogPresenter = new ConfirmDialogPresenter(this, this.activity, String.format(FORMAT_STRING, dialNameData));
        confirmDialogPresenter.show();

        return true;
    }

    @Override
    public void finish() {
        // 검증 과정까지 마친 후 세팅을 마무리하는 함수임
        save();
        activity.finish();
    }

    @Override
    public void save() {
        // 다이얼 수정
        dial.setName(activity.getApplicationContext(), dialNameData);
        dial.setPeriod(periodData);

        if (unableData) {
            dial.disable(activity.getApplicationContext());
        } else {
            dial.restart(activity.getApplicationContext());
        }
    }
}
