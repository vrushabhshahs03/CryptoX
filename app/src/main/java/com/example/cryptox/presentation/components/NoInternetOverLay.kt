package com.example.cryptox.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun NoInternetOverlay() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                onClick = {},
                interactionSource = remember { MutableInteractionSource() },
            )
            .background(Color.Black.copy(alpha = 0.7f))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No Internet Connection",
                    style = MaterialTheme.typography.body1
                )

                Text(
                    text = "Please check your internet connection.",
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun NoInternetOverlayPreview() {
    NoInternetOverlay()
}