package com.friday.plandial;

import android.app.Activity;
import android.app.AlertDialog;
import android.widget.EditText;

import com.example.plandial.R;

public class ConfirmDialogPresenter {
    public static final String POSITIVE_WORD = "완료";
    public static final String NEGATIVE_WORD = "취소";

    private final AlertDialog.Builder builder;

    public ConfirmDialogPresenter(ISettingViewModel settingViewModel, Activity activity, String message) {

        EditText dialName = activity.findViewById(R.id.Input_DialName);

        // 빌더 생성
        builder = new AlertDialog.Builder(activity);
        builder.setMessage(message)
                .setPositiveButton(POSITIVE_WORD, (dialogInterface, i) -> settingViewModel.finish())
                .setNegativeButton(NEGATIVE_WORD, (dialogInterface, i) -> {
                    // nothing
                });
    }

    public void show() {
        builder.show();
    }
}
