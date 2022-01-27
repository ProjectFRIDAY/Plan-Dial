package com.example.plandial;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PlusDialActivity extends AppCompatActivity {
    private ImageButton backButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus_category);

        {
            // 뒤로가기 버튼 설정
            backButton = findViewById(R.id.BackButton);
            backButton.setOnClickListener(view -> finish());
        }
    }
}
