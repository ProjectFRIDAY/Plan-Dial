package com.example.plandial;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Build;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.plandial.db.WorkDatabase;
import com.example.plandial.policy.EditDialValidator;

public class DialEditingViewModel implements ISettingViewModel {
    private static final String FORMAT_STRING = "선택하신 내용대로 %s 다이얼을 수정할게요!";
    private static final EditDialValidator dialValidator = new EditDialValidator();

    private final Activity activity;
    private final Category category;
    private final AlertDial dial;

    private final TextView period, unitOfTime;
    private final EditText dialNameView;
    private final Switch unableSwitchView;
    private final CheckBox iconCheckbox;

    private String dialNameData;
    private Period periodData;
    private boolean unableData;

    public DialEditingViewModel(Activity activity, AlertDial dial, Category category) {
        this.activity = activity;
        this.category = category;
        this.dial = dial;

        this.dialNameView = activity.findViewById(R.id.Input_DialName);
        this.period = activity.findViewById(R.id.DialTime_Period);
        this.unitOfTime = activity.findViewById(R.id.DialTime_UnitOfTime);
        this.unableSwitchView = activity.findViewById(R.id.switchButton);
        this.iconCheckbox = activity.findViewById(R.id.Icon_Checkbox);
    }

    @Override
    public boolean complete() {
        // 사용자가 값 입력을 완료했다고 알릴 때 호출되는 함수임
        UnitOfTime unit = UnitOfTime.nameToUnit.get(unitOfTime.getText().toString());
        assert unit != null;

        //region 뷰로부터 값 불러오기
        this.dialNameData = dialNameView.getText().toString();
        this.periodData = new Period(unit, Integer.parseInt(period.getText().toString()));
        this.unableData = unableSwitchView.isChecked();
        //endregion

        //region 값이 유효한 지 판단함
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setPositiveButton("확인", (dialogInterface, i) -> {
                // nothing
            });

            if (!dialValidator.validateName(dialNameData)) {
                builder.setMessage("다이얼 이름은 1자 이상 10자 미만이어야 합니다.");
                builder.show();
                return false;
            } else if (!dialValidator.validateName(dialNameData, category, dial)) {
                builder.setMessage("카테고리에 동일한 이름의 다이얼이 있습니다.");
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

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void finish() {
        // 검증 과정까지 마친 후 세팅을 마무리하는 함수임
        save();
        activity.finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void save() {
        String oldName = dial.getName();

        // 다이얼 수정
        if(iconCheckbox.isChecked()) {
            dial.setName(activity.getApplicationContext(), dialNameData);
        } else {
            dial.setName(dialNameData);
        }

        dial.setPeriod(periodData);

        if (unableData) {
            dial.disable(activity.getApplicationContext());
        } else {
            dial.restart(activity.getApplicationContext());
        }

        WorkDatabase.getInstance().fixDial(category, dial, oldName);
    }
}
