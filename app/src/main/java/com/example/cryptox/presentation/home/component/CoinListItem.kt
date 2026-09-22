package com.example.cryptox.presentation.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.cryptox.R
import com.example.cryptox.common.exchange.toSelectedCurrency
import com.example.cryptox.common.exchange.toSelectedCurrencyString
import com.example.cryptox.common.utils.extensions.toRounded
import com.example.cryptox.domain.model.Coin

@Composable
fun CoinListItem(
    isFavorite: Boolean,
    modifier: Modifier = Modifier,
    price: Coin,
    logo: String,
    onItemClick: (Coin) -> Unit,
    onFavoriteClick: () -> Unit,
) {
    Column() {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable(onClick = { onItemClick(price) })
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = Modifier.weight(0.65f)) {
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
                    text = "${price.rank}. ${price.name} (${price.symbol})",
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Column(modifier = Modifier.weight(0.3f),
                horizontalAlignment = Alignment.Start) {
                price.price?.uSD?.let {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 8.dp),
                        text = "${it.price.toSelectedCurrencyString()}",
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Absolute.Left,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 8.dp)
                    ) {
                        val isPositive = it.percentChange24h >= 0
                        Text(
                            text = it.percentChange24h.toRounded(2).toString(),
                            textAlign = TextAlign.End,
                            color = if(isPositive) Color.Green else Color.Red,
                            style = MaterialTheme.typography.bodyMedium,

                        )

                        Icon(
                            imageVector = if(isPositive) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                            contentDescription = "Arrow Icon",
                            tint = if(isPositive) Color.Green else Color.Red,
                            )
                    }
                }
            }

            IconButton(
                modifier = Modifier.weight(0.05f),
                onClick = {
                    onFavoriteClick()
                },
            ) {
                Icon(
                    if(isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite Symbol",
                    tint = if(isFavorite) Color.Red else Color.Black
                )
            }
        }
        HorizontalDivider()
    }
}