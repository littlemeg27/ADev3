package com.example.mapsdevicesfeatures.Operations;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

// Brenna Pavlinchak
// AD3 - C202504
// PhotoDao

@Dao
public interface PhotoDao
{
    @Insert
    void insert(PhotoDataEntity photo);

    @Query("SELECT * FROM photos")
    List<PhotoDataEntity> getAllPhotos();
}