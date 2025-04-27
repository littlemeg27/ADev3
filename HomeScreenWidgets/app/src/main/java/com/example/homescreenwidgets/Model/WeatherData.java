package com.example.homescreenwidgets.Model;

import java.io.Serializable;

// Brenna Pavlinchak
// AD3 - C202504
// WeatherData

public class WeatherData implements Serializable
{
    private String city;
    private String conditions;
    private int temperature;
    private String timestamp;
    private String icon;

    public WeatherData(String city, String conditions, int temperature, String timestamp, String icon)
    {
        this.city = city;
        this.conditions = conditions;
        this.temperature = temperature;
        this.timestamp = timestamp;
        this.icon = icon;
    }

    public String getCity()
    {
        return city;
    }

    public String getConditions()
    {
        return conditions;
    }

    public int getTemperature()
    {
        return temperature;
    }

    public String getTimestamp()
    {
        return timestamp;
    }

    public String getIcon()
    {
        return icon;
    }
}
