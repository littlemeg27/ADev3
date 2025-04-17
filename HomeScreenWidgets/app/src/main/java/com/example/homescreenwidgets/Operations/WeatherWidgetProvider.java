package com.example.homescreenwidgets.Operations;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.homescreenwidgets.R;
import com.example.homescreenwidgets.Model.WeatherData;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class WeatherWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        // Load cached data
        WeatherData weatherData = loadCachedData(context);
        if (weatherData != null) {
            views.setTextViewText(R.id.conditions, context.getString(R.string.weather_conditions, weatherData.getConditions()));
            views.setTextViewText(R.id.temperature, context.getString(R.string.weather_temp, String.valueOf(weatherData.getTemperature())));
            views.setTextViewText(R.id.timestamp, context.getString(R.string.weather_timestamp, weatherData.getTimestamp()));
            views.setImageViewResource(R.id.weather_icon, getIconResource(weatherData.getIcon()));
        }

        // Apply theme
        String theme = context.getSharedPreferences("WidgetPrefs", Context.MODE_PRIVATE)
                .getString("theme_" + appWidgetId, "light");
        if ("dark".equals(theme)) {
            views.setInt(R.id.widget_layout, "setBackgroundColor", context.getResources().getColor(R.color.dark_background));
            views.setTextColor(R.id.conditions, context.getResources().getColor(R.color.dark_text));
            views.setTextColor(R.id.temperature, context.getResources().getColor(R.color.dark_text));
            views.setTextColor(R.id.timestamp, context.getResources().getColor(R.color.dark_text));
        } else {
            views.setInt(R.id.widget_layout, "setBackgroundColor", context.getResources().getColor(R.color.light_background));
            views.setTextColor(R.id.conditions, context.getResources().getColor(R.color.light_text));
            views.setTextColor(R.id.temperature, context.getResources().getColor(R.color.light_text));
            views.setTextColor(R.id.timestamp, context.getResources().getColor(R.color.light_text));
        }

        // Config button intent
        Intent configIntent = new Intent(context, ConfigActivity.class);
        configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent configPendingIntent = PendingIntent.getActivity(context, appWidgetId, configIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.config_button, configPendingIntent);

        // Weather icon intent
        Intent forecastIntent = new Intent(context, ForecastActivity.class);
        forecastIntent.putExtra("city", weatherData != null ? weatherData.getCity() : "");
        PendingIntent forecastPendingIntent = PendingIntent.getActivity(context, appWidgetId, forecastIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.weather_icon, forecastPendingIntent);

        // Start service to fetch new data
        Intent serviceIntent = new Intent(context, WeatherService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        WeatherService.enqueueWork(context, serviceIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private WeatherData loadCachedData(Context context) {
        try {
            FileInputStream fis = context.openFileInput("weather_data");
            ObjectInputStream ois = new ObjectInputStream(fis);
            WeatherData data = (WeatherData) ois.readObject();
            ois.close();
            fis.close();
            return data;
        } catch (Exception e) {
            return null;
        }
    }

    private int getIconResource(String icon) {
        switch (icon) {
            case "cloudy":
                return R.mipmap.ic_cloudy;
            case "rainy":
                return R.mipmap.ic_rainy;
            default:
                return R.mipmap.ic_sunny;
        }
    }
}
