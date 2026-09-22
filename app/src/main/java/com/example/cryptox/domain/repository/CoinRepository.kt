package com.example.cryptox.domain.repository

import com.example.cryptox.data.remote.dto.CoinDetailDto
import com.example.cryptox.data.remote.dto.CoinDto

interface CoinRepository {
    suspend fun getCoinDetails(coin: String): CoinDetailDto
    suspend fun getCoinPrice(coin: String): CoinDto
    suspend fun getPrices(): List<CoinDto>
}