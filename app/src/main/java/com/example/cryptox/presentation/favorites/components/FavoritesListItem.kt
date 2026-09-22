package com.example.cryptox.presentation.favorites.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.cryptox.common.exchange.toSelectedCurrencyString
import com.example.cryptox.common.utils.extensions.toRoundedString
import com.example.cryptox.data.remote.dto.Quotes

@Composable
fun FavoritesListItem(
    modifier: Modifier = Modifier,
    onFavoriteClicked: () -> Unit,
    name: String,
    symbol: String,
    price: Quotes?,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            )
            .padding(horizontal = 20.dp)
            .padding(vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "$name ($symbol)",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text =  price?.uSD?.price?.toSelectedCurrencyString() ?: "",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = {
                onFavoriteClicked()
            },
        ) {
            Icon(
                Icons.Default.Favorite,
                contentDescription = "Favorite Symbol",
                tint = Color.Red
            )
        }
    }
}