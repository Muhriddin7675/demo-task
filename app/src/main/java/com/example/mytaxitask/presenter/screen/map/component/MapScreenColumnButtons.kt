package com.example.mytaxitask.presenter.screen.map.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
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
    val visibilityState by rememberUpdatedState(visibility)

    Row(modifier = modifier) {
        AnimatedVisibility(
            visible = visibilityState,
            enter = slideInHorizontally(initialOffsetX = { -2 * it }) + fadeIn(animationSpec = tween(1200)),
            exit = slideOutHorizontally(targetOffsetX = { -2 * it }) + fadeOut(animationSpec = tween(1200)),
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

        Box(modifier = Modifier.weight(1f))

        AnimatedVisibility(
            visible = visibilityState,
            enter = slideInHorizontally(initialOffsetX = { 2 * it }) + fadeIn(animationSpec = tween(1200)),
            exit = slideOutHorizontally(targetOffsetX = { 2 * it }) + fadeOut(animationSpec = tween(1200)),
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


