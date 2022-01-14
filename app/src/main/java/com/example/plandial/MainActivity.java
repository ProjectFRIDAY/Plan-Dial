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

    private IDialDao mIDialDao;             // 맴버변수 선언
    private ICategoryDao mICategoryDao;
    private IPresetDao mIPresetDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PlanDatabase database = Room.databaseBuilder(getApplicationContext(), PlanDatabase.class, "PlanDial")
                .fallbackToDestructiveMigration()   // 스키마 (database) 버전 변경가능
                .allowMainThreadQueries()           // Main Thread에서 DB에 IO(입출력) 가능
                .build();

        //TEST  -> Dial test
        // category or preset의 db를 사용하시려면 이름 바꿔서 사용하시면 됩니다.

        mIDialDao = database.iDialDao(); //인터페이스 객체 할당
        mICategoryDao = database.iCategoryDao(); //인터페이스 객체 할당
        mIPresetDao = database.iPresetDao(); // 인터페이스 객체 할당

        // 다이얼 데이서 생성
//        DialTable dialTable = new DialTable(); //새로운 객체 인스턴스 생성
//        dialTable.setDialName("빨래");
//        dialTable.setDialTimeUnit("Day");
//        dialTable.setDialTime(3);
//
//        mIDialDao.insetDial(dialTable);

        // 다이얼 데이서 수정
//        DialTable dialTable1 = new DialTable(); // 새로운 객체 인스턴스 생성
//        dialTable1.setId(2);
//        dialTable1.setDialName("달리기");
//        dialTable1.setDialTimeUnit("Day");
//        dialTable1.setDialTime(2);
//
//        mIDialDao.updateDial(dialTable1);

        // 다이얼 데이터 삭제
//        DialTable dialTable2 = new DialTable(); // 새로운 객체 인스턴스 생성
//        dialTable2.setId(3);
//
//        mIDialDao.deleteDial(dialTable2);
    }
}