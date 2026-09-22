package com.example.cryptox.presentation.navigation

import com.example.cryptox.common.Constants

sealed class Screens(val route: String) {
    object Home: Screens(route = Constants.HOME_ROUTE)
    object Favorites: Screens(route = Constants.FAVORITES_ROUTE)
    object Portfolio: Screens(route = Constants.PORTFOLIO_ROUTE)
    object Detail: Screens(route = Constants.DETAIL_ROUTE)
    object Onboarding: Screens(route=Constants.ONBOARDING_ROUTE)
    object Login: Screens(route=Constants.LOGIN_ROUTE)
    object SignUp: Screens(route=Constants.SIGNUP_ROUTE)
}