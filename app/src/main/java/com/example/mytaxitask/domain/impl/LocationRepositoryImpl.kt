package com.example.mytaxitask.domain.impl

import com.example.mytaxitask.domain.LocationRepository
import com.example.mytaxitask.service.LocationService
import com.mapbox.mapboxsdk.geometry.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepositoryImpl @Inject constructor(
    private val locationService: LocationService
) :LocationRepository{

    private val _locationFlow = MutableStateFlow<LatLng?>(null)
    override val locationFlow: StateFlow<LatLng?>
        get() = _locationFlow

    init {
        fetchLocation()
    }

    private fun fetchLocation() {
        CoroutineScope(Dispatchers.IO).launch {
            locationService.getLatLng().collect { latLng ->
                _locationFlow.value = latLng
            }
        }
    }
}
