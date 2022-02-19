package com.friday.plandial;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plandial.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final int SYNCING_URGENT_PERIOD = 5 * UnitOfTime.MILLIS_PER_SECOND; // 단위: ms

    private SpinnableDialView mainDialSlider;
    private RecyclerView urgentDialView;
    private RecyclerView categoryDialView;
    private StatusDisplayLayout statusDisplayLayout;
    private ImageButton add_button;
    private TextView categoryNameView;

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PlanDialWidget.WakeUp(this); // widget 미작동시 깨우기

        add_button = findViewById(R.id.add_button);
        urgentDialView = findViewById(R.id.urgent_dials);
        categoryDialView = findViewById(R.id.category_dials);
        mainDialSlider = findViewById(R.id.main_dial_slider);
        statusDisplayLayout = findViewById(R.id.status_display_layout);
        categoryNameView = findViewById(R.id.category_name);

        add_button.setOnClickListener(view -> {
            Intent templateIntent = new Intent(getApplicationContext(), TemplateChoiceActivity.class);
            startActivity(templateIntent);
        });

        categoryNameView.setOnLongClickListener(view -> {
            if (!categoryNameView.getText().equals("빈 카테고리")) {
                CategoryEditFragment categoryEditFragment = new CategoryEditFragment();
                Bundle bundle = new Bundle();
                bundle.putString("categoryName", (String) categoryNameView.getText());
                categoryEditFragment.setArguments(bundle);
                categoryEditFragment.show(getSupportFragmentManager(), categoryEditFragment.getTag());
            }
            return false;
        });

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
            mainDialSlider.setCategoryNameView(findViewById(R.id.category_name));

            //ConstraintLayout mainDialLayout = findViewById(R.id.main_dial_layout);
            mainDialSlider.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mainDialSlider.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    mainDialSlider.arrangeCircles();
                }
            });
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
            mainDialSlider.setCategoryDialAdapter(gridAdapter);

            gridAdapter.setStatusDisplayLayout(statusDisplayLayout);

            // urgent dial 주기적 동기화
            TimerTask syncUrgentTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(adapter::syncDials);
                }
            };
            Timer timer = new Timer();
            timer.schedule(syncUrgentTask, 0, SYNCING_URGENT_PERIOD);
        }

        // 튜토리얼 체킹
        if (DialManager.getInstance().getCategoryCount() == 0) {
            Intent templateIntent = new Intent(getApplicationContext(), TutorialMainActivity.class);
            startActivity(templateIntent);
        } else {
            SharedPreferences pref = getSharedPreferences("Tutorial", Activity.MODE_PRIVATE);
            if (pref.getBoolean("isFirst", true)) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("isFirst", false);
                editor.apply();

                Intent templateIntent = new Intent(getApplicationContext(), TutorialEditActivity.class);
                startActivity(templateIntent);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.categoryDialView.getAdapter().notifyDataSetChanged();
    }
}