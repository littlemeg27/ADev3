package com.example.collectionwidgets.Service;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.collectionwidgets.R;

// Brenna Pavlinchak
// AD3 - C202504
// StackWidgetService

public class StackWidgetService extends RemoteViewsService
{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent)
    {
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}