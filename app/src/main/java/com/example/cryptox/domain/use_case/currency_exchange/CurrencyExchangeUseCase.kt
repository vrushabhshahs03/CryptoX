package com.example.cryptox.domain.use_case.currency_exchange

import com.example.cryptox.domain.repository.CurrencyExchangeRepository
import javax.inject.Inject

class CurrencyExchangeUseCase @Inject constructor(
    private val repository: CurrencyExchangeRepository,
) {
    suspend operator fun invoke() {
        repository.getExchangeRates()
    }
}