package com.example.cryptox.presentation.navigation.bottombar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.cryptox.common.Constants

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector,
) {
    object Home: BottomBarScreen(
        route = Constants.HOME_ROUTE,
        title = "Home",
        icon=Icons.Default.Home
    )
    object Favorites: BottomBarScreen(
        route = Constants.FAVORITES_ROUTE,
        title = "Favorites",
        icon=Icons.Default.Favorite
    )
    object Portfolio: BottomBarScreen(
        route = Constants.PORTFOLIO_ROUTE,
        title = "Portfolio",
        icon = Icons.Default.Wallet,
    )
}