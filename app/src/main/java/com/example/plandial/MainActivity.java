package com.example.plandial;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.plandial.db.ICategoryDao;
import com.example.plandial.db.IDialDao;
import com.example.plandial.db.IPresetDao;
import com.example.plandial.db.PlanDatabase;
import com.example.plandial.db.WorkDatabase;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final int SYNCING_URGENT_PERIOD = 5 * UnitOfTime.MILLIS_PER_SECOND; // 단위: ms

    private SpinnableDialView mainDialSlider;
    private ConstraintLayout mainDialLayout;
    private RecyclerView urgentDialView;
    private RecyclerView categoryDialView;
    private StatusDisplayLayout statusDisplayLayout;
    private ImageButton add_button;

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 아이콘 추천 준비
        IconRecommendation iconRecommendation = new IconRecommendation();
        iconRecommendation.roadIconData(this);

        // DB 준비
        WorkDatabase workDatabase = WorkDatabase.getInstance();
        workDatabase.ready(this);

        // for widget testing
        Intent intent = new Intent(this, PlanDialWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra("itemList", "모임:baseline_groups_black/샤워:outline_bathroom_black/쇼핑:outline_shopping_cart_black/빨래:outline_local_laundry_service_black/교회:outline_church_black");
        sendBroadcast(intent);

        //region test code
//        AlertDial alertDial1 = new AlertDial(this, "빨래", new Period(UnitOfTime.DAY, 1), OffsetDateTime.of(2022, 1, 24, 19, 26, 0, 0, ZoneOffset.ofHours(9)));
//        AlertDial alertDial2 = new AlertDial(this, "청소", new Period(UnitOfTime.DAY, 1), OffsetDateTime.of(2022, 1, 24, 19, 27, 0, 0, ZoneOffset.ofHours(9)));
//        AlertDial alertDial3 = new AlertDial(this, "공부", new Period(UnitOfTime.DAY, 1), OffsetDateTime.of(2022, 1, 24, 19, 28, 0, 0, ZoneOffset.ofHours(9)));
//        AlertDial alertDial4 = new AlertDial(this, "코딩", new Period(UnitOfTime.DAY, 1), OffsetDateTime.of(2022, 1, 24, 19, 29, 0, 0, ZoneOffset.ofHours(9)));
//        Category category1 = new Category("나는 바보다");
//        category1.addDial(alertDial1);
//        category1.addDial(alertDial2);
//        category1.addDial(alertDial3);
//        Category category2 = new Category("나는 바보다2");
//        category2.addDial(alertDial1);
//        category2.addDial(alertDial4);
//        DialManager.getInstance().addCategory(category1);
//        DialManager.getInstance().addCategory(category2);
        //endregion

        add_button = findViewById(R.id.add_button);
        urgentDialView = findViewById(R.id.urgent_dials);
        categoryDialView = findViewById(R.id.category_dials);
        mainDialSlider = findViewById(R.id.main_dial_slider);
        mainDialLayout = findViewById(R.id.main_dial_layout);
        statusDisplayLayout = findViewById(R.id.status_display_layout);

        add_button.setOnClickListener(view -> {
            Intent templateIntent = new Intent(getApplicationContext(), TemplateChoiceActivity.class);
            startActivity(templateIntent);
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.categoryDialView.getAdapter().notifyDataSetChanged();
    }
}