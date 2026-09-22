package com.example.cryptox.data.remote

import com.example.cryptox.data.remote.dto.CurrencyExchangeDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyExchangeApi {
    @GET("latest")
    suspend fun getExchangeRates(
        @Query("from") from: String = "USD",
    ): CurrencyExchangeDto
}