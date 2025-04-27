package com.example.collectionwidgets.Model;

import android.net.Uri;

// Brenna Pavlinchak
// AD3 - C202504
// ImageData

public class ImageData
{
    private Uri uri;
    private String displayName;

    public ImageData(Uri uri, String displayName)
    {
        this.uri = uri;
        this.displayName = displayName;
    }

    public Uri getUri()
    {
        return uri;
    }

    public String getDisplayName()
    {
        return displayName;
    }
}