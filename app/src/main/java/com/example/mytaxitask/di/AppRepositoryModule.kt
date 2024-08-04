package com.example.mytaxitask.di

import com.example.mytaxitask.domain.AppRepository
import com.example.mytaxitask.domain.impl.AppRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AppRepositoryModule  {

    @Binds
    fun getRepository(impl: AppRepositoryImpl): AppRepository
}