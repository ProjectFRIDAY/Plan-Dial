package com.example.plandial.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PresetTable {

    //Table 속성
    @PrimaryKey(autoGenerate = true)
    private int id = 0; // 각 프리셋의 고유한 ID

    private String presetName; //프리셋의 이름 저장

    private String templateId; //템플릿을 구별하기 위한 ID

    private String presetTimeUnit; //프리셋의 시간단위 저장

    private String presetTime; //프리셋의 기간을 저장

    private String presetIcon; // 아이콘의 아이디를 저장함. (아이디의 자료형은 아직 미정)

    // 인스턴스 생성할 때 값도 같이 입력
    public PresetTable(String presetName, String templateId, String presetTimeUnit, String presetTime, String presetIcon) {
        this.presetName = presetName;
        this.templateId = templateId;
        this.presetTimeUnit = presetTimeUnit;
        this.presetTime = presetTime;
        this.presetIcon = presetIcon;
    }

    //getter & setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPresetName() {
        return presetName;
    }

    public void setPresetName(String presetName) {
        this.presetName = presetName;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getPresetTimeUnit() {
        return presetTimeUnit;
    }

    public void setPresetTimeUnit(String presetTimeUnit) {
        this.presetTimeUnit = presetTimeUnit;
    }

    public String getPresetTime() {
        return presetTime;
    }

    public void setPresetTime(String presetTime) {
        this.presetTime = presetTime;
    }

    public String getPresetIcon() {
        return presetIcon;
    }

    public void setPresetIcon(String presetIcon) {
        this.presetIcon = presetIcon;
    }
}