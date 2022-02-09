package com.example.plandial;

import java.util.ArrayList;

public class Template extends Category {
    private String description = "";

    public Template(String name, ArrayList<Dial> dials, String description) {
        super(name, dials);
        this.description = description;
    }

    public Template(String name, String description) {
        // 빈 템플릿
        super(name);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
