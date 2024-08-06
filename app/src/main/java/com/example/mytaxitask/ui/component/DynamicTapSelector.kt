package com.example.mytaxitask.ui.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mytaxitask.ui.theme.black

@Composable
fun DynamicTabSelector(
    tabs: List<String>,
    selectedOption: Int = 0,
    containerColor: Color = MaterialTheme.colorScheme.background,
    tabColorList: List<Color>,
    selectedOptionColor: Color = black,
    containerCornerRadius: Dp = 14.dp,
    tabCornerRadius: Dp = 12.dp,
    selectorHeight: Dp = 56.dp,
    tabHeight: Dp = 48.dp,
    spacing: Dp = 4.dp,
    textStyle: TextStyle = TextStyle(
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.W400,
        fontSize = 18.sp
    ),
    selectedTabTextStyle: TextStyle = TextStyle(
        color = selectedOptionColor,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.W600,
        fontSize = 18.sp
    ),
    onTabSelected: (selectedIndex: Int) -> Unit = {}
) {
    if (tabs.size !in 2..4) {
        throw IllegalArgumentException("DynamicTabSelector must have between 2 and 4 options")
    }
    BoxWithConstraints(
        modifier = Modifier
            .clip(RoundedCornerShape(containerCornerRadius))
            .height(selectorHeight)
            .fillMaxSize()
            .background(containerColor)
    ) {
        val segmentWidth = maxWidth / tabs.size
        val boxWidth = segmentWidth - spacing * 2
        val positions = tabs.indices.map { index ->
            segmentWidth * index + (segmentWidth - boxWidth) / 2
        }
        val animatedOffsetX by animateDpAsState(targetValue = positions[selectedOption], label = "")
        val containerHeight = maxHeight
        val verticalOffset = (containerHeight - tabHeight) / 2

        Row(
            modifier = Modifier.fillMaxHeight(),
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEachIndexed { index, text ->
                Text(
                    text = text,
                    style = textStyle,
                    modifier = Modifier
                        .width(segmentWidth)
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            onTabSelected(index)
                        }
                )
            }
        }
        Box(
            modifier = Modifier
                .offset(x = animatedOffsetX, y = verticalOffset)
                .clip(RoundedCornerShape(tabCornerRadius))
                .width(boxWidth)
                .height(tabHeight)
                .background(tabColorList[selectedOption])
        ) {
            Text(
                text = tabs[selectedOption],
                modifier = Modifier.align(Alignment.Center),
                style = selectedTabTextStyle
            )
        }
    }
}