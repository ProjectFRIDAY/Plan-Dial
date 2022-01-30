package com.example.plandial.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DialTable {

    //Table 속성
    @PrimaryKey(autoGenerate = true)
    private int id = 0; // 각 다이얼의 고유한 ID

    private String dialName;   // 다이얼 이름 저장

    private String dialTimeUnit; // 다이얼 시간단위 저장

    private int dialTime;  //다이얼의 기간을 저장

    private int dialToCategory; // 카테고리의 아이디를 저장함.

    private String dialIcon;    // 아이콘의 아이디를 저장함.

    private Boolean dialDisabled; // True일 경우 비활성화 False일 경우 활성화

    private int dialStart; // 다이얼 작동 시작시점의 년 월 일 시 분을 저장, 특수문자 없이 일단 숫자로만 저장

    // 인스턴스 생성할 때 값도 같이 입력
    public DialTable(String dialName, String dialTimeUnit, int dialTime, int dialToCategory, String dialIcon, Boolean dialDisabled, int dialStart) {
        this.dialName = dialName;
        this.dialTimeUnit = dialTimeUnit;
        this.dialTime = dialTime;
        this.dialToCategory = dialToCategory;
        this.dialIcon = dialIcon;
        this.dialDisabled = dialDisabled;
        this.dialStart = dialStart;
    }

    //getter & setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDialName() {
        return dialName;
    }

    public void setDialName(String dialName) {
        this.dialName = dialName;
    }

    public String getDialTimeUnit() {
        return dialTimeUnit;
    }

    public void setDialTimeUnit(String dialTimeUnit) {
        this.dialTimeUnit = dialTimeUnit;
    }

    public int getDialTime() {
        return dialTime;
    }

    public void setDialTime(int dialTime) {
        this.dialTime = dialTime;
    }

    public int getDialToCategory() {
        return dialToCategory;
    }

    public void setDialToCategory(int dialToCategory) {
        this.dialToCategory = dialToCategory;
    }

    public String getDialIcon() {
        return dialIcon;
    }

    public void setDialIcon(String dialIcon) {
        this.dialIcon = dialIcon;
    }

    public Boolean getDialDisabled() {
        return dialDisabled;
    }

    public void setDialDisabled(Boolean dialDisabled) {
        this.dialDisabled = dialDisabled;
    }

    public int getDialStart() {
        return dialStart;
    }

    public void setDialStart(int dialStart) {
        this.dialStart = dialStart;
    }
}

