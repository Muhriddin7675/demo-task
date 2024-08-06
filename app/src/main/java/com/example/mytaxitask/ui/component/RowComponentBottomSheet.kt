package com.example.mytaxitask.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mytaxitask.R

@Composable
fun RowComponentBottomSheet(
    icon: Int,
    title: String,
    text: String = "",
    clickable: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { clickable() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "bottom sheet icon",
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        CustomTextView(
            text = title,
            fontSize = 18,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = 600
        )
        Box(modifier = Modifier.weight(1f))
        CustomTextView(
            text = text,
            fontSize = 18,
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = 600
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_right),
            contentDescription = "bottom sheet icon",
            tint = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.size(24.dp)
        )

    }
}