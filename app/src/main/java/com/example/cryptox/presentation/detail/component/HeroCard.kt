package com.example.cryptox.presentation.detail.component

import android.R.attr.logo
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.cryptox.R
import com.example.cryptox.common.exchange.toSelectedCurrencyString
import com.example.cryptox.common.utils.extensions.formatNumber
import com.example.cryptox.common.utils.extensions.toRounded

@Composable
fun HeroCard(
    modifier: Modifier = Modifier,
    logo: String,
    title: String,
    currentPrice: Double,
    pctChange24h: Double,
    onShare: () -> Unit,
) {
    val isPositive = pctChange24h >= 0
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(logo)
                        .transformations(
                            CircleCropTransformation()
                        )
                        .crossfade(true)
                        .build(),
                    error = painterResource(R.drawable.image_error),
                    placeholder = painterResource(R.drawable.placeholder),
                    contentDescription = "Coin Logo",
                    modifier = Modifier
                        .height(30.dp)
                        .width(30.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = modifier.weight(1f))
                IconButton(
                    onClick = {
                        onShare()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share Icon"
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Current Price",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "${currentPrice.toSelectedCurrencyString()}",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "24H Change",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(5.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "${pctChange24h.toRounded(2)}%",
                    color = if(isPositive) Color.Green else Color.Red,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.width(5.dp))
                Icon(
                    imageVector = if(isPositive) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = "Arrow Icon",
                    tint = if(isPositive) Color.Green else Color.Red,
                    modifier = Modifier.graphicsLayer(
                        scaleX = 2f,
                        scaleY = 2f
                    )
                )
            }
        }
    }
}

@Composable
@Preview
fun HeroCardPreview() {
    HeroCard(
        logo = "logo",
        title = "Bitcoin (BTC)",
        currentPrice = 34545.76,
        pctChange24h = -9.5,
        onShare = {}
    )
}