package com.example.cryptox.presentation.detail

import com.example.cryptox.domain.model.CoinDetail
import com.example.cryptox.domain.model.Coin

data class CoinDetailState(
    val isLoading: Boolean = false,
    val coin: CoinDetail?=null,
    val coinPrice: Coin?=null,
    val error: String = "",
)