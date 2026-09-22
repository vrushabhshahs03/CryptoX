package com.example.cryptox.data.repository

import com.example.cryptox.data.remote.Api
import com.example.cryptox.data.remote.dto.CoinDetailDto
import com.example.cryptox.data.remote.dto.CoinDto
import com.example.cryptox.domain.repository.CoinRepository
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    val api: Api,
): CoinRepository {

    override suspend fun getCoinDetails(coin: String): CoinDetailDto {
        return api.getCoinDetail(coin)
    }

    override suspend fun getCoinPrice(coin: String): CoinDto {
        val price = api.getCoinPrice(coin)
        return price
    }

    override suspend fun getPrices(): List<CoinDto> {
        return api.getPrices()
    }
}