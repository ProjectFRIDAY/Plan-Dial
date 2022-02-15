package com.example.plandial;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.example.plandial.db.WorkDatabase;
import com.example.plandial.policy.BasicCategoryValidator;

import java.util.ArrayList;

public class CategorySettingViewModel implements ISettingViewModel {
    private static final String FORMAT_STRING = "선택하신 내용대로 %s 카테고리를 생성할게요!";

    private final Activity activity;
    private final Category category;

    private final EditText categoryNameView;
    private final PresetListAdapter presetListAdapter;

    public CategorySettingViewModel(Activity activity, PresetListAdapter presetListAdapter) {
        this.activity = activity;

        this.categoryNameView = activity.findViewById(R.id.set_ct_name);
        this.presetListAdapter = presetListAdapter;
        this.category = new Category(categoryNameView.getText().toString());
    }

    @Override
    public boolean complete() {
        //값 불러오기
        final String categoryNameData = this.categoryNameView.getText().toString().trim();
        this.categoryNameView.setText(categoryNameData);


        //region 값이 유효한 지 판단함
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setPositiveButton("확인", (dialogInterface, i) -> {
                // nothing
            });

            BasicCategoryValidator categoryValidator = new BasicCategoryValidator();

            if (!categoryValidator.validateName(categoryNameData)) {
                builder.setMessage("카테고리 이름은 1자 이상 16자 미만이어야 합니다.");
                builder.show();
                return false;
            }

            if (!categoryValidator.sameName(DialManager.getInstance(), categoryNameData, category)) {
                builder.setMessage("동일한 이름의 카테고리가 있습니다.");
                builder.show();
                return false;
            }
        }

        // 카테고리를 정말 생성할 지 사용자에게 확인함
        ConfirmDialogPresenter confirmDialogPresenter = new ConfirmDialogPresenter(this, this.activity, String.format(FORMAT_STRING, categoryNameData));
        confirmDialogPresenter.show();

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void finish() {
        save();
        Intent intent = new Intent(activity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void save() {
        category.setName(categoryNameView.getText().toString());

        ArrayList<Preset> selectedPresets = presetListAdapter.getSelectedPresets();

        for (Preset preset : selectedPresets) {
            category.addDial(preset.toAlertDial(activity));
        }

        DialManager.getInstance().addCategory(category);
        WorkDatabase.getInstance().makeCategoryWithDials(category);
    }
}
