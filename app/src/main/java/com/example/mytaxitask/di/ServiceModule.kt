package com.example.mytaxitask.di

import android.content.Context
import com.example.mytaxitask.service.LocationService
import com.example.mytaxitask.service.impl.LocationServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @[Provides Singleton]
    fun getService(@ApplicationContext context: Context): LocationService {
        return LocationServiceImpl(context)
    }

}