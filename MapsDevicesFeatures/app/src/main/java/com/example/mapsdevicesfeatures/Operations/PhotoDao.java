package com.example.mapsdevicesfeatures.Operations;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface PhotoDao
{
    @Insert
    void insert(com.example.mapsdevicesfeatures.Operations.PhotoDataEntity photo);

    @Query("SELECT * FROM photos")
    List<com.example.mapsdevicesfeatures.Operations.PhotoDataEntity> getAllPhotos();
}
