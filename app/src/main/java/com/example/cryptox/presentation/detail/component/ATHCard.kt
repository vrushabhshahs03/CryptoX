package com.example.cryptox.presentation.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.cryptox.common.exchange.toSelectedCurrencyString
import com.example.cryptox.common.utils.extensions.formatNumber
import com.example.cryptox.common.utils.extensions.toReadableDate
import com.example.cryptox.common.utils.extensions.toRounded

@Composable
fun ATHSection(
    athPrice: Double,
    percentFromAth: Double,
    athDate: String,
) {
    val isNearAth = percentFromAth > -10
    val color = if(isNearAth)
        Color(0xFF00C853)
    else
        Color(0xFFFF3B30)

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "All Time High Section",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            modifier = Modifier.heightIn(max = 200.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            columns = GridCells.Fixed(2)
        ) {
            item {
                StatCard(
                    title = "ATH Price",
                    value = "${athPrice.toSelectedCurrencyString()}",
                )
            }
            item {
                StatCard(
                    title = "ATH Distance",
                    value = "${percentFromAth.toRounded(2).formatNumber()}%",
                    valueColor = color,
                    showArrow = true,
                    isPositive = isNearAth
                )
            }
            item {
                StatCard(
                    title = "ATH Date",
                    value = "${athDate.toReadableDate()}",
                )
            }
        }
    }
}