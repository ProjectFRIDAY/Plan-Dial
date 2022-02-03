package com.example.plandial;

import java.util.ArrayList;

public class Template extends Category{
    private int icon;

    public Template(String name, ArrayList<Dial> dials, int icon) {
        super(name, dials);
        this.icon = icon;
    }

    public int getIcon() {
        return icon;
    }
}
