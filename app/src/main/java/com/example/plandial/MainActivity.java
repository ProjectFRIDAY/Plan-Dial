package com.example.plandial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;

import com.example.plandial.db.DialTable;
import com.example.plandial.db.ICategoryDao;
import com.example.plandial.db.IDialDao;
import com.example.plandial.db.IPresetDao;
import com.example.plandial.db.PlanDatabase;

public class MainActivity extends AppCompatActivity {

    private IDialDao iDialDao;             // 멤버변수 선언
    private ICategoryDao iCategoryDao;
    private IPresetDao iPresetDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PlanDatabase database = Room.databaseBuilder(getApplicationContext(), PlanDatabase.class, "PlanDial")
                .fallbackToDestructiveMigration()   // 스키마 (database) 버전 변경가능
                .allowMainThreadQueries()           // Main Thread에서 DB에 IO(입출력) 가능
                .build();

        iDialDao = database.iDialDao(); //인터페이스 객체 할당
        iCategoryDao = database.iCategoryDao(); //인터페이스 객체 할당
        iPresetDao = database.iPresetDao(); // 인터페이스 객체 할당
    }
}