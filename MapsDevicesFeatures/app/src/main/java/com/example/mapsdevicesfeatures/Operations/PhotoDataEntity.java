package com.example.mapsdevicesfeatures.Operations;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Brenna Pavlinchak
// AD3 - C202504
// PhotoDataEntity

@Entity(tableName = "photos")
public class PhotoDataEntity
{
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String photoUri;
    public String note;
    public double latitude;
    public double longitude;

    public PhotoDataEntity(String photoUri, String note, double latitude, double longitude)
    {
        this.photoUri = photoUri;
        this.note = note;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}