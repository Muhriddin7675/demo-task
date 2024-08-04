@file:Suppress("DEPRECATION")

package com.example.mytaxitask
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cafe.adriel.voyager.navigator.Navigator
import com.example.mytaxitask.presenter.screen.map.MapScreen
import com.example.mytaxitask.ui.theme.MyTaxiTaskTheme
import com.mapbox.mapboxsdk.Mapbox
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this)
        setContent {
            MyTaxiTaskTheme {
                Navigator(MapScreen())
            }
        }
    }
}


