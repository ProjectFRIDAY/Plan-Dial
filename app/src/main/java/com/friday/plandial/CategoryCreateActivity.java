package com.friday.plandial;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

public class CategoryCreateActivity extends AppCompatActivity {

    private static final String TUTORIAL_MESSAGE = "각 다이얼의 시작 시간은 카테고리를 생성한 후에 수정해야 합니다.";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus_category);

        {
            // 뒤로가기 버튼 설정
            ImageButton backButton = findViewById(R.id.BackButton);
            backButton.setOnClickListener(view -> finish());
        }

        {
            int templateIndex = getIntent().getExtras().getInt("templateIndex");
            Template selectedTemplate = TemplateManager.getInstance().getTemplateByIndex(templateIndex);

            // 카테고리 이름 및 아이콘 기본값 설정
            EditText categoryNameEditView = findViewById(R.id.set_ct_name);
            ImageView categoryIconImageView = findViewById(R.id.Category_Image_Inner);
            categoryNameEditView.setText(selectedTemplate.getName());
            categoryIconImageView.setImageResource(selectedTemplate.getIcon());

            // 프리셋 리스트 표시
            RecyclerView presetList = findViewById(R.id.preset_list);

            GridLayoutManager gridlayoutManager = new GridLayoutManager(this, 2);
            presetList.setLayoutManager(gridlayoutManager);

            PresetListAdapter presetAdapter = new PresetListAdapter(selectedTemplate, true, true);
            presetList.setAdapter(presetAdapter);

            // 뷰 모델 설정
            CategorySettingViewModel categorySettingViewModel = new CategorySettingViewModel(this, presetAdapter);

            // 완료 버튼 설정
            Button completeButton = findViewById(R.id.CategoryPlus_Button);
            completeButton.setOnClickListener(view -> categorySettingViewModel.complete());

            // 튜토리얼 체킹
            SharedPreferences pref = getSharedPreferences("Tutorial", Activity.MODE_PRIVATE);
            if (!TemplateManager.getInstance().getEmptyTemplate().equals(selectedTemplate)
                    && pref.getBoolean("isFirstTemplate", true)) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("isFirstTemplate", false);
                editor.apply();
                Snackbar.make(findViewById(android.R.id.content), TUTORIAL_MESSAGE, Snackbar.LENGTH_INDEFINITE)
                        .setAction("확인", view -> {
                        }).show();
            }
        }
    }
}
