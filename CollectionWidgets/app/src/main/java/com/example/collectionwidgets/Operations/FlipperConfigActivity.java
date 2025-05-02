package com.example.collectionwidgets.Operations;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.collectionwidgets.R;

// Brenna Pavlinchak
// AD3 - C202504
// FlipperConfigActivity

public class FlipperConfigActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flipper_config);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new FlipperConfigFragment())
                .commit();

        setResult(RESULT_CANCELED);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null)
        {
            int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

            if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID)
            {
                getSharedPreferences("WidgetPrefs", MODE_PRIVATE)
                        .edit()
                        .putInt("appWidgetId", appWidgetId)
                        .apply();
            }
        }
    }

    public static class FlipperConfigFragment extends PreferenceFragmentCompat
    {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
        {
            setPreferencesFromResource(R.xml.preferences, rootKey);

            ListPreference flipperMode = findPreference("flipper_mode");

            if (flipperMode != null)
            {
                flipperMode.setOnPreferenceChangeListener((preference, newValue) ->
                {
                    if (newValue instanceof String)
                    {
                        savePreference((String) newValue);
                        return true;
                    }
                    return false;
                });
            }
        }

        private void savePreference(String mode) {
            SharedPreferences prefs = requireActivity().getSharedPreferences("WidgetPrefs", requireActivity().MODE_PRIVATE);
            int appWidgetId = prefs.getInt("appWidgetId", AppWidgetManager.INVALID_APPWIDGET_ID);

            if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID)
            {
                prefs.edit()
                        .putString("flipper_mode_" + appWidgetId, mode)
                        .apply();

                Intent updateIntent = new Intent(requireContext(), FlipperWidgetProvider.class);
                updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{appWidgetId});
                requireContext().sendBroadcast(updateIntent);

                Intent result = new Intent();
                result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                requireActivity().setResult(RESULT_OK, result);
                requireActivity().finish();
            }
        }
    }
}