package com.example.cryptox.data.remote

import com.example.cryptox.data.remote.dto.CoinDetailDto
import com.example.cryptox.data.remote.dto.CoinDto
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {


    @GET("/v1/coins/{coin}")
    suspend fun getCoinDetail(
        @Path("coin") coin: String,
    ): CoinDetailDto

    @GET("/v1/tickers/{coin}")
    suspend fun getCoinPrice(
        @Path("coin") coin: String,
    ): CoinDto

    @GET("/v1/tickers")
    suspend fun getPrices(): List<CoinDto>


}