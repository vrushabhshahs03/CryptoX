package com.example.cryptox.presentation.onboarding

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.cryptox.R

sealed class OnBoardingPage(
    var image: Int,
    var title: String,
    var description: String,
) {
    object Observe: OnBoardingPage(
        image = R.drawable.bitcoin,
        title = "Observe Coins",
        description = "Observe ranked crypto coins and view their details."
    )
    object Search: OnBoardingPage(
        image = R.drawable.seo,
        title = "Search Coins",
        description = "Search for coins and view their details."
    )
    object Favorites: OnBoardingPage(
        image = R.drawable.wishlist,
        title = "Favorites",
        description = "Save your favorite crypto coins for easier access."
    )
    object Portfolio: OnBoardingPage(
        image = R.drawable.portfolio,
        title = "Portfolio",
        description = "Add your crypto coins and calculate your current holdings."
    )
}