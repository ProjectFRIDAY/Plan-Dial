package com.example.plandial;

import java.util.ArrayList;

public class Template extends Category{
    private int icon;
    private String description;

    public Template(String name, ArrayList<Dial> dials, int icon, String description) {
        super(name, dials);
        this.icon = icon;
        this.description = description;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
