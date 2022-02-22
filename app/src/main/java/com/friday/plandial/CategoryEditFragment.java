package com.friday.plandial;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.example.plandial.R;
import com.friday.plandial.db.WorkDatabase;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CategoryEditFragment extends BottomSheetDialogFragment {
    private final DialManager dialManager = DialManager.getInstance();
    private Category category;
    private CategoryEditingViewModel categoryEditingViewModel;

    private EditText InputCategoryName;
    private Button RemoveCategory, SaveCategory;

    public CategoryEditFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_edit_category, container, false);

        InputCategoryName = view.findViewById(R.id.Input_CategoryName);
        RemoveCategory = view.findViewById(R.id.CategoryRemove_Button);
        SaveCategory = view.findViewById(R.id.CategorySave_Button);

        assert getArguments() != null;
        category = dialManager.getCategoryByName(getArguments().getString("categoryName"));
        String categoryName = getArguments().getString("categoryName");
        InputCategoryName.setText(categoryName);

        //저장
        categoryEditingViewModel = new CategoryEditingViewModel(view, this, category);

        //카테고리 이름 수정
        SaveCategory.setOnClickListener(view1 -> categoryEditingViewModel.complete());

        //카테고리 삭제
        RemoveCategory.setOnClickListener(view12 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("정말로 카테고리를 삭제하시겠습니까?")
                    .setPositiveButton("예", (dialogInterface, i) -> {
                        for (Dial dial : category.getAllDials()) {
                            AlertDial alertDial = (AlertDial) dial;
                            alertDial.disable(getContext());
                        }
                        dialManager.removeCategoryByObject(category);
                        WorkDatabase.getInstance().delCategory(category);
                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction().remove(CategoryEditFragment.this).commit();
                        requireActivity().recreate();
						PlanDialWidget.Update(requireActivity()); // widget update
                    })
                    .setNegativeButton("아니오", (dialogInterface, i) -> {
                    });
            builder.show();
        });

        return view;
    }
}

