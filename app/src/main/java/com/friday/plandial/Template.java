package com.friday.plandial;

import java.util.ArrayList;

public class Template extends Category {
    private String description = "";
    private int icon = -1;

    public Template(String name, ArrayList<Dial> dials, String description, int icon) {
        super(name, dials);
        this.description = description;
        this.icon = icon;
    }

    public Template(String name, String description, int icon) {
        // 빈 템플릿
        super(name);
        this.description = description;
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
