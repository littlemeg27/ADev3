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

import com.example.collectionwidgets.R;
import com.example.collectionwidgets.Operations.StackWidgetProvider;
import com.example.collectionwidgets.Model.ImageData;

import java.util.ArrayList;
import java.util.List;

// Brenna Pavlinchak
// AD3 - C202504
// StackRemoteViewsFactory

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory
{
    private Context context;
    private int appWidgetId;
    private List<ImageData> imageList = new ArrayList<>();

    public StackRemoteViewsFactory(Context context, Intent intent)
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
    public int getCount()
    {
        return imageList.size();
    }

    @Override
    public RemoteViews getViewAt(int position)
    {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_stack_item);
        ImageData image = imageList.get(position);
        views.setImageViewUri(R.id.widget_image, image.getUri());

        Intent fillInIntent = new Intent();
        fillInIntent.setData(image.getUri());
        fillInIntent.setAction(StackWidgetProvider.ACTION_VIEW_IMAGE);
        views.setOnClickFillInIntent(R.id.widget_image, fillInIntent);

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
    public boolean hasStableIds() {
        return true;
    }
}