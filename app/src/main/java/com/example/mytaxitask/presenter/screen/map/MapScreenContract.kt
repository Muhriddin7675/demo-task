package com.example.mytaxitask.presenter.screen.map

import com.example.mytaxitask.util.Status
import com.example.mytaxitask.util.tashkentCenterLatLng
import com.mapbox.mapboxsdk.geometry.LatLng
import org.orbitmvi.orbit.ContainerHost

interface MapScreenContract {

    sealed interface MapScreenModel : ContainerHost<UiState, SideEffect> {

        fun onEventDispatcher(intent: Intent)
    }

    data class UiState(
        val zoom: Double = 8.0,
        val setHasZoom: Boolean = true,
        val latLng: LatLng = tashkentCenterLatLng,
        val sheetState: Boolean = false,
        val busyOrActive: Boolean = false,
        val status: Status = Status.Loading,
        val isLoadingTabButton: Boolean = false,
        val selectedOptionTab: Int = 0,
    )

    sealed interface Intent {
        data class UpdateZoom(
            val setHasZoom: Boolean,
            val zoom: Double
        ) : Intent

        data class SetLatLong(
            val latLng: LatLng,
            val status: Status,
        ) : Intent

        data class ShowToast(
            val message: String
        ) : Intent

        data class SetSheetState(
            val sheetState: Boolean
        ) : Intent

        data class UpdateSelectedOptionTab(
            val tapIndex: Int
        ) : Intent
        data class IsLoadingTab(
            val isLoading: Boolean
        ) : Intent

    }

    sealed interface SideEffect {
        data class ShowToast(val message: String) : SideEffect
    }
}