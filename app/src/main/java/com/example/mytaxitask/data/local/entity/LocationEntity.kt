package com.example.mytaxitask.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LocationEntity")
data class LocationEntity(
    @PrimaryKey val id: Int,
    var lat: String,
    var lon: String,
)