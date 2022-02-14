package com.example.plandial.db;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.room.Room;

import com.example.plandial.AlertDial;
import com.example.plandial.Category;
import com.example.plandial.Dial;
import com.example.plandial.DialManager;
import com.example.plandial.Period;
import com.example.plandial.Preset;
import com.example.plandial.Template;
import com.example.plandial.TemplateManager;
import com.example.plandial.UnitOfTime;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class WorkDatabase {
    private static final String PresetDatabase_FILE = "database/PresetDatabase.csv";
    private static final String PresetData_FILE = "datas/PresetData.csv";
    private static final String TEMPLATE = "T";
    private static final String PRESET = "P";

    private static final WorkDatabase workDatabase = new WorkDatabase();

    private static IDialDao iDialDao;
    private static ICategoryDao iCategoryDao;
    private static IPresetDao iPresetDao;
    private static boolean ok = false;

    private static final DialManager dialManager = DialManager.getInstance();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
    private final static ArrayList<String> unitNames = UnitOfTime.unitEnglishNames;

    private WorkDatabase() {
    }

    public static WorkDatabase getInstance() {
        return workDatabase;
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    public boolean ready(Context context) {
        try {
            PlanDatabase database = Room.databaseBuilder(context, PlanDatabase.class, "PlanDial")
                    .fallbackToDestructiveMigration()   // 스키마 (database) 버전 변경가능
                    .allowMainThreadQueries()           // Main Thread에서 DB에 IO(입출력) 가능
                    .build();

            iDialDao = database.iDialDao();
            iCategoryDao = database.iCategoryDao();
            iPresetDao = database.iPresetDao();

            iPresetDao.delPresetAll(); // 프리셋 테이블 초기화

            // 데이터 불러오기
            HashMap<Integer, Category> idToCategory = new HashMap<>();

            for (CategoryTable categoryTable : iCategoryDao.getCategoryAll()) {
                idToCategory.put(categoryTable.getId(), new Category(categoryTable.getCategoryName()));
            }

            for (DialTable dialTable : iDialDao.getDialAll()) {
                int id = dialTable.getId();
                Period period = new Period(Objects.requireNonNull(UnitOfTime.EnglishNameToUnit.get(dialTable.getDialTimeUnit())), dialTable.getDialTime());
                OffsetDateTime startDateTime = OffsetDateTime.of(LocalDateTime.parse(dialTable.getDialStart(), formatter), OffsetDateTime.now().getOffset());

                AlertDial dial = new AlertDial(context, id, dialTable.getDialName(), period, startDateTime, Integer.parseInt(dialTable.getDialIcon()));
                if (dialTable.getDialDisabled()) dial.disable(context);
                Objects.requireNonNull(idToCategory.get(dialTable.getDialToCategory())).addDial(dial);
            }

            for (Category category : idToCategory.values()) {
                dialManager.addCategory(category);
            }

            ok = fillPresets(context);
        } catch (NullPointerException e) {
            Log.e("Error", getErrorMessage("NullPointerException", "Loading", "Data"));
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("Error", getErrorMessage("UnknownException", "Loading", "Data"));
            e.printStackTrace();
        }

        return ok;
    }

    // [사용X] PresetTable에 preset 데이터 채우는 함수 =================================================================
    public boolean fillPresetdatas(Context context) {
        try {
            InputStream inputStream = context.getAssets().open(PresetDatabase_FILE);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            CSVReader read = new CSVReader(reader);

            String[] record = null;
            while ((record = read.readNext()) != null) {
                PresetTable presetTable = new PresetTable(record[1], record[2], record[3], Integer.parseInt(record[4]), record[5]);
                iPresetDao.insertPreset(presetTable);
            }
        } catch (IOException e) {
            Log.e("Error", getErrorMessage("IOException", "Filling", "Presets"));
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // TemplateManager에 preset 데이터 채우는 함수 (DB 안씀) =================================================================
    public boolean fillPresets(Context context) {
        try {
            InputStream inputStream = context.getAssets().open(PresetData_FILE);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            CSVReader read = new CSVReader(reader);

            HashMap<String, Template> idToTemplate = new HashMap<>();
            String[] record = read.readNext(); // 첫줄 무시

            while ((record = read.readNext()) != null) {
                if (record[0].equals(TEMPLATE)) {
                    idToTemplate.put(record[2], new Template(record[1], record[6]));
                } else if (record[0].equals(PRESET)) {
                    int presetIcon = context.getResources().getIdentifier(record[5], "drawable", context.getPackageName());
                    Period presetPeriod = new Period(Objects.requireNonNull(UnitOfTime.EnglishNameToUnit.get(record[3])), Integer.parseInt(record[4]));
                    Preset preset = new Preset(record[1], presetIcon, presetPeriod, record[6]);
                    Objects.requireNonNull(idToTemplate.get(record[2])).addDial(preset);
                }
            }

            for (Template template : idToTemplate.values()) {
                TemplateManager.getInstance().addTemplate(template);
            }
        } catch (IOException e) {
            Log.e("Error", getErrorMessage("IOException", "Loading", "Presets"));
            e.printStackTrace();
            return false;
        }
        return true;
    }


    // == Database method [index] ==
    // -- 생성 파트 --
    // -- 수정 파트 --
    // -- 삭제 파트 --
    // -- 조회 파트 & anything --


    // -- 생성 파트 ----------------------------------------------------------------------------------

    // 다이얼 데이터 생성
    public boolean makeDial(Category category, AlertDial dial) {
        if (!ok) return false;
        try {
            List<CategoryTable> categoryId = iCategoryDao.getCategoryAll();

            OffsetDateTime startDateTime = dial.getStartDateTime();
            String unitName = unitNames.get((Integer) UnitOfTime.unitToIndex.get(dial.getPeriod().getUnit()));

            DialTable dialTable = new DialTable(dial.getName(), unitName, dial.getPeriod().getTimes(),
                    categoryId.get(dialManager.getIndexByCategory(category)).getId(), // categoryLocation = 메인다이얼의 List의 index 번호
                    String.valueOf(dial.getIcon()), dial.isDisabled(), formatter.format(startDateTime));
            iDialDao.insertDial(dialTable);
        } catch (NullPointerException e) {
            Log.e("Error", getErrorMessage("NullPointerException", "Making", "Dial"));
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // 카테고리 데이터 생성 (카테고리에 저장된 다이얼은 무시됨)
    public boolean makeCategory(Category category) {
        if (!ok) return false;
        CategoryTable categoryTable = new CategoryTable(category.getName(), "", ""); // 수정 필요
        iCategoryDao.insertCategory(categoryTable);
        return true;
    }

    // 카테고리 데이터 생성 (카테고리에 저장된 다이얼 포함)
    // 해당 카테고리에 포함된 다이얼들도 자동으로 저장됨.
    // 중간에 오류 발생시에도 계속 진행 후 false 리턴
    public boolean makeCategoryWithDials(Category category) {
        if (!ok) return false;
        boolean check = this.makeCategory(category);

        for (Dial dial : category.getAllDials()) {
            check &= makeDial(category, (AlertDial) dial);
        }

        return check;
    }

//    // 프리셋 데이터 생성
//    public void makePreset(String presetName, String templateId, String presetTimeUnit, int presetTime, String presetIcon) {
//
//        assert ok;
//        PresetTable presetTable = new PresetTable(presetName, templateId, presetTimeUnit, presetTime, presetIcon);
//        iPresetDao.insertPreset(presetTable);
//    }


    // -- 수정 파트 ----------------------------------------------------------------------------------

    // 다이얼 데이터 수정
    public boolean fixDial(Category category, AlertDial dial, String oldName) {
        if (!ok) return false;
        try {
            List<CategoryTable> categoryId = iCategoryDao.getCategoryAll();

            OffsetDateTime startDateTime = dial.getStartDateTime();
            String unitName = unitNames.get((Integer) UnitOfTime.unitToIndex.get(dial.getPeriod().getUnit()));

            DialTable dialTable = iDialDao.getNameDial(oldName).get(0);
            dialTable.changeDialTable(dial.getName(), unitName, dial.getPeriod().getTimes(),
                    categoryId.get(dialManager.getIndexByCategory(category)).getId(), // categoryLocation = 메인다이얼의 List의 index 번호
                    String.valueOf(dial.getIcon()), dial.isDisabled(), formatter.format(startDateTime));
            iDialDao.updateDial(dialTable);
        } catch (NullPointerException e) {
            Log.e("Error", getErrorMessage("NullPointerException", "Making", "Dial"));
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // 카테고리 데이터 수정 (카테고리에 저장된 다이얼은 무시됨)
    public boolean fixCategory(Category category, String oldName) {
        if (!ok) return false;
        CategoryTable categoryTable = iCategoryDao.getNameCategory(oldName).get(0);
        categoryTable.setCategoryName(category.getName());
        iCategoryDao.updateCategory(categoryTable);
        return true;
    }

//    // 프리셋 데이터 수정
//    public void fixPreset(String presetName, String templateId, String presetTimeUnit, int presetTime, String presetIcon) {
//
//        assert ok;
//        PresetTable presetTable = new PresetTable(presetName, templateId, presetTimeUnit, presetTime, presetIcon);
//        iPresetDao.updatePreset(presetTable);
//    }


    // -- 삭제 파트 ----------------------------------------------------------------------------------

    // 다이얼 데이터 삭제
    public boolean delDial(AlertDial dial) {
        if (!ok) return false;
        DialTable dialTable = iDialDao.getNameDial(dial.getName()).get(0);
        iDialDao.deleteDial(dialTable);
        return true;
    }

    // 카테고리 데이터 삭제 (카테고리에 저장된 다이얼 포함)
    public boolean delCategory(Category category) {
        if (!ok) return false;
        CategoryTable categoryTable1 = iCategoryDao.getNameCategory(category.getName()).get(0);

        // 카테고리 삭제시 해당 카테고리에 있는 다이얼도 삭제
        List<DialTable> dialTables = iDialDao.getDialAll();
        List<CategoryTable> categoryId = iCategoryDao.getCategoryAll();

        // 이부분 쿼리로 정리할 수 있으면 하기
        for (DialTable dialTable : dialTables) {
            // categoryLocation = 메인다이얼의 List의 index 번호
            if (dialTable.getDialToCategory() == categoryTable1.getId()) {
                iDialDao.deleteDial(dialTable);
            }
        }

        iCategoryDao.deleteCategory(categoryTable1);
        return true;
    }

//    // 프리셋 데이터 삭제
//    public void delPreset(String presetName, String templateId, String presetTimeUnit, int presetTime, String presetIcon) {
//
//        assert ok;
//        PresetTable presetTable = new PresetTable(presetName, templateId, presetTimeUnit, presetTime, presetIcon);
//        iPresetDao.deletePreset(presetTable);
//    }


    // -- 조회 파트 & anything -----------------------------------------------------------------------
    // *조회 파트는 함수화 시키는 것보다 직접 db를 활용하는것이 더 효율적일 것 같아 가이드만 적어놓겠습니다.

    // 전체 다이얼 데이터 조회 (step 1 & 2는 WorkDatabase class의 상단에 있는 코드를 붙여넣기 하면 됩니다. )
    // step 1 - 멤버변수를 선언한다.
    // Step 2 - 인스턴스를 객체를 할당한다.
    // step 3 - List<DialTable> dialTables = iDialDao.getDialAll(); 이 코드를 작성한다.
    // 다이얼 정보를 원하면 step3의 코드를 붙여넣으면 되고 카테고리 정보를 원하면 DialTable -> CategoryTable로 변경
    // step 4 - 원하는 정보를 가져오는 코드 EX) 다이얼 이름을 불러온다 -> dialTables.get(i).getId()
    // 여기서 i는 DialTable에 있는 데이터들의 순서입니다. i = 1이면 첫번째 데이터라는 뜻.
    // 필요한 기능은 Dao 파일에서 query문 확인하시면 되고 필요한 기능이 없으면 문의주세요!! 담당자 : 평화


    // 이름으로 데이터 가져오기 (다이얼)
    //List<DialTable> getNameDial(String name);

    // 이름으로 데이터 가져오기 (프리셋)
    //List<PresetTable> getNamePreset(String name);

//    // 다이얼 이름을 받아서 다이얼 개수 조회
//    public int numDial(String name) {
//        List<DialTable> dialTables = iDialDao.getNameDial(name);
//        return dialTables.size();
//    }
//
//    // 템플릿 이름을 받아서 프리셋 개수 조회
//    public int numPreset(String name) {
//        List<PresetTable> presetTables = iPresetDao.getNamePreset(name);
//        return presetTables.size();
//    }


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

    public int getNextDialId() {
        return iDialDao.getLastId() + 1;
    }

    private String getErrorMessage(String exception, String process, String target) {
        return exception + " while " + process + " " + target;
    }
}


