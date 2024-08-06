package com.example.mytaxitask.di

import com.example.mytaxitask.domain.AppRepository
import com.example.mytaxitask.domain.LocationRepository
import com.example.mytaxitask.domain.impl.AppRepositoryImpl
import com.example.mytaxitask.domain.impl.LocationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AppRepositoryModule {

    @Binds
    fun getRepository(impl: AppRepositoryImpl): AppRepository

    @Binds
    fun getLocationRepository(impl: LocationRepositoryImpl): LocationRepository

}