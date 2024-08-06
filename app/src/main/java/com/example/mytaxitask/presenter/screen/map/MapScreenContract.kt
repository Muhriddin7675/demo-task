package com.example.mytaxitask.presenter.screen.map

import com.example.mytaxitask.util.Status
import com.mapbox.mapboxsdk.geometry.LatLng
import org.orbitmvi.orbit.ContainerHost

interface MapScreenContract {

    sealed interface MapScreenModel : ContainerHost<UiState, SideEffect> {

        fun onEventDispatcher(intent: Intent)
    }

    data class UiState (
        val scale: Double = 15.0,
        val latLng: LatLng? = null,
        val fullBootSheet: Boolean = false,
        val busyOrActive: Boolean = false,
        val status: Status = Status.Loading
    )

    sealed interface Intent {
        data class ClickButtonScaleNear(
            val scale: Double
        ) : Intent

        data class ClickButtonScaleFar(
            val scale: Double
        ) : Intent

        data class ClickButtonChevronUp(
            val fullBootSheet: Boolean
        ) : Intent

        data class SetLatLong(
            val latLng: LatLng,
            val status: Status,
        ) : Intent

        data class ShowToast(
            val message: String
        ) : Intent

    }

    sealed interface SideEffect {
        data class  ShowToast(val message: String) : SideEffect
    }
}