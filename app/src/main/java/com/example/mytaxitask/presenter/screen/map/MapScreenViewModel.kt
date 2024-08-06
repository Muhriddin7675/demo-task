package com.example.mytaxitask.presenter.screen.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytaxitask.domain.AppRepository
import com.example.mytaxitask.domain.LocationRepository
import com.example.mytaxitask.util.Status
import com.mapbox.mapboxsdk.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MapScreenViewModel @Inject constructor(
    private val appRepository: AppRepository,
    private val locationRepository: LocationRepository
) : ViewModel(), MapScreenContract.MapScreenModel {


    init {

        locationRepository.locationFlow.onEach {
            it?.let { handleLatLng(it) }
        }.launchIn(viewModelScope)

    }

    private fun handleLatLng(latLng: LatLng) {
        onEventDispatcher(MapScreenContract.Intent.SetLatLong(latLng, Status.Success))
        onEventDispatcher(MapScreenContract.Intent.ShowToast("Location is: ${latLng.latitude} ${latLng.longitude}"))
        viewModelScope.launch {
            appRepository.addLatLong(latLng.latitude, latLng.longitude)
        }
    }

    override fun onEventDispatcher(intent: MapScreenContract.Intent) {
        when (intent) {
            is MapScreenContract.Intent.UpdateZoom ->
                intent {
                    reduce {
                        state.copy(zoom = intent.zoom, setHasZoom = intent.setHasZoom)
                    }
                }

            is MapScreenContract.Intent.SetLatLong ->
                intent {
                    reduce {
                        state.copy(latLng = intent.latLng, status = intent.status)
                    }
                }


            is MapScreenContract.Intent.ShowToast ->
                intent {
                    postSideEffect(MapScreenContract.SideEffect.ShowToast(intent.message))
                }


            is MapScreenContract.Intent.SetSheetState -> {
                intent {
                    reduce {
                        state.copy(sheetState = intent.sheetState)
                    }
                }
            }

            is MapScreenContract.Intent.IsLoadingTab -> {
                intent {
                    reduce {
                        state.copy(isLoadingTabButton = intent.isLoading)
                    }
                }
            }

            is MapScreenContract.Intent.UpdateSelectedOptionTab -> {
                intent {
                    viewModelScope.launch {

                        delay(200)
                        onEventDispatcher(MapScreenContract.Intent.IsLoadingTab(false))
                        reduce {
                            state.copy(selectedOptionTab = intent.tapIndex)
                        }
                    }
                }

            }
        }
    }

    override val container =
        container<MapScreenContract.UiState, MapScreenContract.SideEffect>(MapScreenContract.UiState())
}