package com.example.mytaxitask.presenter.screen.map.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.mytaxitask.R
import com.example.mytaxitask.ui.component.CustomIconButton
import com.example.mytaxitask.ui.theme.navigationIconColor

@Composable
fun MapScreenColumnButtons(
    modifier: Modifier,
    visibility: Boolean,
    onClickButtonScaleNear: () -> Unit,
    onClickButtonScaleFar: () -> Unit,
    onClickButtonNavigation: () -> Unit,
    onClickButtonChevronUp: () -> Unit
) {
    Row(modifier = modifier) {

        AnimatedVisibility(
            modifier = Modifier,
            visible = visibility,
            enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn(),
            exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut(),

            ) {

            CustomIconButton(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { onClickButtonChevronUp() }
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
                    .size(56.dp),
                icon = R.drawable.ic_chevrons_up,
                iconSize = 24,
                childBox = true
            )

        }
        Spacer(modifier = Modifier.weight(1f))

        AnimatedVisibility(
            modifier = Modifier,
            visible = visibility,
            enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
            exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut(),
        ) {

            Column {
                CustomIconButton(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onClickButtonScaleNear() }
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
                        .size(56.dp),
                    icon = R.drawable.ic_plus,
                    iconSize = 24,
                )

                CustomIconButton(
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 16.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onClickButtonScaleFar() }
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
                        .size(56.dp),
                    icon = R.drawable.ic_remove,
                    iconSize = 26,
                )
                CustomIconButton(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onClickButtonNavigation() }
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
                        .size(56.dp),
                    icon = R.drawable.ic_navigation,
                    iconSize = 24,
                    iconColor = navigationIconColor
                )
            }

        }

    }

}