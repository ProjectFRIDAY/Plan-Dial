package com.example.plandial;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CategoryEditFragment extends BottomSheetDialogFragment {
    Context context;

    private EditText InputCategoryName;
    private Button RemoveCategory;

    public CategoryEditFragment(Context context) {
        this.context = context;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_edit_category, container, false);

        InputCategoryName = view.findViewById(R.id.Input_CategoryName);
        RemoveCategory = view.findViewById(R.id.CategoryRemove_Button);

        String categoryName = getArguments().getString("categoryName");
        InputCategoryName.setText(categoryName);

        RemoveCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "확인", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        return view;
    }
}

