package com.example.plandial;

import java.util.ArrayList;

public class TemplateManager {
    private static final TemplateManager templateManager = new TemplateManager();
    private static final ArrayList<Template> templates = new ArrayList<>();

    private TemplateManager() {
    }

    public static TemplateManager getInstance() {
        return templateManager;
    }

    public int getTemplateCount() {
        return templates.size();
    }

    public ArrayList<Template> getAllTemplates() {
        return templates;
    }

    public Template getTemplateByIndex(int index) {
        try {
            return templates.get(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public Template getTemplateByName(String name) {
        /* 인자로 들어온 문자열과 같은 이름의 카테고리들 중 인덱스가 가장 작은 카테고리를 반환한다.
         * 인자로 들어온 문자열과 같은 이름의 카테고리가 없다면 null을 반환한다.
         */

        for (Template template : templates) {
            if (template.getName().equals(name)) {
                return template;
            }
        }

        return null;
    }

    public void addTemplate(Template template) {
        templates.add(template);
    }
}
