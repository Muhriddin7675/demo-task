package com.example.mytaxitask

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cafe.adriel.voyager.navigator.Navigator
import com.example.mytaxitask.domain.AppRepository
import com.example.mytaxitask.presenter.screen.map.MapScreen
import com.example.mytaxitask.service.impl.LocationServiceImpl
import com.example.mytaxitask.ui.theme.MyTaxiTaskTheme
import com.mapbox.mapboxsdk.Mapbox
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var repository: AppRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this)
        val serviceIntent = Intent(this, LocationServiceImpl::class.java)
        startService(serviceIntent)

        setContent {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MyTaxiTaskTheme {
                    Navigator(MapScreen())
                }
            }
        }
    }
}


