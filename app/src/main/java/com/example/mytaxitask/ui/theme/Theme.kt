package com.example.mytaxitask.ui.theme

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


private val LightColorScheme = lightColorScheme(
    background = Color(0xffffffff),
    onBackground = Color(0xff121212),
    primary = Color(0xFFF5F6F9),
    secondary = Color(0xFF818AB0),
    tertiary = Color(0xFFBBC2D5),


    )
private val DarkColorScheme = darkColorScheme(
    background = Color(0xff121212),
//    background = Color.Gray,
    onBackground = Color(0xffffffff),
    primary = Color(0xFF242424),
    secondary = Color(0xFF999999),
    tertiary = Color(0xFF666666),

)

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun MyTaxiTaskTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
//            myLog( "Status bar rang: ${colorScheme.background.toArgb()}")
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.isStatusBarContrastEnforced = true
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}