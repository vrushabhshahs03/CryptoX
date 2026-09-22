package com.example.cryptox.presentation.home

import android.R.attr.top
import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cryptox.domain.model.CurrencyCode
import com.example.cryptox.presentation.auth.common.AuthProvider
import com.example.cryptox.presentation.home.component.CoinListItem
import com.example.cryptox.presentation.components.CustomDrawer
import com.example.cryptox.presentation.components.OTPTextField
import com.example.cryptox.presentation.home.component.ChangePassword
import com.example.cryptox.presentation.home.component.EditUserProfile
import com.example.cryptox.presentation.home.component.HomeModalState
import com.example.cryptox.presentation.loading.CustomLoader
import com.example.cryptox.presentation.main.components.DefaultTopBar
import com.example.cryptox.presentation.navigation.Screens
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    viewmodel: HomeViewModel = hiltViewModel(),
) {
    val state = viewmodel.state.value
    var backPressedTime by remember { mutableStateOf(0L) }
    var showDrawer by remember { mutableStateOf(false) }
    var modalState by remember { mutableStateOf<HomeModalState>(HomeModalState.Hidden) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val pullToRefreshState = rememberPullToRefreshState()
    var showDialog by remember { mutableStateOf(false) }
    val selectedCurrency = viewmodel.selectedCurrency.value
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    BackHandler {
        val currentTime = System.currentTimeMillis()
        if(currentTime - backPressedTime < 2000) {
            (context as? Activity)?.finish()
        } else {
            backPressedTime = currentTime
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "Press back again to exit"
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        val user = FirebaseAuth.getInstance().currentUser
        val provider = user?.providerId
        val phoneNumber = user?.phoneNumber
        if((provider == "password") and (phoneNumber == null)) {
            viewmodel.deleteAccount()
        }
    }

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
                is HomeUiEvent.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is HomeUiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
                is HomeUiEvent.AccountDeleted -> {
                    showDialog = false
                }
                is HomeUiEvent.LoggedOut -> {
                    navController.navigate(Screens.Login.route) {
                        popUpTo(0)
                    }
                }
                is HomeUiEvent.PasswordChanged -> {
                    modalState = HomeModalState.Hidden
                }
                is HomeUiEvent.ProfileUpdated -> {
                    modalState = HomeModalState.Hidden
                }
                else -> {}
            }
        }
    }



    Column(modifier = modifier.fillMaxSize()) {
        DefaultTopBar(
            onQueryChange = { newValue ->
                viewmodel.onSearchQueryChange(newValue)
            },
            onClick = {
                showDrawer = !showDrawer
            },
            image = state.user?.photoUri,
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    onClick = {
                        showDrawer = false
                    }
                )
        ) {
            if(showDialog) {
                AlertDialog(
                    modifier = Modifier.align(Alignment.Center),
                    onDismissRequest = {
                        showDialog = false
                    },
                    title = {
                        Text("Delete Account")
                    },
                    text = {
                        Text("Are you sure you want to delete account?")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showDialog = false
                                viewmodel.deleteAccount()
                            }
                        ) {
                            Text("Delete")
                        }
                    },
                )
            }
            if(state.coins.isEmpty() and (state.user != null) and !state.isLoading and state.error.isEmpty()) {
                Text(
                    "No coins founds for your search!",
                    modifier = Modifier.align(Alignment.Center)
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
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    PullToRefreshBox(
                        isRefreshing = state.isLoading,
                        indicator = {
                        },
                        state = pullToRefreshState,
                        onRefresh = {
                            viewmodel.refresh()
                        }
                    ) {
                        HorizontalDivider()
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    top = (pullToRefreshState.distanceFraction * 50).dp
                                )
                        ) {
                            items(state.coins) { coinPrice ->
                                CoinListItem(
                                    price = coinPrice,
                                    onFavoriteClick = {
                                        if(state.favoriteCoinIds.contains(coinPrice.coinId)) {
                                            viewmodel.removeFavorite(coinPrice.coinId)
                                        } else {
                                            viewmodel.addFavorite(coinPrice.coinId)
                                        }
                                    },
                                    onItemClick = {
                                        navController.navigate(
                                            "${Screens.Detail.route}/${coinPrice.coinId}"
                                        )
                                    },
                                    isFavorite = state.favoriteCoinIds.contains(coinPrice.coinId),
                                    logo = "https://static.coinpaprika.com/coin/${coinPrice.coinId}/logo.png"
                                )
                            }
                        }
                    }
                }
            }
            CustomDrawer(
                onProfileUpdate = { modalState = HomeModalState.EditProfile },
                onPasswordChange = {
                    modalState = HomeModalState.ChangePassword
                },
                selectedCurrency = selectedCurrency,
                onAccountDelete = {
                    showDialog = true
                },
                onLogOut = {
                    viewmodel.logout()
                },
                showDrawer = showDrawer,
                showPasswordChange = state.providers.contains(AuthProvider.EmailPassword),
                onToggle = {
                    showDrawer = !showDrawer
                },
                onCurrencyChange = { currency ->
                    viewmodel.saveCurrency(CurrencyCode.valueOf(currency))
                },
                name = state.user?.name
            )
            if(modalState != HomeModalState.Hidden) {
                ModalBottomSheet(
                    sheetState = sheetState,
                    onDismissRequest = {
                        modalState = HomeModalState.Hidden
                    },
                ) {
                    when(modalState) {
                        is HomeModalState.Hidden -> Unit
                        is HomeModalState.ChangePassword -> {
                            ChangePassword()
                        }
                        is HomeModalState.EditProfile -> {
                            state.user?.let {
                                EditUserProfile(
                                    user = it
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}