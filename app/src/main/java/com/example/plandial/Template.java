package com.example.plandial;

import java.util.ArrayList;

public class Template extends Category{
    private int icon;

    public Template(String name, ArrayList<AlertDial> alertDials, int icon) {
        super(name, alertDials);
        this.icon = icon;
    }

    public int getIcon() {
        return icon;
    }
}
