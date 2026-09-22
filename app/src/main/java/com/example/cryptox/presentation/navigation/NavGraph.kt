package com.example.cryptox.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.cryptox.BuildConfig
import com.example.cryptox.common.Constants
import com.example.cryptox.presentation.auth.login.LoginScreen
import com.example.cryptox.presentation.auth.signup.SignUpScreen
import com.example.cryptox.presentation.detail.CoinDetailScreen
import com.example.cryptox.presentation.favorites.FavoritesScreen
import com.example.cryptox.presentation.home.HomeScreen
import com.example.cryptox.presentation.onboarding.OnboardingScreen
import com.example.cryptox.presentation.portfolio.PortfolioScreen

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String,
    snackbarHostState: SnackbarHostState,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(route = Screens.Home.route) {
            HomeScreen(
                navController = navController,
                snackbarHostState = snackbarHostState,
                modifier = modifier
            )
        }
        composable(
            route = Screens.Detail.route + "/{coinKey}",
            arguments = listOf(
                navArgument("coinKey") {
                    type = NavType.StringType
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "https://${BuildConfig.DEEP_LINK_HOST}/coin/{coinKey}"
                }
            )
        ) {
            CoinDetailScreen(modifier = modifier)
        }
        composable(route = Screens.Favorites.route) {
            FavoritesScreen(
                navController = navController,
                snackbarHostState = snackbarHostState,
                modifier = modifier
            )
        }
        composable(route = Screens.Portfolio.route) {
            PortfolioScreen(
                snackbarHostState = snackbarHostState,
                navController = navController,
                modifier = modifier
            )
        }
        composable(route = Screens.Onboarding.route) {
            OnboardingScreen(
                navController = navController,
                modifier = modifier
            )
        }
        composable(route = Screens.Login.route) {
            LoginScreen(
                modifier = modifier,
                onLogin = {
                    navController.navigate(Screens.Onboarding.route) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                },
                onSigUpClicked = { navController.navigate(Screens.SignUp.route) }
            )
        }
        composable(route = Screens.SignUp.route) {
            SignUpScreen(
                navController = navController,
                )
        }
    }
}