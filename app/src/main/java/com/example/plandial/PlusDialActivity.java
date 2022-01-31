package com.example.plandial;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PlusDialActivity extends AppCompatActivity {
    private ImageButton backButton;
    private FormatTextView startDayInput;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus_dial);

        {
            // 뒤로가기 버튼 설정
            backButton = findViewById(R.id.BackButton);
            backButton.setOnClickListener(view -> finish());
        }

        {
            startDayInput = findViewById(R.id.Input_Startday);
            Activity activity = this;
            startDayInput.setOnClickListener(view -> {
                DateTimePickerDialog dateTimePickerDialog = new DateTimePickerDialog(activity, startDayInput);
                dateTimePickerDialog.show();
            });
        }
    }
}
