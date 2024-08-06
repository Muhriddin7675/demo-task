package com.example.mytaxitask.service

import com.mapbox.mapboxsdk.geometry.LatLng
import kotlinx.coroutines.flow.Flow

interface LocationService {
     fun getLatLng(): Flow<LatLng>
}