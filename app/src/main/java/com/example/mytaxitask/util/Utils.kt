package com.example.mytaxitask.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import timber.log.Timber

fun myLog(message: String, tag: String = "TTT") {
    Timber.tag(tag).d(message)
}

fun getDrawableToBitmap(context: Context, @DrawableRes drawableId: Int): Bitmap {
    val drawable = ContextCompat.getDrawable(context, drawableId)
    val bitmap = Bitmap.createBitmap(drawable!!.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

fun restoreMapView(mapboxMap: MapboxMap,latLong:LatLng) {
    val cameraPosition = CameraPosition.Builder()
        .target(latLong)
        .zoom(15.0)
        .build()
    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
}
