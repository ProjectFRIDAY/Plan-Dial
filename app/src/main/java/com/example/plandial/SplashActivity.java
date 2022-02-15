package com.example.plandial;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.plandial.db.WorkDatabase;

// 추후 안드로이드 12에 추가된 SplashScreen API로 변경해야 함.
public class SplashActivity extends AppCompatActivity {
    private final int minLoadingTime = 300; // Splash 화면을 보여줄 최소 시간

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            // 아이콘 추천 준비
            IconRecommendation iconRecommendation = new IconRecommendation();
            iconRecommendation.roadIconData(this);

            // DB 준비
            DialManager.getInstance().resetAll();
            TemplateManager.getInstance().resetAll();
            WorkDatabase workDatabase = WorkDatabase.getInstance();
            workDatabase.ready(this);

            Thread.sleep(minLoadingTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
