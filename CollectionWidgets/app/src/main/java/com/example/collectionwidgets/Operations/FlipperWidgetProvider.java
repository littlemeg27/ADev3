package com.example.collectionwidgets.Operations;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterViewFlipper;
import android.widget.Button;
import com.example.collectionwidgets.R;
import com.example.collectionwidgets.Service.FlipperWidgetService;

// Brenna Pavlinchak
// AD3 - C202504
// FlipperManager (Non-widget alternative to FlipperWidgetProvider)

public class FlipperManager {

    private Context context;
    private AdapterViewFlipper flipper;
    private Button nextButton;
    private Button prevButton;
    private SharedPreferences prefs;
    private int widgetId; // Simulate widget ID for SharedPreferences

    public static final String ACTION_VIEW_IMAGE = "com.example.collectionwidgets.FLIPPER_VIEW_IMAGE";

    public FlipperManager(Context context, AdapterViewFlipper flipper, Button nextButton, Button prevButton, int widgetId) {
        this.context = context;
        this.flipper = flipper;
        this.nextButton = nextButton;
        this.prevButton = prevButton;
        this.widgetId = widgetId;
        this.prefs = context.getSharedPreferences("WidgetPrefs", Context.MODE_PRIVATE);
        initializeFlipper();
    }

    private void initializeFlipper() {
        // Set adapter using FlipperWidgetService (assumes it provides a RemoteViewsFactory)
        Intent serviceIntent = new Intent(context, FlipperWidgetService.class);
        serviceIntent.putExtra("widgetId", widgetId); // Simulate AppWidgetManager.EXTRA_APPWIDGET_ID
        flipper.setAdapter(new FlipperAdapter(context, serviceIntent));

        // Configure auto/manual advance
        String flipperMode = prefs.getString("flipper_mode_" + widgetId, "auto");
        if ("auto".equals(flipperMode)) {
            flipper.setAutoStart(true);
            flipper.setFlipInterval(3000);
            flipper.startFlipping();
        } else {
            flipper.setAutoStart(false);
            flipper.stopFlipping();
        }

        // Set click listener for image viewing
        flipper.setOnClickListener(v -> {
            Uri imageUri = getCurrentImageUri(); // Implement based on adapter
            if (imageUri != null) {
                Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                viewIntent.setDataAndType(imageUri, "image/*");
                viewIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(viewIntent);
            }
        });

        // Set next/prev button listeners
        nextButton.setOnClickListener(v -> flipper.showNext());
        prevButton.setOnClickListener(v -> flipper.showPrevious());
    }

    public void updateFlipper() {
        String flipperMode = prefs.getString("flipper_mode_" + widgetId, "auto");
        if ("auto".equals(flipperMode)) {
            flipper.setAutoStart(true);
            flipper.setFlipInterval(3000);
            flipper.startFlipping();
        } else {
            flipper.setAutoStart(false);
            flipper.stopFlipping();
        }
    }

    private Uri getCurrentImageUri() {
        // Placeholder: Implement logic to get the current image URI from the adapter
        return null; // Replace with actual implementation
    }

    // Custom adapter to bridge FlipperWidgetService's RemoteViewsFactory
    private static class FlipperAdapter extends android.widget.BaseAdapter {
        private Context context;
        private Intent serviceIntent;

        public FlipperAdapter(Context context, Intent serviceIntent) {
            this.context = context;
            this.serviceIntent = serviceIntent;
            // Initialize adapter data using FlipperWidgetService's logic
        }

        @Override
        public int getCount() {
            return 0; // Implement based on FlipperWidgetService data
        }

        @Override
        public Object getItem(int position) {
            return null; // Implement
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, android.view.ViewGroup parent) {
            // Implement view creation, e.g., ImageView with URI from FlipperWidgetService
            return null; // Replace with actual implementation
        }
    }
}