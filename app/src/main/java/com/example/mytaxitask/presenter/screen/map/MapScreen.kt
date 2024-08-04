package com.example.mytaxitask.presenter.screen.map

import CustomButton
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import com.example.mytaxitask.R
import com.example.mytaxitask.presenter.screen.map.MapScreenContract.UiState
import com.example.mytaxitask.ui.component.CustomIconButton
import com.example.mytaxitask.ui.component.CustomTextView
import com.example.mytaxitask.ui.dialog.RowComponentIconText
import com.example.mytaxitask.ui.theme.black
import com.example.mytaxitask.ui.theme.errorColor
import com.example.mytaxitask.ui.theme.greenColor
import com.example.mytaxitask.ui.theme.navigationIconColor
import com.example.mytaxitask.util.getDrawableToBitmap
import com.example.mytaxitask.util.myLog
import com.example.mytaxitask.util.restoreMapView
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState

class MapScreen : Screen {

    @Composable
    override fun Content() {
        val model: MapScreenContract.MapScreenModel = getViewModel<MapScreenViewModel>()
        val uiState = model.collectAsState().value
        MapScreenContent(uiState = uiState, onEventDispatcher = model::onEventDispatcher)
    }
}


@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreenContent(
    uiState: UiState,
    onEventDispatcher: (MapScreenContract.Intent) -> Unit
) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    var zoom by remember { mutableDoubleStateOf(15.toDouble()) }
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    var fullBootSheet by remember { mutableStateOf(false) }
    var busyOrActive by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var lastLatLng by remember { mutableStateOf(LatLng(41.311081, 69.34)) }
    val scaffoldState = rememberBottomSheetScaffoldState()
    val bottomSheetState by remember { mutableStateOf(scaffoldState.bottomSheetState) }

    // LaunchedEffect to observe changes in the bottomSheetState
    LaunchedEffect(bottomSheetState.currentValue) {
        val isExpanded = bottomSheetState.currentValue == SheetValue.Expanded
        onEventDispatcher(MapScreenContract.Intent.ClickButtonChevronUp(isExpanded))
    }

    // Your UI her


    when (uiState) {
        is UiState.LoadUiState -> {
            zoom = uiState.scale
            fullBootSheet = uiState.fullBootSheet
            busyOrActive = uiState.busyOrActive
            lastLatLng = LatLng(uiState.lat, uiState.long)
            myLog("MapScreenContent lat ${uiState.lat}")
            myLog("MapScreenContent long ${uiState.long}")
            myLog("MapScreenContent ${uiState.scale}")
            myLog("MapScreenContent fullBootSheet ${uiState.fullBootSheet}")
            myLog("MapScreenContent busyOrActive ${uiState.busyOrActive}")
        }
    }

    DisposableEffect(zoom) {
        mapView.getMapAsync { mapboxMap ->
            mapboxMap.cameraPosition.let { currentPosition ->
                val cameraPosition = CameraPosition.Builder()
                    .target(currentPosition.target)
                    .zoom(zoom)
                    .build()
                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
        }
        onDispose {}
    }
//    LaunchedEffect(lastLatLng,zoom2) {
//        mapView.getMapAsync { mapboxMap ->
//            mapboxMap.run {
//                val cameraPosition = CameraPosition.Builder()
//                    .target(lastLatLng)
//                    .zoom(zoom2)
//                    .build()
//                animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
//            }
//        }
//    }

    DisposableEffect(lifecycleOwner) {
        val lifecycle = lifecycleOwner.lifecycle
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> {}
            }
        }


        lifecycle.addObserver(lifecycleObserver)

        mapView.getMapAsync { mapboxMap ->
            mapboxMap.setStyle("https://api.maptiler.com/maps/streets/style.json?key=BLPhpLCCcS9XIn3H7CPZ") { style ->
                val bitmap = getDrawableToBitmap(
                    context = context,
                    drawableId = R.drawable.ic_new_car_marker
                )
                val icon = bitmap.let { IconFactory.getInstance(context).fromBitmap(it) }
                val cameraPosition = CameraPosition.Builder()
                    .target(lastLatLng)
                    .zoom(zoom)
                    .build()
                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

                val markerOptions = MarkerOptions()
                    .position(lastLatLng)
                    .title("Bro")
                    .icon(icon)

                mapboxMap.addMarker(markerOptions)

            }
        }

        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
            mapView.onDestroy()
        }
    }


    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContainerColor = MaterialTheme.colorScheme.background.copy(alpha = 0f),
        sheetPeekHeight = 160.dp,
        sheetShadowElevation = 0.dp,
        sheetContent = {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 16.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(horizontal = 16.dp)
                ) {
                    RowComponentIconText(
                        icon = R.drawable.ic_tariff,
                        title = stringResource(id = R.string.tariff),
                        text = "6/8",
                        clickable = {})
                    HorizontalDivider(
                        thickness = 0.7.dp,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    RowComponentIconText(
                        icon = R.drawable.ic_order,
                        title = stringResource(id = R.string.orders),
                        text = "0",
                        clickable = {})
                    HorizontalDivider(
                        thickness = 0.7.dp,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    RowComponentIconText(
                        icon = R.drawable.ic_rocket,
                        title = stringResource(id = R.string.thereIs),
                        clickable = {})

                }
            }


        },
        content = {
            Box(Modifier.fillMaxSize()) {
                AndroidView(factory = { mapView })
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
                    Row(
                        modifier = Modifier
                            .padding(top = 16.dp, start = 0.dp, end = 0.dp)
                            .height(56.dp)
                            .weight(1f)
                            .background(
                                MaterialTheme.colorScheme.background,
                                shape = RoundedCornerShape(14.dp)
                            )
                    ) {
                        CustomButton(
                            text = stringResource(id = R.string.busy),
                            onClick = {
                                onEventDispatcher(
                                    MapScreenContract.Intent.ClickBusyOrActive(
                                        busyOrActive = false
                                    )
                                )
                            },
                            enable = !busyOrActive,
                            contentColor = MaterialTheme.colorScheme.onBackground,
                            enableBackgroundColor = errorColor,
                            textFontSize = 18,
                            backgroundColor = MaterialTheme.colorScheme.background,
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxHeight()
                                .weight(1f)
                        )
                        CustomButton(
                            text = stringResource(id = R.string.active),
                            textFontSize = 18,
                            onClick = {
                                onEventDispatcher(
                                    MapScreenContract.Intent.ClickBusyOrActive(busyOrActive = true)
                                )
                            },
                            contentColor = MaterialTheme.colorScheme.onBackground,
                            enableBackgroundColor = greenColor,
                            enable = busyOrActive,
                            backgroundColor = MaterialTheme.colorScheme.background,
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxHeight()
                                .weight(1f)
                        )
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

                ColumnButtons(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center),
                    clickButtonScaleNear = {
                        if (zoom < 27)
                            onEventDispatcher(
                                MapScreenContract.Intent.ClickButtonScaleNear(
                                    zoom + 1
                                )
                            )
                    },
                    clickButtonScaleFar = {
                        if (zoom >= 1)
                            onEventDispatcher(
                                MapScreenContract.Intent.ClickButtonScaleFar(
                                    scale = zoom - 1
                                )
                            )
                    },
                    clickButtonNavigation = {
                        mapView.getMapAsync { mapBoxMap ->
                            restoreMapView(mapboxMap = mapBoxMap, latLong = lastLatLng)
                        }


                    },
                    clickButtonChevronUp = {
                        scope.launch {
                            scaffoldState.bottomSheetState.expand()
                        }
                        onEventDispatcher(MapScreenContract.Intent.ClickButtonChevronUp(true))
                    },
                    visibility = !fullBootSheet
                )
            }
        }
    )
}

