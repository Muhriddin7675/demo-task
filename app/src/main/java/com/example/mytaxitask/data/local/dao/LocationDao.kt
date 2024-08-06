package com.example.mytaxitask.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.example.mytaxitask.data.local.entity.LocationEntity

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addLocation(card: LocationEntity)

    @Update
    fun updateLocation(card: LocationEntity)

    @Delete
    fun deleteLocation(card: LocationEntity)
}
