package com.example.plandial;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PlusDialActivity extends AppCompatActivity {
    private ImageButton backButton;
    private ImageView iconImage;

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
            // 아이콘 추천
            iconImage = findViewById(R.id.DialImage_Recommend);
            IconRecommendation iconRecommendation = new IconRecommendation();
            if(iconRecommendation.getIsReady()){
                int result = iconRecommendation.getIconByName(this, "수영");
                iconImage.setImageResource(result);
            }
        }
    }
}
