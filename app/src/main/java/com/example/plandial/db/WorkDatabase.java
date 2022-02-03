package com.example.plandial.db;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.TableLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.plandial.DialManager;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorkDatabase {
    private static final String PresetDatabase_FILE = "database/PresetDatabase.csv";


    private static final WorkDatabase workDatabase = new WorkDatabase();

    private static IDialDao iDialDao;             // 멤버변수 선언
    private static ICategoryDao iCategoryDao;
    private static IPresetDao iPresetDao;
    private static boolean ok = false;

    private WorkDatabase() {
    }

    public static WorkDatabase getInstance() {
        return workDatabase;
    }

    public void ready(Context context) {
        PlanDatabase database = Room.databaseBuilder(context, PlanDatabase.class, "PlanDial")
                .fallbackToDestructiveMigration()   // 스키마 (database) 버전 변경가능
                .allowMainThreadQueries()           // Main Thread에서 DB에 IO(입출력) 가능
                .build();


        iDialDao = database.iDialDao(); //인터페이스 객체 할당
        iCategoryDao = database.iCategoryDao(); //인터페이스 객체 할당
        iPresetDao = database.iPresetDao(); // 인터페이스 객체 할당

        iPresetDao.delPresetAll();

        ok = true;
    }

    // PresetTable에 preset 데이터 채우는 함수 =================================================================
    public boolean fillPresetdatas(Context context) {
        try {
            InputStream inputStream = context.getAssets().open(PresetDatabase_FILE);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            CSVReader read = new CSVReader(reader);

            String[] record = null;
            ArrayList<String> tmp;
            int i = 0;
            while ((record = read.readNext()) != null) {
                PresetTable presetTable = new PresetTable(record[1], record[2], record[3], Integer.parseInt(record[4]), record[5]);
                iPresetDao.insertPreset(presetTable);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    // == Database method ==
    // [index]
    // -- 생성 파트 --
    // -- 수정 파트 --
    // -- 삭제 파트 --
    // -- 조회 파트 --


    // -- 생성 파트 ----------------------------------------------------------------------------------

    // 다이얼 데이터 생성
    public void makeDial(int categoryLocation, String dialName, String dialTimeUnit, int dialTime, String dialIcon, int dialStart) {

        assert ok;
        List<CategoryTable> categoryId = iCategoryDao.getCategoryAll();
        DialTable dialTable = new DialTable(dialName, dialTimeUnit, dialTime,
                categoryId.get(categoryLocation).getId(), // categoryLocation = 메인다이얼의 List의 index 번호
                dialIcon, false, dialStart);
        iDialDao.insertDial(dialTable);
    }

    // 카테고리 데이터 생성
    public void makeCategory(String categoryName, String categoryToTemplate, String categoryColor) {

        assert ok;
        CategoryTable categoryTable = new CategoryTable(categoryName, categoryToTemplate, categoryColor);
        iCategoryDao.insertCategory(categoryTable);
    }

    // 프리셋 데이터 생성
    public void makePreset(String presetName, String templateId, String presetTimeUnit, int presetTime, String presetIcon) {

        assert ok;
        PresetTable presetTable = new PresetTable(presetName, templateId, presetTimeUnit, presetTime, presetIcon);
        iPresetDao.insertPreset(presetTable);
    }


    // -- 수정 파트 ----------------------------------------------------------------------------------

    // 다이얼 데이터 수정
    public void fixDial(int categoryLocation, String dialName, String dialTimeUnit, int dialTime, String dialIcon, int dialStart) {

        assert ok;
        List<CategoryTable> categoryId = iCategoryDao.getCategoryAll();
        DialTable dialTable = new DialTable(dialName, dialTimeUnit, dialTime,
                categoryId.get(categoryLocation).getId(), // categoryLocation = 메인다이얼의 List의 index 번호
                dialIcon, false, dialStart);
        iDialDao.updateDial(dialTable);
    }

    // 카테고리 데이터 수정
    public void fixCategory(String categoryName, String categoryToTemplate, String categoryColor) {

        assert ok;
        CategoryTable categoryTable = new CategoryTable(categoryName, categoryToTemplate, categoryColor);
        iCategoryDao.updateCategory(categoryTable);
    }

    // 프리셋 데이터 수정
    public void fixPreset(String presetName, String templateId, String presetTimeUnit, int presetTime, String presetIcon) {

        assert ok;
        PresetTable presetTable = new PresetTable(presetName, templateId, presetTimeUnit, presetTime, presetIcon);
        iPresetDao.updatePreset(presetTable);
    }


    // -- 삭제 파트 ----------------------------------------------------------------------------------

    // 다이얼 데이터 삭제
    public void delDial(int categoryLocation, String dialName, String dialTimeUnit, int dialTime, String dialIcon, int dialStart) {

        assert ok;
        List<CategoryTable> categoryId = iCategoryDao.getCategoryAll();
        DialTable dialTable = new DialTable(dialName, dialTimeUnit, dialTime,
                categoryId.get(categoryLocation).getId(), // categoryLocation = 메인다이얼의 List의 index 번호
                dialIcon, false, dialStart);
        iDialDao.deleteDial(dialTable);
    }

    // 카테고리 데이터 삭제
    public void delCategory(int categoryLocaton, String categoryName, String categoryToTemplate, String categoryColor) {

        assert ok;
        CategoryTable categoryTable1 = new CategoryTable(categoryName, categoryToTemplate, categoryColor);
        iCategoryDao.deleteCategory(categoryTable1);
        // 카테고리 삭제시 해당 카테고리에 있는 다이얼도 삭제
        List<DialTable> dialTables = iDialDao.getDialAll();
        List<CategoryTable> categoryId = iCategoryDao.getCategoryAll();
        // 이부분 쿼리로 정리할 수 있으면 하기
        for (int i = 0; i < dialTables.size(); i++) {
            if (dialTables.get(i).getDialToCategory() == categoryId.get(categoryLocaton).getId()) { // categoryLocation = 메인다이얼의 List의 index 번호
                DialTable dialTable = new DialTable(dialTables.get(i).getDialName(),
                        dialTables.get(i).getDialTimeUnit(), dialTables.get(i).getDialTime(),
                        dialTables.get(i).getDialToCategory(), dialTables.get(i).getDialIcon(),
                        dialTables.get(i).getDialDisabled(), dialTables.get(i).getDialStart());
                iDialDao.deleteDial(dialTable);

            }
        }
    }

    // 프리셋 데이터 삭제
    public void delPreset(String presetName, String templateId, String presetTimeUnit, int presetTime, String presetIcon) {

        assert ok;
        PresetTable presetTable = new PresetTable(presetName, templateId, presetTimeUnit, presetTime, presetIcon);
        iPresetDao.deletePreset(presetTable);
    }


    // -- 조회 파트 ----------------------------------------------------------------------------------
    // *조회 파트는 함수화 시키는 것보다 직접 db를 활용하는것이 더 효율적일 것 같아 가이드만 적어놓겠습니다.

    // 전체 다이얼 데이터 조회 (step 1 & 2는 WorkDatabase class의 상단에 있는 코드를 붙여넣기 하면 됩니다. )
    // step 1 - 멤버변수를 선언한다.
    // Step 2 - 인스턴스를 객체를 할당한다.
    // step 3 - List<DialTable> dialTables = iDialDao.getDialAll(); 이 코드를 작성한다.
    // 다이얼 정보를 원하면 step3의 코드를 붙여넣으면 되고 카테고리 정보를 원하면 DialTable -> CategoryTable로 변경
    // step 4 - 원하는 정보를 가져오는 코드 EX) 다이얼 이름을 불러온다 -> dialTables.get(i).getId()
    // 여기서 i는 DialTable에 있는 데이터들의 순서입니다. i = 1이면 첫번째 데이터라는 뜻.
    // 필요한 기능은 Dao 파일에서 query문 확인하시면 되고 필요한 기능이 없으면 문의주세요!! 담당자 : 평화


    // 전체 다이얼들의 데이터 조회
//    List<DialTable> dialTables = iDialDao.getDialAll();
//        for(int i = 0; i<dialTables.size();i++) {
//        Log.d("TEST", dialTables.get(i).getId() + "\n"
//                + dialTables.get(i).getDialName() + "\n"
//                + dialTables.get(i).getDialTimeUnit() + "\n"
//                + dialTables.get(i).getDialTime() + "\n"
//                + dialTables.get(i).getDialToCategory() + "\n"
//                + dialTables.get(i).getDialIcon() + "\n"
//                + dialTables.get(i).getDialDisabled() + "\n"
//                + dialTables.get(i).getDialStart() + "\n");
//    }

    // 활성화 되어있는 다이얼들의 데이터 조회
//    List<DialTable> ableDialTables = iDialDao.getDialDis();
//        for(int i = 0; i<ableDialTables.size();i++) {
//        Log.d("TEST", ableDialTables.get(i).getId() + "\n"
//                + ableDialTables.get(i).getDialName() + "\n"
//                + ableDialTables.get(i).getDialTimeUnit() + "\n"
//                + ableDialTables.get(i).getDialTime() + "\n"
//                + ableDialTables.get(i).getDialToCategory() + "\n"
//                + ableDialTables.get(i).getDialIcon() + "\n"
//                + ableDialTables.get(i).getDialStart() + "\n");
//    }


    // 프리셋  선택 부분 추가해서 개선해야함.
    // 프리셋 table -> 카테고리 table 이동 코드
    // 프리셋 talbe -> 다이얼 table 이동 코드
    // 어떤 프리셋인지 구별하는 변수는 t라고 한다.
//    int t = 31;
//    List<PresetTable> presetTables = iPresetDao.getPresetAll();
//    CategoryTable categoryTable2 = new CategoryTable(presetTables.get(t).getPresetName(),
//            presetTables.get(t).getTemplateId(), "FFFFFF");
//        iCategoryDao.insertCategory(categoryTable2);
//
//        switch(t)
//
//    {
//        case 31:
//            List<PresetTable> presetTables1 = iPresetDao.getPreset1();
//            for (int i = 0; i < presetTables1
//                    .size(); i++) {
//                DialTable dialTable1 = new DialTable(presetTables1.get(i).getPresetName(),
//                        presetTables1.get(i).getPresetTimeUnit(), presetTables1.get(i).getPresetTime(),
//                        presetTables1.get(i).getId(), presetTables1.get(i).getPresetIcon(),
//                        false, 1234);
//                iDialDao.insertDial(dialTable1);
//
//            }
//            break;
//        case 32:
//            List<PresetTable> presetTables2 = iPresetDao.getPreset2();
//            for (int i = 0; i < presetTables2.size(); i++) {
//                DialTable dialTable1 = new DialTable(presetTables2.get(i).getPresetName(),
//                        presetTables2.get(i).getPresetTimeUnit(), presetTables2.get(i).getPresetTime(),
//                        presetTables2.get(i).getId(), presetTables2.get(i).getPresetIcon(),
//                        false, 1234);
//                iDialDao.insertDial(dialTable1);
//            }
//            break;
//        case 33:
//            List<PresetTable> presetTables3 = iPresetDao.getPreset3();
//            for (int i = 0; i < presetTables3.size(); i++) {
//                DialTable dialTable1 = new DialTable(presetTables3.get(i).getPresetName(),
//                        presetTables3.get(i).getPresetTimeUnit(), presetTables3.get(i).getPresetTime(),
//                        presetTables3.get(i).getId(), presetTables3.get(i).getPresetIcon(),
//                        false, 1234);
//                iDialDao.insertDial(dialTable1);
//            }
//            break;
//        case 34:
//            List<PresetTable> presetTables4 = iPresetDao.getPreset4();
//            for (int i = 0; i < presetTables4.size(); i++) {
//                DialTable dialTable1 = new DialTable(presetTables4.get(i).getPresetName(),
//                        presetTables4.get(i).getPresetTimeUnit(), presetTables4.get(i).getPresetTime(),
//                        presetTables4.get(i).getId(), presetTables4.get(i).getPresetIcon(),
//                        false, 1234);
//                iDialDao.insertDial(dialTable1);
//            }
//            break;
//        case 35:
//            List<PresetTable> presetTables5 = iPresetDao.getPreset5();
//            for (int i = 0; i < presetTables5.size(); i++) {
//                DialTable dialTable1 = new DialTable(presetTables5.get(i).getPresetName(),
//                        presetTables5.get(i).getPresetTimeUnit(), presetTables5.get(i).getPresetTime(),
//                        presetTables5.get(i).getId(), presetTables5.get(i).getPresetIcon(),
//                        false, 1234);
//                iDialDao.insertDial(dialTable1);
//            }
//            break;
//        default:
//            break;
//    }
}


