package com.example.mapsdevicefeatures.Operations;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {PhotoDataEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase
{
    public abstract PhotoDao photoDao();
}
