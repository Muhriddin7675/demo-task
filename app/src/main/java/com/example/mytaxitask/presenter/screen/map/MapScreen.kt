@file:Suppress("DEPRECATION")
package com.example.mytaxitask.presenter.screen.map

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import com.example.mytaxitask.R
import com.example.mytaxitask.presenter.screen.map.MapScreenContract.UiState
import com.example.mytaxitask.presenter.screen.map.component.MapScreenScaffoldContent
import com.example.mytaxitask.presenter.screen.map.component.MapScreenScaffoldSheetContent
import com.example.mytaxitask.util.Status
import com.example.mytaxitask.util.YOUR_MAPTILER_API_KEY
import com.example.mytaxitask.util.changeColorStatusBar
import com.example.mytaxitask.util.getDrawableToBitmap
import com.example.mytaxitask.util.maxZoom
import com.example.mytaxitask.util.minZoom
import com.example.mytaxitask.util.myLog
import com.example.mytaxitask.util.restoreMapView
import com.example.mytaxitask.util.tashkentCenterLatLng
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.maps.MapView
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

class MapScreen : Screen {
    @Composable
    override fun Content() {
        val model: MapScreenContract.MapScreenModel = getViewModel<MapScreenViewModel>()
        val uiState = model.collectAsState().value
        val context = LocalContext.current

        model.collectSideEffect(sideEffect = { sideEffect ->
            when (sideEffect) {
                is MapScreenContract.SideEffect.ShowToast -> {
                    Toast.makeText(context, sideEffect.message, Toast.LENGTH_LONG).show()
                }
            }
        })
        MapScreenContent(uiState = uiState, onEventDispatcher = model::onEventDispatcher)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreenContent(
    uiState: UiState,
    onEventDispatcher: (MapScreenContract.Intent) -> Unit
) {
    val context = LocalContext.current
    val activity = context as ComponentActivity
    val mapView by remember { mutableStateOf(MapView(context)) }
    var zoom by remember { mutableDoubleStateOf(8.0) }
    val lifecycleOwner = LocalLifecycleOwner.current
    var sheetState by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var lastLatLng by remember { mutableStateOf(tashkentCenterLatLng) }
    val scaffoldState = rememberBottomSheetScaffoldState()
    val isSystemInDarkMode = isSystemInDarkTheme()
    var isDarkMode by remember { mutableStateOf(isSystemInDarkMode) }
    var marker by remember { mutableStateOf<Marker?>(null) }

    LaunchedEffect(isSystemInDarkMode) {
        isDarkMode = isSystemInDarkMode
    }

    LaunchedEffect(uiState.sheetState) {
        sheetState = uiState.sheetState

    }
    LaunchedEffect(uiState.latLng) {
            lastLatLng = uiState.latLng
    }

    LaunchedEffect(Unit) {
        mapView.getMapAsync { mapboxMap ->
            mapboxMap.apply {
                setMaxZoomPreference(maxZoom)
                setMinZoomPreference(minZoom)
                uiSettings.isCompassEnabled = false
            }
            restoreMapView(mapboxMap = mapboxMap, latLong = tashkentCenterLatLng, zoom = 7.0)
        }
    }

    activity.changeColorStatusBar(
        isDarkMode = isDarkMode,
        statusBarColor = MaterialTheme.colorScheme.background
    )

    LaunchedEffect(isDarkMode) {
        mapView.getMapAsync { mapboxMap ->
            if (isDarkMode) {
                mapboxMap.setStyle("https://api.maptiler.com/maps/streets-v2-dark/style.json?key=$YOUR_MAPTILER_API_KEY")
            } else {
                mapboxMap.setStyle("https://api.maptiler.com/maps/streets/style.json?key=$YOUR_MAPTILER_API_KEY")
            }
        }
    }

    LaunchedEffect(uiState.zoom) {
        mapView.getMapAsync { mapboxMap ->
            myLog("uiState.zoom: ${uiState.zoom}")
            zoom = if (uiState.setHasZoom) {
                uiState.zoom
            } else {
                val currentZoom = mapboxMap.cameraPosition.zoom
                currentZoom + uiState.zoom - zoom
            }

            if (zoom < minZoom) zoom = minZoom
            if (zoom > maxZoom) zoom = maxZoom
            myLog("zoom: $zoom")
            val cameraPosition = CameraPosition.Builder()
                .target(mapboxMap.cameraPosition.target)
                .zoom(zoom)
                .build()

            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        }
    }

    if (uiState.status == Status.Success)
        LaunchedEffect(uiState.latLng) {
            mapView.getMapAsync { mapboxMap ->
                mapboxMap.apply {
                    if (marker == null) {
                        val bitmap = getDrawableToBitmap(
                            context = context,
                            drawableId = R.drawable.ic_new_car_marker
                        )
                        val icon = bitmap.let { IconFactory.getInstance(context).fromBitmap(it) }
                        val markerOptions = MarkerOptions()
                            .position(lastLatLng)
                            .icon(icon)
                        restoreMapView(mapboxMap = mapboxMap, latLong = lastLatLng, zoom = 15.0)
                        marker = addMarker(markerOptions)
                    } else {
                        marker?.let {
                            it.position = lastLatLng
                        }
                    }
                }
            }
        }

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
            scaffoldState.bottomSheetState.let { sheetState ->
                val sheetStateBool = (sheetState.currentValue == SheetValue.Expanded)
                onEventDispatcher(MapScreenContract.Intent.SetSheetState(sheetStateBool))
            }
            MapScreenScaffoldSheetContent()
        },
        content = {
            MapScreenScaffoldContent(
                scaffoldState = scaffoldState,
                scope = scope,
                onEventDispatcher = onEventDispatcher,
                mapView = mapView,
                sheetState = sheetState,
                lastLatLng = lastLatLng,
                zoom = zoom,
                isLoading = uiState.isLoadingTabButton,
                selectedOption = uiState.selectedOptionTab
            )
        }
    )
}


