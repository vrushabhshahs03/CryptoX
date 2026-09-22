package com.example.cryptox.presentation.portfolio.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cryptox.domain.model.PortfolioItem
import com.example.cryptox.presentation.loading.CustomLoader
import com.example.cryptox.presentation.portfolio.PortfolioUiEvent
import com.example.cryptox.presentation.portfolio.PortfolioViewModel

@Composable
fun EditPortfolio(
    modifier: Modifier = Modifier,
    portfolioItem: PortfolioItem?,
    coin: String,
    viewmodel: PortfolioViewModel = hiltViewModel(),
) {
    var quantity by remember { mutableStateOf(portfolioItem?.quantity.toString()) }
    var showLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewmodel.eventFlow.collect { event ->
            when(event) {
                is PortfolioUiEvent.ItemEditedLoading -> {
                    showLoading = event.isLoading
                }
                else -> {}
            }
        }
    }

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        if(showLoading) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable(
                        onClick = {}, indication = null,
                        interactionSource = remember {
                            MutableInteractionSource()
                        })
                    .background(
                        Color.Black.copy(alpha = 0.1f)
                    )
            ) {
                CustomLoader(modifier = Modifier.align(Alignment.Center))
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.715f),
                value = "${coin}",
                readOnly = true,
                label = {
                    Text("Coin")
                },
                singleLine = true,
                onValueChange = {}
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.715f),
                value = quantity,
                singleLine = true,
                label = {
                    Text("Quantity")
                },
                onValueChange = { value ->
                    quantity = value
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    if(quantity.toInt() > 0) {
                        portfolioItem?.let {
                            viewmodel.addPortfolioItem(
                                PortfolioItem(
                                    coinId = it.coinId,
                                    symbol = it.symbol,
                                    quantity = quantity.toInt(),
                                    averageBuyPrice = it.averageBuyPrice
                                )
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Text("Update Coin", color = Color.White)
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun EditPortfolioPreview() {
    EditPortfolio(
        coin = "",
        portfolioItem = PortfolioItem(
            symbol = "TBH",
            quantity = 3,
            averageBuyPrice = 23.89
        )
    )
}