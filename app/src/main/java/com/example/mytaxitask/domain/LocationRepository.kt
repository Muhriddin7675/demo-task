package com.example.mytaxitask.domain

import com.mapbox.mapboxsdk.geometry.LatLng
import kotlinx.coroutines.flow.StateFlow

interface LocationRepository {
    val locationFlow: StateFlow<LatLng?>
}