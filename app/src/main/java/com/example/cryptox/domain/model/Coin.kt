package com.example.cryptox.domain.model

import com.example.cryptox.data.remote.dto.Quotes

data class Coin(
    val coinId: String,
    val name: String,
    val symbol: String,
    val rank: Int,
    val price: Quotes?
)