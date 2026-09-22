package com.example.cryptox.presentation.portfolio.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cryptox.domain.model.PortfolioItem
import com.example.cryptox.presentation.loading.CustomLoader
import com.example.cryptox.presentation.portfolio.PortfolioUiEvent
import com.example.cryptox.presentation.portfolio.PortfolioViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddPortfolioScreen(
    modifier: Modifier = Modifier,
    coinsList: Map<String, String>,
    viewmodel: PortfolioViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    var selectedCoin by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("0") }
    var showDropDown by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    var showLoading by remember { mutableStateOf(false) }
    val filteredCoins: List<String> =
        if (selectedCoin.isBlank()) {
            coinsList.values.toList()
        } else {
            coinsList.values.filter {
                it.contains(selectedCoin, ignoreCase = true)
            }
        }

    LaunchedEffect(Unit) {
        viewmodel.eventFlow.collect { event ->
            when(event) {
                is PortfolioUiEvent.ItemAddedLoading -> {
                    showLoading = event.isLoading
                }
                else -> {}
            }
        }
    }


    Box(
        modifier = modifier.fillMaxWidth()
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
        ) {
            Text(
                "Add to Portfolio",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 20.dp),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = selectedCoin,
                onValueChange = { value ->
                    selectedCoin = value
                },
                label = {
                    Text("Search Coins...")
                },
                trailingIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            Icons.Default.Search,
                            "Search Icon"
                        )
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth(0.715f)
                    .onFocusChanged { focusState ->
                        showDropDown = focusState.isFocused
                    }
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = quantity,
                onValueChange = { value ->
                    quantity = value
                },
                label = {
                    Text("Quantity")
                },
                modifier = Modifier.fillMaxWidth(0.715f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    if(quantity.toInt() > 0) {
                        if(coinsList.values.contains(selectedCoin)) {
                            viewmodel.addPortfolioItem(
                                PortfolioItem(
                                    coinId = coinsList.entries.first { it.value == selectedCoin }.key,
                                    symbol = selectedCoin
                                        .substringAfter("(")
                                        .substringBefore(")"),
                                    quantity = quantity.toInt(),
                                    averageBuyPrice = 0.0
                                )
                            )
                        } else {
                            Toast.makeText(context, "Select a proper coin from the list!", Toast.LENGTH_LONG)
                                .show()
                        }
                    } else {
                        Toast.makeText(context, "At least one quantity needs to be entered!", Toast.LENGTH_LONG)
                            .show()
                    }
                },
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text("Add to Portfolio", color = Color.White)
            }
        }
        if(showDropDown) {
            Card(
                modifier = Modifier
                    .padding(top = 140.dp)
                    .fillMaxWidth(0.715f)
                    .heightIn(max = 200.dp)
                    .align(Alignment.TopCenter)
                    .zIndex(1f),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                )
            ) {
                LazyColumn(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(horizontal = 30.dp)
                ) {
                    items(items = filteredCoins) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    onClick = {
                                        selectedCoin = it
                                        focusManager.clearFocus()
                                    }
                                )
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Text(it)
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPortfolioScreens(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var selectedCoin by remember {
        mutableStateOf("")
    }
    var quantity by remember {
        mutableStateOf("0")
    }
    var showDropDown by remember {
        mutableStateOf(false)
    }
    val coins = listOf(
        "Bitcoin (BTC)",
        "Ethereum (ETH)",
        "Tether (USDT)",
        "BNB (BNB)",
        "XRP (XRP)",
        "USDC (USDC)",
        "Solana (SOL)",
        "TRON (TRX)",
        "Hyperliquid (HYPE)",
        "Lido Staked Ether (STETH)",
        "Dogecoin (DOGE)",
        "Usds (USDS)",
        "Zcash (ZEC)",
        "Wrapped Bitcoin (WBTC)",
        "Cardano (ADA)",
        "Wrapped Liquid Staked Ether 2.0 (WSTETH)"
    )
    val filteredCoins =
        if(selectedCoin.isBlank()) {
            coins
        } else {
            coins.filter {
                it.contains(
                    selectedCoin,
                    ignoreCase = true
                )
            }
        }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Add to Portfolio",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 20.dp),
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = selectedCoin,
                onValueChange = {
                    selectedCoin = it
                },
                label = {
                    Text("Search Coins...")
                },
                trailingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .onFocusChanged {
                        showDropDown = it.isFocused
                    }
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = quantity,
                onValueChange = {
                    quantity = it
                },
                label = {
                    Text("Quantity")
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if(coins.contains(selectedCoin)) {
                        Toast.makeText(
                            context,
                            "Done",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Chukla",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text(
                    text = "Add to Portfolio",
                    color = Color.White
                )
            }
        }

        if(showDropDown) {
            Card(
                modifier = Modifier
                    .padding(top = 145.dp)
                    .fillMaxWidth(0.85f)
                    .height(200.dp)
                    .align(Alignment.TopCenter)
                    .zIndex(1f),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                )
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    items(filteredCoins) { coin ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedCoin = coin

                                    focusManager.clearFocus()
                                }
                                .padding(20.dp),
                            horizontalArrangement =
                                Arrangement.Center
                        ) {
                            Text(coin)
                        }

                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AddPortfolioScreenPreview() {
    AddPortfolioScreen(
        coinsList = emptyMap()
    )
}