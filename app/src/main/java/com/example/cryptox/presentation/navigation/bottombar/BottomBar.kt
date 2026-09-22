package com.example.cryptox.presentation.navigation.bottombar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cryptox.presentation.navigation.Screens

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val screens = listOf<BottomBarScreen>(
        BottomBarScreen.Home,
        BottomBarScreen.Favorites,
        BottomBarScreen.Portfolio,
    )

    val bottomBarHiddenRoutes = setOf(
        Screens.Onboarding.route,
        Screens.Login.route,
        Screens.SignUp.route
    )

    val currentStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentStackEntry?.destination
    val bottomBarDestination = currentDestination?.route !in bottomBarHiddenRoutes
    if(bottomBarDestination) {
        NavigationBar() {
            screens.forEach { screen ->
                addBottomItem(
                    navController = navController,
                    screen = screen,
                    currentDestination = currentDestination
                )
            }
        }
    }
}

@Composable
fun RowScope.addBottomItem(
    navController: NavHostController,
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
) {
    NavigationBarItem(
        label = {
            Text(
                text = screen.title
            )
        },
        icon = { Icon(imageVector = screen.icon, contentDescription = "BottomBar Screen Symbol") },
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.startDestinationId){
                    saveState = true
                }
                restoreState = true
                launchSingleTop = true
            }
        },
        colors = NavigationBarItemDefaults.colors(
            unselectedIconColor = LocalContentColor.current.copy(alpha = 0.4f),
            unselectedTextColor = LocalContentColor.current.copy(alpha = 0.4f),
        ),
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true
    )
}

@Composable
@Preview
fun BottomBarPreview() {
    BottomBar(navController = rememberNavController())
}