package com.example.mytaxitask.presenter.screen.map.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mytaxitask.R
import com.example.mytaxitask.ui.component.RowComponentBottomSheet

@Composable
fun MapScreenScaffoldSheetContent() {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primary)
        ) {
            RowComponentBottomSheet(
                icon = R.drawable.ic_tariff,
                title = stringResource(id = R.string.tariff),
                text = "6/8",
                clickable = {})
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 0.7.dp,
                color = MaterialTheme.colorScheme.tertiary
            )
            RowComponentBottomSheet(
                icon = R.drawable.ic_order,
                title = stringResource(id = R.string.orders),
                text = "0",
                clickable = {})
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 0.7.dp,
                color = MaterialTheme.colorScheme.tertiary
            )
            RowComponentBottomSheet(
                icon = R.drawable.ic_rocket,
                title = stringResource(id = R.string.thereIs),
                clickable = {})

        }
    }
}