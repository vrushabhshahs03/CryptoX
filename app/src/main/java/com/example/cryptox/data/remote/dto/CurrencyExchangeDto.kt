package com.example.cryptox.data.remote.dto

import com.example.cryptox.domain.model.CurrencyExchange
import com.google.gson.annotations.SerializedName

data class CurrencyExchangeDto(
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("base")
    val base: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("rates")
    val rates: Map<String, Double>,
)

fun CurrencyExchangeDto.toCurrencyExchange(): CurrencyExchange {
    return CurrencyExchange(
        rates = rates
    )
}