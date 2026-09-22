package com.example.cryptox.presentation.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.cryptox.presentation.main.MainViewModel
import com.example.cryptox.presentation.navigation.Screens
import com.example.cryptox.presentation.onboarding.components.FinishButton
import com.example.cryptox.presentation.onboarding.components.PagerScreen
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val onBoardingState = viewModel.onBoardingState.value

    if(onBoardingState) {
        navController.navigate(Screens.Home.route)
    } else {
        val pages = listOf<OnBoardingPage>(
            OnBoardingPage.Observe,
            OnBoardingPage.Search,
            OnBoardingPage.Favorites,
            OnBoardingPage.Portfolio
        )
        val pagerState = rememberPagerState()

        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                count = pages.size,
                state = pagerState,
                modifier = Modifier.weight(8f)
            ) { position ->
                PagerScreen(screen = pages[position])
            }
            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier.weight(1f)
            )
            FinishButton(
                onClick = {
                    viewModel.saveOnBoardingState(true)
                    navController.navigate(Screens.Home.route) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                },
                pagerState = pagerState,
                modifier = Modifier
                    .weight(2f)
                    .padding(horizontal = 20.dp)
            )
        }
    }
}