package com.example.mytaxitask.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun CustomTextView(
    text: String,
    modifier: Modifier = Modifier,
    fontWeight: Int = 400,
    fontSize: Int = 16,
    maxLines:Int = 1,
    color: Color = Color.Black,
    textAlign: TextAlign = TextAlign.Center,
    lineHeight: Float? = null
) {
    Text(
        text = text,
        modifier = modifier,
        fontWeight = FontWeight(fontWeight),
        fontSize = fontSize.sp,
        color = color,
        fontFamily = FontFamily.Default,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        textAlign = textAlign,
        lineHeight = lineHeight?.sp ?: TextUnit.Unspecified
    )
}