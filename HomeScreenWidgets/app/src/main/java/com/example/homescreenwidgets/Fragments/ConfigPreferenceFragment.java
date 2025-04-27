package com.example.homescreenwidgets.Fragments;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import com.example.homescreenwidgets.Operations.WeatherService;
import com.example.homescreenwidgets.Operations.WeatherWidgetProvider;
import com.example.homescreenwidgets.R;

// Brenna Pavlinchak
// AD3 - C202504
// ConfigPreferenceFragment

public class ConfigPreferenceFragment extends PreferenceFragmentCompat
{
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
    {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        ListPreference themePreference = findPreference("theme");
        ListPreference cityPreference = findPreference("city");

        if (themePreference != null && cityPreference != null) {
            themePreference.setOnPreferenceChangeListener((preference, newValue) ->
            {
                if (newValue instanceof String)
                {
                    savePreferences((String) newValue, cityPreference.getValue());
                    return true;
                }
                return false;
            });
            cityPreference.setOnPreferenceChangeListener((preference, newValue) ->
            {
                if (newValue instanceof String)
                {
                    savePreferences(themePreference.getValue(), (String) newValue);
                    return true;
                }
                return false;
            });
        }
    }

    private void savePreferences(String theme, String city) {
        SharedPreferences prefs = requireActivity().getSharedPreferences("WidgetPrefs", requireActivity().MODE_PRIVATE);
        int appWidgetId = prefs.getInt("appWidgetId", AppWidgetManager.INVALID_APPWIDGET_ID);

        if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID)
        {
            prefs.edit()
                    .putString("theme_" + appWidgetId, theme)
                    .putString("city_" + appWidgetId, city)
                    .apply();

            Intent serviceIntent = new Intent(requireContext(), WeatherService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            WeatherService.enqueueWork(requireContext(), serviceIntent);

            Intent updateIntent = new Intent(requireContext(), WeatherWidgetProvider.class);
            updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{appWidgetId});
            requireContext().sendBroadcast(updateIntent);

            Intent result = new Intent();
            result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            requireActivity().setResult(requireActivity().RESULT_OK, result);
            requireActivity().finish();
        }
    }
}