package com.example.mapsdevicesfeatures.Operations;

import androidx.room.Database;
import androidx.room.RoomDatabase;

// Brenna Pavlinchak
// AD3 - C202504
// AppDatabase

@Database(entities = {PhotoDataEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase
{
    public abstract PhotoDao photoDao();

    public static AppDatabase getDatabase(android.content.Context context)
    {
        return androidx.room.Room.databaseBuilder(context,
                        AppDatabase.class, "photo-database").build();
    }
}