package com.example.plandial;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.plandial.db.WorkDatabase;

// 추후 안드로이드 12에 추가된 SplashScreen API로 변경해야 함.
public class SplashActivity extends AppCompatActivity {
    private final int minLoadingTime = 100; // Splash 화면을 보여줄 최소 시간 (단위: ms)

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean ready = false;

        try {
            // 아이콘 추천 준비
            IconRecommendation iconRecommendation = new IconRecommendation();
            iconRecommendation.roadIconData(this);

            // DB 준비
            DialManager.getInstance().resetAll();
            TemplateManager.getInstance().resetAll();
            WorkDatabase workDatabase = WorkDatabase.getInstance();
            ready = workDatabase.ready(this);

            Thread.sleep(minLoadingTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (ready) {
            Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("데이터를 불러올 수 없음")
                    .setMessage("데이터를 불러올 수 없습니다.\n해당 오류가 계속될 경우, 개발자에게 문의해주세요.")
                    .setCancelable(false)
                    .setPositiveButton("예", (dialogInterface, i) -> this.finish());
            builder.show();
        }
    }
}
