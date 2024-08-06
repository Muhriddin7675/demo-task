package com.example.mytaxitask.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.LocationListener
import android.location.LocationManager
import android.media.MediaPlayer
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import timber.log.Timber

fun myLog(message: String, tag: String = "TTT") {
    Timber.tag(tag).d(message)
}

const val YOUR_MAPTILER_API_KEY = "BLPhpLCCcS9XIn3H7CPZ";
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

fun restoreMapView(mapboxMap: MapboxMap, latLong: LatLng) {
    val cameraPosition = CameraPosition.Builder()
        .target(latLong)
        .zoom(15.0)
        .build()
    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
}

fun registerLocationUpdates(context: Context, onLocationUpdated: (LatLng) -> Unit) {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val locationListener = LocationListener { location ->
        val latLng = LatLng(location.latitude, location.longitude)
        onLocationUpdated(latLng)
        Toast.makeText(context, "Location is: $latLng", Toast.LENGTH_LONG).show()
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
        return
    }
    locationManager.requestLocationUpdates(
        LocationManager.NETWORK_PROVIDER,
        0L,
        0f,
        locationListener
    )

}


fun playAudio(context: Context, audioResId: Int) {
    val mediaPlayer = MediaPlayer.create(context, audioResId)
    mediaPlayer?.start()

    // MediaPlayer resurslarini tozalash
    mediaPlayer?.setOnCompletionListener {
        it.release()
    }
}
