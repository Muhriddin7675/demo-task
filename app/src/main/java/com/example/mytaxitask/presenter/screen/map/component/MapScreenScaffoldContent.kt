package com.example.mytaxitask.presenter.screen.map.component

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
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
import com.example.mytaxitask.util.myLog
import com.example.mytaxitask.util.playAudio
import com.example.mytaxitask.util.restoreMapView
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreenScaffoldContent(
    mapView: MapView,
    zoom: Double,
    lastLatLng: LatLng = LatLng(0.0, 0.0),
    fullBootSheet: Boolean,
    scaffoldState: BottomSheetScaffoldState,
    scope: CoroutineScope,
    onEventDispatcher: (MapScreenContract.Intent) -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    val selectedOption = remember { mutableIntStateOf(0) }
    val optionTexts = listOf("Band","Faol")
    val context = LocalContext.current
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
                    selectedOption = selectedOption.intValue) {

                    if(selectedOption.intValue != it)
                    scope.launch {
                        isLoading = true
                        playAudio(context, R.raw.click_tab_button)
                        delay(200)
                        isLoading = false
                        delay(50)
                        selectedOption.intValue = it
                    }
                }
                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.background),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary, strokeWidth = 3.dp, modifier = Modifier.size(24.dp))
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

        MapScreenColumnButtons(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Center),
            clickButtonScaleNear = {
                if (zoom < 22){
                    onEventDispatcher(
                        MapScreenContract.Intent.ClickButtonScaleNear(
                            zoom + 1
                        )
                    )
                }
            },
            clickButtonScaleFar = {
                if (zoom >= 3){
                    onEventDispatcher(
                        MapScreenContract.Intent.ClickButtonScaleFar(
                            scale = zoom - 1
                        )
                    )
                }
            },
            clickButtonNavigation = {
                mapView.getMapAsync { mapBoxMap ->
                    onEventDispatcher(
                        MapScreenContract.Intent.ClickButtonScaleNear(15.0)
                    )

                    restoreMapView(mapboxMap = mapBoxMap, latLong = lastLatLng)
                    restoreMapView(mapboxMap = mapBoxMap, latLong = lastLatLng)
                    myLog("fun restoreMapView  $lastLatLng")
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