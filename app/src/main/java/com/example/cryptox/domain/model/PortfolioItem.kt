package com.example.cryptox.domain.model

data class PortfolioItem(
    val coinId: String ="",
    val symbol: String = "",
    val quantity: Int = 0,
    val averageBuyPrice: Double = 0.0,
)