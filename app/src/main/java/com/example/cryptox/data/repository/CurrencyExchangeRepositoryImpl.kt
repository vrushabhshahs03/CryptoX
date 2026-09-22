package com.example.cryptox.data.repository

import android.util.Log
import com.example.cryptox.common.exchange.CurrencyManager
import com.example.cryptox.data.remote.CurrencyExchangeApi
import com.example.cryptox.data.remote.dto.CurrencyExchangeDto
import com.example.cryptox.data.remote.dto.toCurrencyExchange
import com.example.cryptox.domain.repository.CurrencyExchangeRepository
import com.example.cryptox.domain.repository.UserPreferencesRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class CurrencyExchangeRepositoryImpl @Inject constructor(
    private val api: CurrencyExchangeApi,
    private val currencyManager: CurrencyManager,
    private val userPreferencesRepository: UserPreferencesRepository,
): CurrencyExchangeRepository {
    override suspend fun getExchangeRates() {
        val currencyExchange = api.getExchangeRates().toCurrencyExchange()
        currencyManager.rates = currencyExchange.rates + mapOf("USD" to 1.0)
    }
}