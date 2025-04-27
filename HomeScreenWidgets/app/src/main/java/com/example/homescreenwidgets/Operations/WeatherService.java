package com.example.homescreenwidgets.Operations;

import android.content.Context;
import android.content.Intent;
import androidx.core.app.JobIntentService;
import com.example.homescreenwidgets.Model.WeatherData;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// Brenna Pavlinchak
// AD3 - C202504
// WeatherService

public class WeatherService extends JobIntentService
{
    private static final int JOB_ID = 1000;

    public static void enqueueWork(Context context, Intent work)
    {
        enqueueWork(context, WeatherService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(Intent intent)
    {
        String city = getSharedPreferences("WidgetPrefs", MODE_PRIVATE)
                .getString("city_" + intent.getIntExtra("appWidgetId", 0), "new_york");
        WeatherData data = fetchWeatherData(city);

        try
        {
            FileOutputStream fos = openFileOutput("weather_data", MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
            oos.close();
            fos.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Intent updateIntent = new Intent(this, WeatherWidgetProvider.class);
        updateIntent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        updateIntent.putExtra("appWidgetIds", new int[]{intent.getIntExtra("appWidgetId", 0)});
        sendBroadcast(updateIntent);
    }

    private WeatherData fetchWeatherData(String city)
    {
        String conditions = "Sunny";
        int temperature = 25;
        String icon = "sunny";
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
        return new WeatherData(city, conditions, temperature, timestamp, icon);
    }
}