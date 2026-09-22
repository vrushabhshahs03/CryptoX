package com.example.cryptox.presentation.favorites

sealed class FavoriteUiEvent {
    data class ShowSnackbar(val message: String): FavoriteUiEvent()
    data class ShowToast(val message: String): FavoriteUiEvent()
}