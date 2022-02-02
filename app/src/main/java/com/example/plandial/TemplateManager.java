package com.example.plandial;

public class TemplateManager {
    private static final TemplateManager templateManager = new TemplateManager();

    private TemplateManager() {
    }

    public static TemplateManager getInstance() {
        return TemplateManager.templateManager;
    }

    public Dial getPresetByIndex(int index) {
        // index번째 프리셋 가져오기
        return null;
    }

    public Dial getPresetByName(String name) {
        // 이름이 name인 프리셋 가져오기
        return null;
    }

    public Category getTemplateByIndex(int index) {
        // index번째 템플릿 가져오기
        return null;
    }

    public Category getTemplateByName(String name) {
        // 이름이 name인 템플릿 가져오기
        return null;
    }
}
