package com.example.mytaxitask.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LocationEntity")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var lat: Double,
    var lon: Double,
)