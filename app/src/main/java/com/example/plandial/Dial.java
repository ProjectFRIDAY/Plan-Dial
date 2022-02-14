package com.example.plandial;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.OffsetDateTime;

public class Dial {
    private String name;
    private int icon;
    private Period period;

    public Dial(String name, int icon, Period period) {
        assert name != null;
        assert icon != -1;
        assert period != null;

        this.name = name;
        this.icon = icon;
        this.period = period;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    public AlertDial toAlertDial(Context context) {
        return new AlertDial(context, name, period, OffsetDateTime.now(), icon);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + name.hashCode();
        result = 31 * result + icon;
        result = 31 * result + period.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Dial) || this.hashCode() != obj.hashCode()) {
            return false;
        }

        Dial other = (Dial) obj;
        return this.name.equals(other.name)
                && this.icon == other.icon
                && this.period.equals(other.period);
    }
}