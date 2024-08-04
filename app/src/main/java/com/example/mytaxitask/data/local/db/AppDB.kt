package com.example.mytaxitask.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mytaxitask.data.local.dao.LocationDao
import com.example.mytaxitask.data.local.entity.LocationEntity

@Database(entities = [LocationEntity::class], version = 2, exportSchema = false)
abstract class AppDB : RoomDatabase() {
    abstract fun locationDao() : LocationDao

}