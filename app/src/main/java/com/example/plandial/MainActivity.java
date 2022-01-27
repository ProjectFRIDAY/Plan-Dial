package com.example.plandial;

import android.os.Bundle;
import android.widget.ImageView;

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

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final int SYNCING_URGENT_PERIOD = 1 * UnitOfTime.SECONDS_PER_MINUTE * UnitOfTime.MILLIS_PER_SECOND; // 단위: ms

    SpinnableDialView mainDialSlider;
    ConstraintLayout mainDialLayout;
    RecyclerView urgentDialView;
    RecyclerView categoryDialView;
    StatusDisplayLayout statusDisplayLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //region test code
        Dial dial1 = new Dial(this, "빨래", new Period(UnitOfTime.DAY, 1), OffsetDateTime.of(2022, 1, 24, 19, 26, 0, 0, ZoneOffset.ofHours(9)));
        Dial dial2 = new Dial(this, "청소", new Period(UnitOfTime.DAY, 1), OffsetDateTime.of(2022, 1, 24, 19, 27, 0, 0, ZoneOffset.ofHours(9)));
        Dial dial3 = new Dial(this, "공부", new Period(UnitOfTime.DAY, 1), OffsetDateTime.of(2022, 1, 24, 19, 28, 0, 0, ZoneOffset.ofHours(9)));
        Dial dial4 = new Dial(this, "코딩", new Period(UnitOfTime.DAY, 1), OffsetDateTime.of(2022, 1, 24, 19, 29, 0, 0, ZoneOffset.ofHours(9)));
        Dial dial5 = new Dial(this, "코딩5", new Period(UnitOfTime.DAY, 1), OffsetDateTime.of(2022, 1, 24, 19, 29, 0, 0, ZoneOffset.ofHours(9)));
        Dial dial6 = new Dial(this, "코딩6", new Period(UnitOfTime.DAY, 1), OffsetDateTime.of(2022, 1, 24, 19, 29, 0, 0, ZoneOffset.ofHours(9)));
        Dial dial7 = new Dial(this, "코딩7", new Period(UnitOfTime.DAY, 1), OffsetDateTime.of(2022, 1, 24, 19, 29, 0, 0, ZoneOffset.ofHours(9)));
        Dial dial8 = new Dial(this, "코딩8", new Period(UnitOfTime.DAY, 1), OffsetDateTime.of(2022, 1, 24, 19, 29, 0, 0, ZoneOffset.ofHours(9)));
        Dial dial9 = new Dial(this, "코딩9", new Period(UnitOfTime.DAY, 1), OffsetDateTime.of(2022, 1, 24, 19, 29, 0, 0, ZoneOffset.ofHours(9)));
        Dial dial10 = new Dial(this, "코딩10", new Period(UnitOfTime.DAY, 1), OffsetDateTime.of(2022, 1, 24, 19, 29, 0, 0, ZoneOffset.ofHours(9)));
        Dial dial11 = new Dial(this, "코딩11", new Period(UnitOfTime.DAY, 1), OffsetDateTime.of(2022, 1, 24, 19, 29, 0, 0, ZoneOffset.ofHours(9)));
        Dial dial12 = new Dial(this, "코딩12", new Period(UnitOfTime.DAY, 1), OffsetDateTime.of(2022, 1, 24, 19, 29, 0, 0, ZoneOffset.ofHours(9)));
        Dial dial13 = new Dial(this, "코딩13", new Period(UnitOfTime.DAY, 1), OffsetDateTime.of(2022, 1, 24, 19, 29, 0, 0, ZoneOffset.ofHours(9)));
        Dial dial14 = new Dial(this, "코딩14", new Period(UnitOfTime.DAY, 1), OffsetDateTime.of(2022, 1, 24, 19, 29, 0, 0, ZoneOffset.ofHours(9)));
        Dial dial15 = new Dial(this, "코딩15", new Period(UnitOfTime.DAY, 1), OffsetDateTime.of(2022, 1, 24, 19, 29, 0, 0, ZoneOffset.ofHours(9)));
        Dial dial16 = new Dial(this, "코딩16", new Period(UnitOfTime.DAY, 1), OffsetDateTime.of(2022, 1, 24, 19, 29, 0, 0, ZoneOffset.ofHours(9)));
        Category category1 = new Category("나는 바보다");
        category1.addDial(dial1);
        category1.addDial(dial2);
        category1.addDial(dial3);
        category1.addDial(dial4);
        category1.addDial(dial5);
        category1.addDial(dial6);
        category1.addDial(dial7);
        category1.addDial(dial8);
        category1.addDial(dial9);
        category1.addDial(dial10);
        category1.addDial(dial11);
        category1.addDial(dial12);
        category1.addDial(dial13);
        category1.addDial(dial14);
        category1.addDial(dial15);
        category1.addDial(dial16);
        Category category2 = new Category("나는 바보다2");
        category2.addDial(dial1);
        category2.addDial(dial4);
        DialManager.getInstance().addCategory(category1);
        DialManager.getInstance().addCategory(category2);
        //endregion

        urgentDialView = findViewById(R.id.urgent_dials);
        categoryDialView = findViewById(R.id.category_dials);
        mainDialSlider = findViewById(R.id.main_dial_slider);
        mainDialLayout = findViewById(R.id.main_dial_layout);
        statusDisplayLayout = findViewById(R.id.status_display_layout);

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
                    adapter.syncDials();
                }
            };
            Timer timer = new Timer();
            timer.schedule(syncUrgentTask, 0, SYNCING_URGENT_PERIOD);
        }

        PlanDatabase database = Room.databaseBuilder(getApplicationContext(), PlanDatabase.class, "PlanDial")
                .fallbackToDestructiveMigration()   // 스키마 (database) 버전 변경가능
                .allowMainThreadQueries()           // Main Thread에서 DB에 IO(입출력) 가능
                .build();

        // 멤버변수 선언
        IDialDao iDialDao = database.iDialDao(); //인터페이스 객체 할당
        ICategoryDao iCategoryDao = database.iCategoryDao(); //인터페이스 객체 할당
        IPresetDao iPresetDao = database.iPresetDao(); // 인터페이스 객체 할당

    }
}