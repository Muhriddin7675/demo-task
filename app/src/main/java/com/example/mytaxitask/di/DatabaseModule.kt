package com.example.mytaxitask.di

import android.content.Context
import androidx.room.Room
import com.example.mytaxitask.data.local.dao.LocationDao
import com.example.mytaxitask.data.local.db.AppDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @[Provides Singleton]
    fun provideDatabase(@ApplicationContext context: Context): AppDB =
        Room.databaseBuilder(context, AppDB::class.java, "LocationDb").build()

    @[Provides Singleton]
    fun provideLocationDao(appDB: AppDB): LocationDao =
        appDB.locationDao()

}