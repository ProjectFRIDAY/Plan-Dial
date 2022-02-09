package com.example.plandial;

public class Preset extends Dial{
    private String description;

    public Preset(String name, int icon, Period period, String description) {
        super(name, icon, period);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}