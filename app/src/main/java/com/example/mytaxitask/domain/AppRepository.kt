package com.example.mytaxitask.domain

interface AppRepository {
    suspend fun addLatLong(lat: Double, long: Double)
}