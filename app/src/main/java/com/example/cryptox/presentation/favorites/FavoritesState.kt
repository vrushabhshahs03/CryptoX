package com.example.cryptox.presentation.favorites

import com.example.cryptox.domain.model.Coin

data class FavoritesState(
    val isActive: Boolean = true,
    val isLoading: Boolean = false,
    val favorites: Set<String> = emptySet(),
    val prices: List<Coin> = emptyList(),
    val error: String = "",
)