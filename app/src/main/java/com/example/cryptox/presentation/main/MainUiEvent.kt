package com.example.cryptox.presentation.main

sealed class MainUiEvent() {
    data class StartDestination(val destination: String): MainUiEvent()
}