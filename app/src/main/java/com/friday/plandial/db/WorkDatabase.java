package com.friday.plandial.db;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.room.Room;

import com.friday.plandial.AlertDial;
import com.friday.plandial.Category;
import com.friday.plandial.Dial;
import com.friday.plandial.DialManager;
import com.friday.plandial.Period;
import com.friday.plandial.Preset;
import com.friday.plandial.Template;
import com.friday.plandial.TemplateManager;
import com.friday.plandial.UnitOfTime;
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
    private static final String PresetData_FILE = "datas/PresetData.csv";
    private static final String TEMPLATE = "T";
    private static final String PRESET = "P";

    private static final WorkDatabase workDatabase = new WorkDatabase();

    private static IDialDao iDialDao;
    private static ICategoryDao iCategoryDao;
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
                    int templateIcon = context.getResources().getIdentifier(record[5], "drawable", context.getPackageName());
                    idToTemplate.put(record[2], new Template(record[1], record[6], templateIcon));
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



    public int getNextDialId() {
        return iDialDao.getLastId() + 1;
    }

    private String getErrorMessage(String exception, String process, String target) {
        return exception + " while " + process + " " + target;
    }
}
