package com.example.mytaxitask.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun CustomIconButton(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    iconSize: Int = 24,
    childBox:Boolean = false,
    childBoxColor:Color = MaterialTheme.colorScheme.primary,
    iconColor: Color = MaterialTheme.colorScheme.secondary,

) {
        Box(
            modifier = modifier
        ) {
            Box(
                modifier = if (childBox) Modifier
                    .padding(4.dp)
                    .fillMaxSize()
                    .background(
                        color = childBoxColor.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(8.dp)
                else Modifier.align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(iconSize.dp),
                    painter = painterResource(id = icon), contentDescription = "menu",
                    colorFilter = ColorFilter.tint(iconColor)
                )
            }

        }


}