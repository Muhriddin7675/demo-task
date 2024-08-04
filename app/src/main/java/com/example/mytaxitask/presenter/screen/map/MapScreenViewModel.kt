package com.example.mytaxitask.presenter.screen.map

import androidx.lifecycle.ViewModel
import com.example.mytaxitask.domain.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.container

import javax.inject.Inject

@HiltViewModel
class MapScreenViewModel @Inject constructor(
    var repository: AppRepository
) : ViewModel(), MapScreenContract.MapScreenModel {

    override fun onEventDispatcher(intent: MapScreenContract.Intent) {
        when (intent) {
            is MapScreenContract.Intent.ClickBusyOrActive -> intent {
                reduce {
                    MapScreenContract.UiState.LoadUiState(busyOrActive = intent.busyOrActive)
                }
            }

            is MapScreenContract.Intent.ClickButtonChevronUp ->
                intent {
                    reduce {
                        MapScreenContract.UiState.LoadUiState(fullBootSheet = intent.fullBootSheet)
                    }
                }


            is MapScreenContract.Intent.ClickButtonScaleFar ->
                intent {
                    reduce {
                        MapScreenContract.UiState.LoadUiState(scale = intent.scale)
                    }
                }


            is MapScreenContract.Intent.ClickButtonScaleNear ->
                intent {
                    reduce {
                        MapScreenContract.UiState.LoadUiState(scale = intent.scale)
                    }
                }


            is MapScreenContract.Intent.ShowToast -> {

            }
        }
    }

    override val container =
        container<MapScreenContract.UiState, MapScreenContract.SideEffect>(MapScreenContract.UiState.LoadUiState())

}