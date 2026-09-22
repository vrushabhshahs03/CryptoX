package com.example.cryptox.presentation.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cryptox.common.exchange.toSelectedCurrencyString
import com.example.cryptox.common.utils.extensions.formatNumber
import com.example.cryptox.common.utils.extensions.toRounded

@Composable
fun StatsSection(
    marketCap: Long,
    volume24h: Double,
    percentChange7d: Double,
    percentChange30d: Double,
) {
    val is7dPositive = percentChange7d >= 0
    val is30dPositive = percentChange30d >= 0

    Column(modifier = Modifier.padding(all = 16.dp)) {
        Text(
            text = "Stats Section",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.heightIn(max = 200.dp)
        ) {
            item {
                StatCard(
                    title = "Market Cap",
                    value = "${marketCap.toSelectedCurrencyString()}"
                )
            }

            item {
                StatCard(
                    title = "24h Volume",
                    value = "${volume24h.toSelectedCurrencyString()}",
                )
            }

            item {
                StatCard(
                    title = "7D Change",
                    value = "${percentChange7d.toRounded(2).formatNumber()}%",
                    valueColor = if(is7dPositive) Color(0xFF00C853) else Color(0xFFFF3B30),
                    showArrow = true,
                    isPositive = is7dPositive
                )
            }

            item {
                StatCard(
                    title = "30D Change",
                    value = "${percentChange30d.toRounded(2).formatNumber()}%",
                    valueColor = if(is30dPositive) Color(0xFF00C853) else Color(0xFFFF3B30),
                    showArrow = true,
                    isPositive = is30dPositive
                )
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    valueColor: Color = Color.Black,
    isPositive: Boolean = false,
    showArrow: Boolean = false,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = valueColor
                )
                Spacer(modifier = Modifier.height(6.dp))
                if(showArrow) {
                    Icon(
                        imageVector = if(isPositive) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = "Arrow Icon",
                        tint = valueColor,
                        modifier = Modifier.graphicsLayer {
                            scaleX = 1.5f
                            scaleY = 1.5f
                        }
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun StatsSectionPreview() {
    StatsSection(
        marketCap = 0,
        volume24h = 0.0,
        percentChange7d = 0.0,
        percentChange30d = 0.0
    )
}

@Composable
@Preview
fun StatCardPreview() {
    StatCard(
        title = "hello",
        value = "0.0",
        valueColor = Color.Black,
        isPositive = true,
        showArrow = true
    )
}