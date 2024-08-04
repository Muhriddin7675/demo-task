package com.example.mytaxitask.domain.impl

import com.example.mytaxitask.data.local.dao.LocationDao
import com.example.mytaxitask.domain.AppRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepositoryImpl @Inject constructor(
    private val dao: LocationDao
) : AppRepository {


}

