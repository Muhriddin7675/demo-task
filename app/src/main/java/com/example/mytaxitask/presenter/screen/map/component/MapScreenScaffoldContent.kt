package com.example.mytaxitask.presenter.screen.map.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.mytaxitask.R
import com.example.mytaxitask.presenter.screen.map.MapScreenContract
import com.example.mytaxitask.ui.component.CustomIconButton
import com.example.mytaxitask.ui.component.CustomTextView
import com.example.mytaxitask.ui.component.DynamicTabSelector
import com.example.mytaxitask.ui.theme.black
import com.example.mytaxitask.ui.theme.errorColor
import com.example.mytaxitask.ui.theme.greenColor
import com.example.mytaxitask.util.playAudio
import com.example.mytaxitask.util.restoreMapView
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreenScaffoldContent(
    mapView: MapView,
    zoom: Double,
    lastLatLng: LatLng,
    sheetState: Boolean,
    scaffoldState: BottomSheetScaffoldState,
    scope: CoroutineScope,
    isLoading: Boolean,
    onEventDispatcher: (MapScreenContract.Intent) -> Unit,
    selectedOption: Int
) {
    var time = System.currentTimeMillis()
    val optionTexts =
        listOf(stringResource(id = R.string.busy), stringResource(id = R.string.active))
    val context = LocalContext.current
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize()) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { mapView },
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                CustomIconButton(
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp, end = 12.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { }
                        .background(MaterialTheme.colorScheme.background)
                        .size(56.dp),
                    icon = R.drawable.ic_menu,
                    iconSize = 24,
                    childBoxColor = MaterialTheme.colorScheme.primaryContainer,
                    iconColor = MaterialTheme.colorScheme.onBackground
                )

                Box(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .height(56.dp)
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    DynamicTabSelector(
                        tabs = optionTexts,
                        tabColorList = listOf(errorColor, greenColor),
                        selectedOption = selectedOption
                    ) {
                        if (time + 500 < System.currentTimeMillis()) {
                            time = System.currentTimeMillis()
                            if (selectedOption != it) {
                                scope.launch {
                                    onEventDispatcher(MapScreenContract.Intent.IsLoadingTab(true))
                                    delay(300)
                                    onEventDispatcher(
                                        MapScreenContract.Intent.UpdateSelectedOptionTab(
                                            it
                                        )
                                    )
                                    if (it == 1) playAudio(context, R.raw.click_tap_button_sound)
                                }
                            }
                        }
                    }
                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    MaterialTheme.colorScheme.background,
                                    RoundedCornerShape(16.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.secondary,
                                strokeWidth = 3.dp,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .padding(top = 16.dp, start = 12.dp, end = 16.dp)
                        .background(
                            MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(14.dp)
                        )
                        .size(56.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxSize()
                            .background(
                                color = greenColor,
                                shape = RoundedCornerShape(10.dp)
                            )
                    ) {
                        CustomTextView(
                            text = "95",
                            color = black,
                            fontSize = 20,
                            fontWeight = 700,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
            ) {
                MapScreenColumnButtons(
                    modifier = Modifier.fillMaxWidth(),
                    onClickButtonScaleNear = {
                        onEventDispatcher(
                            MapScreenContract.Intent.UpdateZoom(zoom = zoom + 1.0, setHasZoom = false)
                        )
                    },
                    onClickButtonScaleFar = {
                        if (zoom >= 3) {
                            onEventDispatcher(
                                MapScreenContract.Intent.UpdateZoom(
                                    zoom = zoom - 1.0,
                                    setHasZoom = false
                                )
                            )
                        }
                    },
                    onClickButtonNavigation = {
                        mapView.getMapAsync { mapBoxMap ->
                            onEventDispatcher(
                                MapScreenContract.Intent.UpdateZoom(
                                    zoom = 15.0,
                                    setHasZoom = true
                                )
                            )
                            restoreMapView(mapboxMap = mapBoxMap, latLong = lastLatLng, zoom = 15.0)
                        }
                    },
                    onClickButtonChevronUp = {
                        onEventDispatcher(MapScreenContract.Intent.SetSheetState(true))
                        scope.launch {
                            scaffoldState.bottomSheetState.expand()
                        }
                    },
                    visibility = !sheetState
                )
            }
        }
    }
}


