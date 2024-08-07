package com.example.mytaxitask.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.MediaPlayer
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.toArgb
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
                if (count > 0) {
                    restoreMapView(mapboxMap, latLong, zoom, count - 1)
                }
            }
            override fun onFinish() {}
        }
    )
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