package com.example.plandial;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SpinnableDialView mainDialSlider;
    ConstraintLayout mainDialLayout;
    RecyclerView urgentDialView;
    RecyclerView categoryDialView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urgentDialView = findViewById(R.id.urgent_dials);
        categoryDialView = findViewById(R.id.category_dials);
        mainDialSlider = findViewById(R.id.main_dial_slider);
        mainDialLayout = findViewById(R.id.main_dial_layout);

        {
            // 메인다이얼 슬라이더 설정 (객체 내부에서 ImageView 등록하도록 개선 필요)
            ArrayList<ImageView> circles = new ArrayList<>();
            circles.add(findViewById(R.id.category_circle_0));
            circles.add(findViewById(R.id.category_circle_1));
            circles.add(findViewById(R.id.category_circle_2));
            circles.add(findViewById(R.id.category_circle_3));
            circles.add(findViewById(R.id.category_circle_4));
            circles.add(findViewById(R.id.category_circle_5));
            circles.add(findViewById(R.id.category_circle_6));
            circles.add(findViewById(R.id.category_circle_7));
            circles.add(findViewById(R.id.category_circle_8));
            circles.add(findViewById(R.id.category_circle_9));
            mainDialSlider.setCircles(this, circles);
        }

        {
            // urgent dial 표시 설정
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            urgentDialView.setLayoutManager(layoutManager);

            UrgentDialAdapter adapter = new UrgentDialAdapter();
            urgentDialView.setAdapter(adapter);

            // category dial 표시 설정
            GridLayoutManager gridlayoutManager = new GridLayoutManager(this, 3);
            categoryDialView.setLayoutManager(gridlayoutManager);

            CategoryDialAdapter gridAdapter = new CategoryDialAdapter();
            categoryDialView.setAdapter(gridAdapter);
        }
    }
}