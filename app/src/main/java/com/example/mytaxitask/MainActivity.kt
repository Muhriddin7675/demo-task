package com.example.mytaxitask

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import cafe.adriel.voyager.navigator.Navigator
import com.example.mytaxitask.presenter.screen.map.MapScreen
import com.example.mytaxitask.service.impl.LocationServiceImpl
import com.example.mytaxitask.ui.theme.MyTaxiTaskTheme
import com.mapbox.mapboxsdk.Mapbox
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startLocationService()
            setMapContent()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this)

        if (hasLocationPermission()) {
            startLocationService()
            setMapContent()
        } else {
            requestLocationPermission()
        }
    }

    private fun hasLocationPermission() =
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    private fun requestLocationPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun startLocationService() {
        val serviceIntent = Intent(this, LocationServiceImpl::class.java)
        startService(serviceIntent)
    }

    private fun setMapContent() {
        setContent {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MyTaxiTaskTheme {
                    Navigator(MapScreen())
                }
            }
        }
    }
}
