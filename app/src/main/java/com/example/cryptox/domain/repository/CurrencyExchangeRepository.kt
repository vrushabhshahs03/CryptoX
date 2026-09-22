package com.example.cryptox.domain.repository

interface CurrencyExchangeRepository {
    suspend fun getExchangeRates()
}