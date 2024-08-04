package com.example.mytaxitask.presenter.screen.map

import org.orbitmvi.orbit.ContainerHost

interface MapScreenContract {

    sealed interface MapScreenModel : ContainerHost<UiState, SideEffect> {

        fun onEventDispatcher(intent: Intent)
    }

    sealed interface UiState {

        data class LoadUiState(
            val scale: Double = 15.0,
            val lat: Double = 41.311081,
            val long: Double = 69.279747,
            val fullBootSheet: Boolean = false,
            val busyOrActive: Boolean = false
        ) : UiState

    }

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

        data class ClickBusyOrActive(
            val busyOrActive: Boolean
        ) : Intent

        data class ShowToast(val msg: String) : Intent


    }

    sealed interface SideEffect {
        data class ShowToast(val msg: String) : SideEffect

    }
}