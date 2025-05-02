package com.example.collectionwidgets.Service;

import android.appwidget.AppWidgetManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.collectionwidgets.Operations.FlipperWidgetProvider;
import com.example.collectionwidgets.R;
import com.example.collectionwidgets.Model.ImageData;

import java.util.ArrayList;
import java.util.List;

// Brenna Pavlinchak
// AD3 - C202504
// FlipperRemoteViewsFactory

public class FlipperRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory
{
    private final Context context;
    private final int appWidgetId;
    private final List<ImageData> imageList = new ArrayList<>();

    public FlipperRemoteViewsFactory(Context context, Intent intent)
    {
        this.context = context;
        this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate()
    {
        loadImages();
    }

    @Override
    public void onDataSetChanged()
    {
        loadImages();
    }

    private void loadImages()
    {
        imageList.clear();
        ContentResolver resolver = context.getContentResolver();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME};
        Cursor cursor = resolver.query(uri, projection, null, null, null);

        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                Uri imageUri = Uri.withAppendedPath(uri, String.valueOf(id));
                imageList.add(new ImageData(imageUri, name));
            }
            cursor.close();
        }

    }

    @Override
    public void onDestroy()
    {
        imageList.clear();
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public RemoteViews getViewAt(int position)
    {
        if (position >= imageList.size())
        {
            return null;
        }

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_flipper_item);
        ImageData image = imageList.get(position);
        views.setImageViewUri(R.id.image_view, image.getUri());

        Intent fillInIntent = new Intent();
        fillInIntent.setData(image.getUri());
        fillInIntent.setAction(FlipperWidgetProvider.ACTION_VIEW_IMAGE);
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