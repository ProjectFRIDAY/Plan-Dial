package com.example.plandial;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EditDialActivity extends AppCompatActivity {
    private Intent intent;
    String dialName;
    private ImageButton backButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dial);
        EditText Input_DialName = findViewById(R.id.Input_DialName);

        {
            // 뒤로가기 버튼 설정
            backButton = findViewById(R.id.BackButton);
            backButton.setOnClickListener(view -> finish());
        }

        intent = getIntent();
        Input_DialName.setText(intent.getStringExtra("dialName"));
    }
}