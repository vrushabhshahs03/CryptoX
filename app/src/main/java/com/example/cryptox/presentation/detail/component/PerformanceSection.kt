package com.example.cryptox.presentation.detail.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cryptox.common.utils.extensions.toRounded
import com.example.cryptox.data.remote.dto.USD
import kotlin.math.sign

@Composable
fun PerformanceSection(
    price: USD,
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Performance Section",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(12.dp))
        FlowRow(
            maxItemsInEachRow = 4,
            modifier = Modifier.fillMaxWidth()
        ) {
            PerformanceChip("1H", price.percentChange1h)
            PerformanceChip("24H", price.percentChange24h)
            PerformanceChip("7D", price.percentChange7d)
            PerformanceChip("30D", price.percentChange30d)
        }
    }
}

@Composable
fun PerformanceChip(
    label: String,
    value: Double,
) {
    val isPositive = value >= 0
    val backgroundColor = if(isPositive)
        Color(0x1A00C853) else Color(0x1AFF3B30)
    val textColor = if(isPositive)
        Color(0xFF00C853) else Color(0xFFFF3B30)


    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(50),
        modifier = Modifier.padding(4.dp)
    ) {
        Text(
            text = "$label ${value.toRounded(2)}%",
            color = textColor,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
            style = MaterialTheme.typography.labelMedium,
            )
    }
}

@Composable
@Preview
fun PerformanceChipPreview() {
    PerformanceChip(
        label = "24h",
        value = -4.0
    )
}