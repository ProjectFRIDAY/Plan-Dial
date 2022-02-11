package com.example.plandial;

import android.app.Activity;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.example.plandial.policy.BasicCategoryValidator;

import java.util.ArrayList;

public class CategorySettingViewModel implements ISettingViewModel {
    private static final String FORMAT_STRING = "선택하신 내용대로 %s 카테고리를 생성할게요!";

    private final Activity activity;

    private final EditText dialNameView;
    private final PresetListAdapter presetListAdapter;

    public CategorySettingViewModel(Activity activity, PresetListAdapter presetListAdapter) {
        this.activity = activity;

        this.dialNameView = activity.findViewById(R.id.set_ct_name);
        this.presetListAdapter = presetListAdapter;
    }

    @Override
    public boolean complete() {
        //값 불러오기
        final String categoryNameData = dialNameView.getText().toString();

        //region 값이 유효한 지 판단함
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setPositiveButton("확인", (dialogInterface, i) -> {
                // nothing
            });

            BasicCategoryValidator categoryValidator = new BasicCategoryValidator();

            if (!categoryValidator.validateName(categoryNameData)) {
                builder.setMessage("다이얼 이름은 1자 이상 10자 미만이어야 합니다.");
                builder.show();
                return false;
            }
        }

        // 카테고리를 정말 생성할 지 사용자에게 확인함
        ConfirmDialogPresenter confirmDialogPresenter = new ConfirmDialogPresenter(this, this.activity, String.format(FORMAT_STRING, categoryNameData));
        confirmDialogPresenter.show();

        return true;
    }

    @Override
    public void finish() {
        save();
        
        activity.finish();
    }

    @Override
    public void save() {
        Category category = new Category(dialNameView.getText().toString());

        ArrayList<Preset> selectedPresets = presetListAdapter.getSelectedPresets();

        for (Preset preset : selectedPresets) {
            category.addDial(preset.toAlertDial());
        }

        DialManager.getInstance().addCategory(category);
    }
}
