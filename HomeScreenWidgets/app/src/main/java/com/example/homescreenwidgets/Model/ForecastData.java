package com.example.homescreenwidgets.Model;

public class ForecastData {
    private String day;
    private String icon;
    private int highTemp;
    private int lowTemp;

    public ForecastData(String day, String icon, int highTemp, int lowTemp) {
        this.day = day;
        this.icon = icon;
        this.highTemp = highTemp;
        this.lowTemp = lowTemp;
    }

    public String getDay() {
        return day;
    }

    public String getIcon() {
        return icon;
    }

    public int getHighTemp() {
        return highTemp;
    }

    public int getLowTemp() {
        return lowTemp;
    }
}
