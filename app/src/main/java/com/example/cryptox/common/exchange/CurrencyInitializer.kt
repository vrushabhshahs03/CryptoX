package com.example.cryptox.common.exchange

import com.example.cryptox.domain.repository.CurrencyExchangeRepository
import com.example.cryptox.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyInitializer @Inject constructor(
    private val repository: UserPreferencesRepository,
    private val currencyManager: CurrencyManager,
    private val currencyExchangeRepository: CurrencyExchangeRepository,
) {
    suspend fun initialize() {
        currencyManager.selectedCurrency = repository.getSelectedCurrency().first()
        currencyExchangeRepository.getExchangeRates()

    }
}