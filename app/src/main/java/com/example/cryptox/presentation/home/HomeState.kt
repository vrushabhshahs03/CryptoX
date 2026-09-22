package com.example.cryptox.presentation.home

import com.example.cryptox.domain.model.Coin
import com.example.cryptox.domain.model.User
import com.example.cryptox.presentation.auth.common.AuthProvider

data class HomeState(
    val isActive: Boolean = true,
    val isLoading: Boolean = false,
    val error: String = "",
    val coins: List<Coin> = emptyList(),
    val user: User? = null,
    val providers: List<AuthProvider> = emptyList(),
    val favoriteCoinIds: Set<String> = emptySet(),
)