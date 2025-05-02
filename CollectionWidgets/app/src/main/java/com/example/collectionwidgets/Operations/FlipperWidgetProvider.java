package com.example.collectionwidgets.Operations;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.RemoteViews;
import com.example.collectionwidgets.R;
import com.example.collectionwidgets.Service.FlipperWidgetService;

// Brenna Pavlinchak
// AD3 - C202504
// FlipperWidgetProvider (Updated with updateAllWidgets)

public class FlipperWidgetProvider extends AppWidgetProvider
{

    public static final String ACTION_VIEW_IMAGE = "com.example.collectionwidgets.FLIPPER_VIEW_IMAGE";
    public static final String ACTION_NEXT = "com.example.collectionwidgets.FLIPPER_NEXT";
    public static final String ACTION_PREV = "com.example.collectionwidgets.FLIPPER_PREV";
    public static final String EXTRA_WIDGET_ID = "widgetId";

    // New static method to update all widgets
    public static void updateAllWidgets(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName provider = new ComponentName(context, FlipperWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(provider);
        for (int widgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, widgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int widgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, widgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        int widgetId = intent.getIntExtra(EXTRA_WIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            return;
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        if (ACTION_NEXT.equals(intent.getAction()) || ACTION_PREV.equals(intent.getAction())) {
            updateWidget(context, appWidgetManager, widgetId);
        } else if (ACTION_VIEW_IMAGE.equals(intent.getAction())) {
            Uri imageUri = intent.getParcelableExtra("imageUri");
            if (imageUri != null) {
                Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                viewIntent.setDataAndType(imageUri, "image/*");
                viewIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(viewIntent);
            }
        }
    }

    private static void updateWidget(Context context, AppWidgetManager appWidgetManager, int widgetId) {
        SharedPreferences prefs = context.getSharedPreferences("WidgetPrefs", Context.MODE_PRIVATE);
        String flipperMode = prefs.getString("flipper_mode_" + widgetId, "auto");

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.flipper_widget_layout);

        Intent serviceIntent = new Intent(context, FlipperWidgetService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
        views.setRemoteAdapter(R.id.flipper, serviceIntent);

        if ("auto".equals(flipperMode)) {
            views.setInt(R.id.flipper, "setFlipInterval", 3000);
            views.setBoolean(R.id.flipper, "setAutoStart", true);
        } else {
            views.setBoolean(R.id.flipper, "setAutoStart", false);
        }

        Intent viewIntent = new Intent(context, FlipperWidgetProvider.class);
        viewIntent.setAction(ACTION_VIEW_IMAGE);
        viewIntent.putExtra(EXTRA_WIDGET_ID, widgetId);
        PendingIntent viewPendingIntent = PendingIntent.getBroadcast(
                context, widgetId, viewIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        views.setPendingIntentTemplate(R.id.flipper, viewPendingIntent);

        Intent nextIntent = new Intent(context, FlipperWidgetProvider.class);
        nextIntent.setAction(ACTION_NEXT);
        nextIntent.putExtra(EXTRA_WIDGET_ID, widgetId);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(
                context, widgetId, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        views.setOnClickPendingIntent(R.id.next_button, nextPendingIntent);

        Intent prevIntent = new Intent(context, FlipperWidgetProvider.class);
        prevIntent.setAction(ACTION_PREV);
        prevIntent.putExtra(EXTRA_WIDGET_ID, widgetId);
        PendingIntent prevPendingIntent = PendingIntent.getBroadcast(
                context, widgetId, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        views.setOnClickPendingIntent(R.id.prev_button, prevPendingIntent);

        appWidgetManager.updateAppWidget(widgetId, views);
        if ("auto".equals(flipperMode)) {
            appWidgetManager.notifyAppWidgetViewDataChanged(widgetId, R.id.flipper);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        SharedPreferences prefs = context.getSharedPreferences("WidgetPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        for (int widgetId : appWidgetIds) {
            editor.remove("flipper_mode_" + widgetId);
        }
        editor.apply();
    }
}