@Composable
fun ColumnButtons(
    modifier: Modifier,
    visibility: Boolean,
    clickButtonScaleNear: () -> Unit,
    clickButtonScaleFar: () -> Unit,
    clickButtonNavigation: () -> Unit,
    clickButtonChevronUp: () -> Unit
) {
    Row(modifier = modifier) {

        AnimatedVisibility(
            modifier = Modifier,
            visible = visibility,
            enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn(),
            exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut(),

            ) {

            CustomIconButton(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { clickButtonChevronUp() }
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f))
                    .size(56.dp),
                icon = R.drawable.ic_chevrons_up,
                iconSize = 24,
                childBox = true
            )

        }
        Spacer(modifier = Modifier.weight(1f))

        AnimatedVisibility(
            modifier = Modifier,
            visible = visibility,
            enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
            exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut(),
        ) {

            Column() {
                CustomIconButton(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { clickButtonScaleNear() }
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f))
                        .size(56.dp),
                    icon = R.drawable.ic_plus,
                    iconSize = 24,
                )

                CustomIconButton(
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 16.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { clickButtonScaleFar() }
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f))
                        .size(56.dp),
                    icon = R.drawable.ic_remove,
                    iconSize = 26,
                )
                CustomIconButton(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { clickButtonNavigation() }
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f))
                        .size(56.dp),
                    icon = R.drawable.ic_navigation,
                    iconSize = 24,
                    iconColor = navigationIconColor
                )
            }

        }

    }

}

/*
@Preview
@Composable
fun MapScreenContentPreview() {
    MapScreenContent(uiState = UiState(), onEventDispatcher = {})
}*/
