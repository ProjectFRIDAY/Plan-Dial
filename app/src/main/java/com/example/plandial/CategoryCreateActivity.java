package com.example.plandial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryCreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus_category);

        {
            // 뒤로가기 버튼 설정
            ImageButton backButton = findViewById(R.id.BackButton);
            Intent intent = new Intent(this, TemplateChoiceActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            backButton.setOnClickListener(view -> startActivity(intent));
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

            PresetListAdapter presetAdapter = new PresetListAdapter(selectedTemplate, true);
            presetList.setAdapter(presetAdapter);

            // 뷰 모델 설정
            CategorySettingViewModel categorySettingViewModel = new CategorySettingViewModel(this, presetAdapter);

            // 완료 버튼 설정
            ImageButton completeButton = findViewById(R.id.DialPlus_Button);
            completeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    categorySettingViewModel.complete();
                }
            });
        }
    }
}
