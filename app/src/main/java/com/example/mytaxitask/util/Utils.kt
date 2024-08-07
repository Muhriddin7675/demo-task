package com.example.mytaxitask.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.LocationListener
import android.location.LocationManager
import android.media.MediaPlayer
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.constants.MapboxConstants
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import timber.log.Timber

val tashkentCenterLatLng = LatLng(41.2995, 69.2401)
const val maxZoom = 24.0
const val minZoom = 1.0
const val YOUR_MAPTILER_API_KEY = "BLPhpLCCcS9XIn3H7CPZ";


fun myLog(message: String, tag: String = "TTT") {
    Timber.tag(tag).d(message)
}

fun getDrawableToBitmap(context: Context, @DrawableRes drawableId: Int): Bitmap {
    val drawable = ContextCompat.getDrawable(context, drawableId)
    val bitmap = Bitmap.createBitmap(
        drawable!!.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

fun restoreMapView(mapboxMap: MapboxMap, latLong: LatLng, zoom: Double, count: Int = 3) {
    val targetCameraPosition = CameraPosition.Builder()
        .target(latLong)
        .zoom(zoom)
        .build()
    mapboxMap.animateCamera(
        CameraUpdateFactory.newCameraPosition(targetCameraPosition),
        MapboxConstants.ANIMATION_DURATION,
        object : MapboxMap.CancelableCallback {
            override fun onCancel() {
                myLog("onCancel called with target: $latLong and zoom: $zoom")
                myLog("Current camera position: ${mapboxMap.cameraPosition}")
                if (count > 0) {
                    restoreMapView(mapboxMap, latLong, zoom, count - 1)
                }
            }
            override fun onFinish() {
                myLog("onFinish called with target: $latLong and zoom: $zoom")
            }
        }
    )
}

fun registerLocationUpdates(context: Context): LatLng {
    var latLong = tashkentCenterLatLng
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val locationListener = LocationListener { location ->
        val latLng = LatLng(location.latitude, location.longitude)
        latLong = latLng
    }

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        return latLong
    }
    locationManager.requestLocationUpdates(
        LocationManager.NETWORK_PROVIDER,
        0L,
        0f,
        locationListener
    )

    return latLong
}


fun playAudio(context: Context, audioResId: Int) {
    val mediaPlayer = MediaPlayer.create(context, audioResId)
    mediaPlayer?.start()
    mediaPlayer?.setOnCompletionListener {
        it.release()
    }
}
//Muhriddin Valiyev
fun ComponentActivity.changeColorStatusBar(
    isDarkMode: Boolean,
    statusBarColor: androidx.compose.ui.graphics.Color
) {
    val window: Window = this.window
    val decorView = window.decorView
    val wic = WindowInsetsControllerCompat(window, decorView)
    wic.isAppearanceLightStatusBars = !isDarkMode
    window.statusBarColor = statusBarColor.toArgb()
}