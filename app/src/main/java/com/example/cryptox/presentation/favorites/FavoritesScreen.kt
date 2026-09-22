package com.example.cryptox.presentation.favorites

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.cryptox.presentation.components.GeneralTopBar
import com.example.cryptox.presentation.favorites.components.FavoritesListItem
import com.example.cryptox.presentation.loading.CustomLoader
import com.example.cryptox.presentation.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    viewmodel: FavoritesViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val state = viewmodel.state.value
    val context = LocalContext.current
    val pullToRefreshState = rememberPullToRefreshState()

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
                is FavoriteUiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is FavoriteUiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Column(modifier = modifier.fillMaxSize())
    {
        GeneralTopBar(title = "Favorites")
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if(state.isLoading) {
                CustomLoader()
            }
            if(state.favorites.isEmpty() and !state.isLoading and state.error.isEmpty()) {
                Text(
                    text = "No coins added to favorites. Add coins to favorites to see them here!",
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
            if(state.error.isNotBlank() and !state.isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    androidx.compose.material3.Text(
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
                PullToRefreshBox(
                    isRefreshing = state.isLoading,
                    indicator = {},
                    state = pullToRefreshState,
                    onRefresh = {
                        viewmodel.refresh()
                    }
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(
                            top = (pullToRefreshState.distanceFraction * 50).dp
                        )
                    ) {
                        items(state.prices.filter { coinPrice ->
                            state.favorites.contains(coinPrice.coinId)
                        }) {
                            it?.let {
                                FavoritesListItem(
                                    name = it.name,
                                    symbol = it.symbol,
                                    price = it.price,
                                    onFavoriteClicked = {
                                        viewmodel.removeFavorite(it.coinId)
                                    },
                                    onClick = {
                                        navController.navigate(
                                            "${Screens.Detail.route}/${it.coinId}"
                                        )
                                    }
                                )
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }
        }
    }
}