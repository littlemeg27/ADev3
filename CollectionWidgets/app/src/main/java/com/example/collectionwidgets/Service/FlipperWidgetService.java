package com.example.collectionwidgets.Service;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.example.collectionwidgets.R;

public class FlipperWidgetService extends RemoteViewsService
{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent)
    {
        return new FlipperRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    private static class FlipperRemoteViewsFactory implements RemoteViewsFactory
    {
        private final Context context;
        private final int widgetId;
        private Uri[] imageUris;

        public FlipperRemoteViewsFactory(Context context, Intent intent)
        {
            this.context = context;
            this.widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate()
        {
            imageUris = new Uri[]
                    {
                    Uri.parse("android.resource://" + context.getPackageName() + "/drawable/image1"),
                    Uri.parse("android.resource://" + context.getPackageName() + "/drawable/image2"),
                    Uri.parse("android.resource://" + context.getPackageName() + "/drawable/image3")
            };
            // TODO: Replace with actual image loading (e.g., from SharedPreferences, storage, or assets)
        }

        @Override
        public void onDataSetChanged()
        {
            // Refresh image
        }

        @Override
        public void onDestroy()
        {
            imageUris = null;
        }

        @Override
        public int getCount()
        {
            return imageUris != null ? imageUris.length : 0;
        }

        @Override
        public RemoteViews getViewAt(int position)
        {
            if (position >= getCount())
            {
                return null;
            }

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_flipper_item);
            views.setImageViewUri(R.id.image_view, imageUris[position]);

            Intent fillInIntent = new Intent();
            fillInIntent.putExtra("imageUri", imageUris[position]);
            views.setOnClickFillInIntent(R.id.image_view, fillInIntent);

            return views;
        }

        @Override
        public RemoteViews getLoadingView()
        {
            return null;
        }

        @Override
        public int getViewTypeCount()
        {
            return 1;
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public boolean hasStableIds()
        {
            return true;
        }
    }
}