package com.example.mapsdevicesfeatures.Operations;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {com.example.mapsdevicesfeatures.Operations.PhotoDataEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase
{
    public abstract com.example.mapsdevicesfeatures.Operations.PhotoDao photoDao();
}
