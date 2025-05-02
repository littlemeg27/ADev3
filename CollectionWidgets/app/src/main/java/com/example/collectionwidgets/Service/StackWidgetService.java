package com.example.collectionwidgets.Service;

import android.content.Intent;

import android.widget.RemoteViewsService;


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