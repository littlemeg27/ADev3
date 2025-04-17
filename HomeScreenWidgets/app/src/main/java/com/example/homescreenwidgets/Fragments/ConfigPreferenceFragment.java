package com.example.homescreenwidgets.Fragments;


import static androidx.core.content.ContentProviderCompat.requireContext;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.homescreenwidgets.Operations.WeatherWidgetProvider;

public class ConfigPreferenceFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        // Handle theme and city changes
        ListPreference themePreference = findPreference("theme");
        ListPreference cityPreference = findPreference("city");

        if (themePreference != null && cityPreference != null) {
            themePreference.setOnPreferenceChangeListener((preference, newValue) -> {
                savePreferences((String) newValue, cityPreference.getValue());
                return true;
            });
            cityPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                savePreferences(themePreference.getValue(), (String) newValue);
                return true;
            });
        }
    }

    private void savePreferences(String theme, String city) {
        SharedPreferences prefs = requireActivity().getSharedPreferences("WidgetPrefs", requireActivity().MODE_PRIVATE);
        int appWidgetId = prefs.getInt("appWidgetId", AppWidgetManager.INVALID_APPWIDGET_ID);

        if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
            prefs.edit()
                    .putString("theme_" + appWidgetId, theme)
                    .putString("city_" + appWidgetId, city)
                    .apply();

            // Start service to fetch weather data
            Intent serviceIntent = new Intent(requireContext(), WeatherService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            WeatherService.enqueueWork(requireContext(), serviceIntent);

            // Update widget
            Intent updateIntent = new Intent(requireContext(), WeatherWidgetProvider.class);
            updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{appWidgetId});
            requireContext().sendBroadcast(updateIntent);

            // Return result
            Intent result = new Intent();
            result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            requireActivity().setResult(requireActivity().RESULT_OK, result);
            requireActivity().finish();
        }
    }
}