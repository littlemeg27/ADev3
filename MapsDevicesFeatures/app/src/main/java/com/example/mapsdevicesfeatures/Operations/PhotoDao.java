package com.example.mapsdevicefeatures.Operations;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface PhotoDao
{
    @Insert
    void insert(PhotoDataEntity photo);

    @Query("SELECT * FROM photos")
    List<PhotoDataEntity> getAllPhotos();
}
