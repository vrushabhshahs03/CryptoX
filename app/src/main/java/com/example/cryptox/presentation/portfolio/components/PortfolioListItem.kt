package com.example.cryptox.presentation.portfolio.components

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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cryptox.common.exchange.toSelectedCurrencyString
import com.example.cryptox.common.utils.extensions.toRoundedString
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import me.saket.swipe.rememberSwipeableActionsState

@Composable
fun PortfolioListItem(
    modifier: Modifier = Modifier,
    quantity: Int,
    name: String,
    symbol: String,
    price: Double,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    val swipeState = rememberSwipeableActionsState()
    val delete = SwipeAction(
        onSwipe = {
            onDeleteClick()
        },
        icon = {
            Icon(
                imageVector = Icons.Default.Delete,
                "Delete Icon",
                modifier = Modifier.padding(start = 15.dp)
            )
        },
        background = Color.Red
    )
    val edit = SwipeAction(
        onSwipe = {
            onEditClick()
        },
        background = Color.Blue,
        icon = {
            Icon(
                Icons.Default.Edit,
                "Edit Icon",
                modifier = Modifier.padding(end = 15.dp)
            )
        }
    )
    SwipeableActionsBox(
        state = swipeState,
        startActions = listOf(edit),
        endActions = listOf(delete)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(0.45f),
                text = "$name ($symbol) X $quantity",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                modifier = Modifier.weight(0.35f)
                    .padding(start = 5.dp, end = 8.dp),
                textAlign = TextAlign.End,
                text = (price * quantity).toSelectedCurrencyString(),
                style = MaterialTheme.typography.bodyMedium
            )
            IconButton(
                modifier = Modifier.weight(0.1f),
                onClick = {
                    onDeleteClick()
                }) {
                Icon(
                    Icons.Default.Delete,
                    "Delete Icon"
                )
            }
            IconButton(
                modifier = Modifier.weight(0.1f),
                onClick = {
                    onEditClick()
                }) {
                Icon(
                    Icons.Default.Edit,
                    "Edit Icon"
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PortfolioListItemPreview() {
    PortfolioListItem(
        quantity = 3,
        name = "Bitcoin",
        symbol = "BTC",
        price = 1200.34,
        onEditClick = {},
        onDeleteClick = {},
    )
}