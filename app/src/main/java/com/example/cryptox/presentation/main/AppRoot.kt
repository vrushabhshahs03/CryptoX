package com.example.cryptox.presentation.main

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.cryptox.presentation.components.NoInternetOverlay
import com.example.cryptox.presentation.navigation.NavGraph
import com.example.cryptox.presentation.navigation.bottombar.BottomBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRoot(
    viewModel: MainViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    val isConnected by viewModel.isConnected.collectAsStateWithLifecycle()
    var startDestination by rememberSaveable { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val window = (context as Activity).window

        WindowCompat.setDecorFitsSystemWindows(window, false)

        WindowInsetsControllerCompat(window, window.decorView)
            .hide(WindowInsetsCompat.Type.systemBars())
    }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when(event) {
                is MainUiEvent.StartDestination -> {
                    startDestination = event.destination
                }
            }
        }
    }

    startDestination?.let{
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState)
                },
                bottomBar = {
                    BottomBar(navController = navController)
                },
            ) { paddingValues ->
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Surface(color = MaterialTheme.colorScheme.background) {

                        NavGraph(
                            modifier = Modifier.padding(paddingValues),
                            navController = navController,
                            startDestination = it,
                            snackbarHostState = snackbarHostState
                        )

                    }
                }
            }
            if(!isConnected) {
                NoInternetOverlay()
            }
        }
    }


}