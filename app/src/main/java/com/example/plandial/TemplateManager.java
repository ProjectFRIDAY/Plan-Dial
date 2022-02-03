package com.example.plandial;

public class TemplateManager {
    private static final TemplateManager templateManager = new TemplateManager();

    private TemplateManager() {
    }

    public static TemplateManager getInstance() {
        return TemplateManager.templateManager;
    }

    public int getPresetCount() {
        // 전체 프리셋 개수 가져오기
        return 0;
    }

    public Dial getPresetByIndex(int index) {
        // index번째 프리셋 가져오기
        return null;
    }

    public Dial getPresetByName(String name) {
        // 이름이 name인 프리셋 가져오기
        return null;
    }

    public int getTemplateCount() {
        // 전체 템플릿 개수 가져오기
        return DialManager.getInstance().getCategoryCount();
    }

    public Category getTemplateByIndex(int index) {
        return DialManager.getInstance().getCategoryByIndex(index);
    }

    public Category getTemplateByName(String name) {
        // 이름이 name인 템플릿 가져오기
        return null;
    }
}
