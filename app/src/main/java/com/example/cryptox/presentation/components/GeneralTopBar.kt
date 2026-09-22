package com.example.cryptox.presentation.components


import androidx.activity.compose.LocalActivity
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralTopBar(

    modifier: Modifier = Modifier,
    title: String,
    showBack: Boolean = false,
) {
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current
        ?.onBackPressedDispatcher

    TopAppBar(
        title = {
            Text(
                title,
                color = Color.White
            )
        },
        navigationIcon = {
            if(showBack) {
                IconButton(
                    onClick = {
                        backDispatcher?.onBackPressed()
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint=Color.White
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Blue,
            actionIconContentColor = Color.White
        ),
    )
}