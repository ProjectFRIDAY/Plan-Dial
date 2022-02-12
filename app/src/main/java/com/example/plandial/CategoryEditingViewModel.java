package com.example.plandial;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.plandial.db.WorkDatabase;
import com.example.plandial.policy.EditCategoryValidator;

public class CategoryEditingViewModel implements ISettingViewModel {
    private static final String FORMAT_STRING = "변경하신 이름대로 %s 카테고리를 수정할게요!";
    private static final EditCategoryValidator categoryValidator = new EditCategoryValidator();

    private Fragment fragment;
    private final Category category;
    private final DialManager dialManager = DialManager.getInstance();

    private String categoryNameData;
    private final EditText categoryNameView;

    public CategoryEditingViewModel(View view, Fragment fragment, Category category) {
        this.fragment = fragment;
        this.category = category;
        this.categoryNameView = view.findViewById(R.id.Input_CategoryName);
    }

    @Override
    public boolean complete() {

        //region 뷰로부터 값 불러오기
        this.categoryNameData = categoryNameView.getText().toString();
        //endregion

        //region 값이 유효한 지 판단함
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
            builder.setPositiveButton("확인", (dialogInterface, i) -> {
                // nothing
            });
            if (!categoryValidator.validateName(categoryNameData)) {
                builder.setMessage("카테고리 이름은 1자 이상 16자 미만이어야 합니다.");
                builder.show();
                return false;
            } else if (!categoryValidator.sameName(dialManager,categoryNameData, category)) {
                builder.setMessage("동일한 이름의 카테고리가 있습니다.");
                builder.show();
                return false;
            }
        }
        //endregion

        // 카테고리를 정말 수정할 지 사용자에게 확인함
        ConfirmDialogPresenter confirmDialogPresenter = new ConfirmDialogPresenter(this, fragment.getActivity(), String.format(FORMAT_STRING, categoryNameData));
        confirmDialogPresenter.show();

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void finish() {
        // 검증 과정까지 마친 후 세팅을 마무리하는 함수임
        save();
        Intent intent = new Intent(fragment.getContext(), MainActivity.class);
        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
        fragment.getActivity().startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void save() {
        String oldName = category.getName();

        // 카테고리 수정
        category.setName(categoryNameData);

        WorkDatabase.getInstance().fixCategory(category, oldName);
    }
}