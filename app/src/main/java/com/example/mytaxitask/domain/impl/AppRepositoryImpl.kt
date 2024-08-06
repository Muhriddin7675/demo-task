package com.example.mytaxitask.domain.impl

import com.example.mytaxitask.data.local.dao.LocationDao
import com.example.mytaxitask.data.local.entity.LocationEntity
import com.example.mytaxitask.domain.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepositoryImpl @Inject constructor(
    private val dao: LocationDao
) : AppRepository {
    override suspend fun addLatLong(lat: Double, long: Double) {
        withContext(Dispatchers.IO){
            dao.addLocation(LocationEntity(lat = lat, lon = long, id = 0))
        }
    }


}

