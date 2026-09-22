package com.example.cryptox.presentation.portfolio

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cryptox.common.exchange.toSelectedCurrencyString
import com.example.cryptox.domain.model.PortfolioItem
import com.example.cryptox.presentation.components.GeneralTopBar
import com.example.cryptox.presentation.loading.CustomLoader
import com.example.cryptox.presentation.navigation.Screens
import com.example.cryptox.presentation.portfolio.components.AddPortfolioScreen
import com.example.cryptox.presentation.portfolio.components.EditPortfolio
import com.example.cryptox.presentation.portfolio.components.PortfolioListItem
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    navController: NavHostController,
    viewmodel: PortfolioViewModel = hiltViewModel(),
) {
    val state = viewmodel.state.value
    val coins = viewmodel.cachedCoinsList
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val pullToRefreshState = rememberPullToRefreshState()
    var showBottomSheet by remember { mutableStateOf<PortfolioModalState>(PortfolioModalState.Hidden) }
    var editingPortfolioItem by remember { mutableStateOf<PortfolioItem?>(null) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewmodel.isCurrentUserActive()
    }

    LaunchedEffect(state.isActive) {
        if(!state.isActive) {
            navController.navigate(Screens.Login.route) {
                popUpTo(0)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewmodel.eventFlow.collect { event ->
            when(event) {
                is PortfolioUiEvent.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(message = event.message)
                }
                is PortfolioUiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
                is PortfolioUiEvent.ItemAdded -> {
                    showBottomSheet = PortfolioModalState.Hidden
                }
                is PortfolioUiEvent.ItemDeleted -> {}
                is PortfolioUiEvent.ItemEdited -> {
                    editingPortfolioItem = null
                    showBottomSheet = PortfolioModalState.Hidden
                }
                else -> {}
            }
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        GeneralTopBar(title = "Portfolio")
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Card(
                    modifier = Modifier
                        .weight(3f)
                        .heightIn(min = 300.dp)
                        .fillMaxWidth()
                        .padding(20.dp),
                    shape = RoundedCornerShape(20.dp),
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                "Current Value",
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            Text(
                                "${state.portfolioPrice.toSelectedCurrencyString()}",
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                }
                HorizontalDivider()
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .weight(7f)
                        .fillMaxSize()
                ) {
                    if(state.portfolioItems.isEmpty() and !state.isLoading and state.error.isBlank()) {
                        Text(
                            "Portfolio is empty, add coins to see them here!",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(
                                    horizontal = 40.dp
                                )
                        )
                    }
                    if(state.isLoading) {
                        CustomLoader(modifier = Modifier.align(Alignment.Center))
                    }
                    if(state.error.isNotBlank() and !state.isLoading) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                state.error,
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(
                                        horizontal = 40.dp
                                    )
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            IconButton(onClick = {
                                viewmodel.refresh()
                            }) {
                                Icon(
                                    modifier = Modifier
                                        .width(30.dp)
                                        .height(30.dp),
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Refresh Icon"
                                )
                            }
                        }
                    } else {
                        val priceMap = state.prices.associateBy { it.coinId }
                        PullToRefreshBox(
                            isRefreshing = state.isLoading,
                            indicator = {},
                            state = pullToRefreshState,
                            onRefresh = {
                                viewmodel.refresh()
                            }
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = (pullToRefreshState.distanceFraction * 50).dp
                                    )
                                    .padding(horizontal = 10.dp)
                            ) {
                                items(items = state.portfolioItems.entries.toList()) { (id, portfolioItem) ->
                                    PortfolioListItem(
                                        quantity = portfolioItem.quantity,
                                        name = priceMap[id]?.name ?: "",
                                        symbol = priceMap[id]?.symbol ?: "",
                                        price = priceMap[id]?.price?.uSD?.price ?: 0.0,
                                        onDeleteClick = {
                                            viewmodel.removePortfolio(id)
                                        },
                                        onEditClick = {
                                            showBottomSheet = PortfolioModalState.EditPortfolioItem
                                            editingPortfolioItem = portfolioItem
                                        }
                                    )
                                    HorizontalDivider()
                                }
                            }
                        }


                        if(showBottomSheet != PortfolioModalState.Hidden) {
                            ModalBottomSheet(
                                sheetState = sheetState,
                                onDismissRequest = {
                                    editingPortfolioItem = null
                                    showBottomSheet = PortfolioModalState.Hidden
                                },
                            ) {
                                if(showBottomSheet == PortfolioModalState.EditPortfolioItem) {
                                    if(editingPortfolioItem != null) {
                                        val item = editingPortfolioItem
                                        item?.let { item ->
                                            EditPortfolio(
                                                portfolioItem = item,
                                                coin = coins.get(item.coinId) ?: ""
                                            )
                                        }
                                    }
                                } else {
                                    AddPortfolioScreen(
                                        coinsList = coins,
                                    )
                                }
                            }
                        }
                    }


                    IconButton(
                        onClick = {
                            showBottomSheet = PortfolioModalState.AddPortfolioItem
                        },
                        modifier = Modifier
                            .padding(bottom = 20.dp)
                            .padding(end = 20.dp)
                            .background(
                                color = Color.Gray,
                                shape = CircleShape
                            )
                            .align(Alignment.BottomEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Icon",
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PortfolioScreenPreview() {
    PortfolioScreen(
        navController = rememberNavController(),
        snackbarHostState = SnackbarHostState(),
    )
}