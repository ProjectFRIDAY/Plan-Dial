package com.friday.plandial;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Build;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.plandial.R;
import com.friday.plandial.db.WorkDatabase;
import com.friday.plandial.policy.BasicDialValidator;

import java.time.OffsetDateTime;

public class DialSettingViewModel implements ISettingViewModel {
    private static final String FORMAT_STRING = "선택하신 내용대로 %s 다이얼을 생성할게요!";
    private static final BasicDialValidator dialValidator = new BasicDialValidator();

    private final Activity activity;
    private final Category category;

    private final TextView period, unitOfTime;
    private final EditText dialNameView;
    private final DateTimeTextView startDayView;

    private String dialNameData;
    private Period periodData;
    private OffsetDateTime startDayData;

    public DialSettingViewModel(Activity activity, Category category) {
        this.activity = activity;
        this.category = category;

        this.dialNameView = activity.findViewById(R.id.Input_DialName);
        this.period = activity.findViewById(R.id.DialTime_Period);
        this.unitOfTime = activity.findViewById(R.id.DialTime_UnitOfTime);
        this.startDayView = activity.findViewById(R.id.Input_Startday);
    }

    @Override
    public boolean complete() {
        // 사용자가 값 입력을 완료했다고 알릴 때 호출되는 함수임
        UnitOfTime unit = UnitOfTime.nameToUnit.get(unitOfTime.getText().toString());
        assert unit != null;

        //region 뷰로부터 값 불러오기
        this.dialNameData = dialNameView.getText().toString().trim();
        this.dialNameView.setText(this.dialNameData);
        this.periodData = new Period(unit, Integer.parseInt(period.getText().toString()));
        this.startDayData = startDayView.getDateTime();
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
            } else if (!dialValidator.sameName(dialNameData, category)) {
                builder.setMessage("카테고리에 동일한 이름의 다이얼이 있습니다.");
                builder.show();
                return false;
            } else if (!dialValidator.validatePeriod(periodData)) {
                builder.setMessage("주기는 0보다 커야 합니다.");
                builder.show();
                return false;
            } else if (!dialValidator.validateStartDay(startDayData)) {
                this.startDayData = OffsetDateTime.now();
                startDayView.setByOffsetDateTime(startDayData);
            }
        }
        //endregion

        // 다이얼을 정말 생성할 지 사용자에게 확인함
        ConfirmDialogPresenter confirmDialogPresenter = new ConfirmDialogPresenter(this, this.activity, String.format(FORMAT_STRING, dialNameData));
        confirmDialogPresenter.show();

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void finish() {
        // 검증 과정까지 마친 후 세팅을 마무리하는 함수임

        save();
		PlanDialWidget.Update(activity); // widget update
        activity.finish();

    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void save() {
        // 카테고리에 새로운 다이얼 생성
        AlertDial dial = new AlertDial(
                activity.getApplicationContext(),
                dialNameData,
                periodData,
                startDayData);

        category.addDial(dial);
        WorkDatabase.getInstance().makeDial(category, dial);
    }
}
