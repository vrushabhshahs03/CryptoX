package com.example.cryptox.presentation.portfolio

import com.example.cryptox.domain.model.Coin
import com.example.cryptox.domain.model.PortfolioItem

data class PortfolioState(
    val isActive: Boolean = true,
    val isLoading: Boolean = false,
    val portfolioItems: Map<String, PortfolioItem> = emptyMap(),
    val prices: List<Coin> = emptyList(),
    val portfolioPrice: Double = 0.0,
    val error: String = " ",
)