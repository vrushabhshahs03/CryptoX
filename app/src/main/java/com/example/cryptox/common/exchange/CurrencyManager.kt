package com.example.cryptox.common.exchange

import com.example.cryptox.domain.model.CurrencyCode
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyManager @Inject constructor() {
    var selectedCurrency: CurrencyCode = CurrencyCode.USD
    var rates: Map<String, Double> = mapOf("USD" to 1.0)

    fun convert(
        amountUsd: Double,
    ): Double {
        val rate = rates[selectedCurrency.name] ?: 1.0
        return amountUsd * rate
    }

    fun convert(
        amountUsd: Long,
    ): Double {
        val rate = rates[selectedCurrency.name] ?: 1.0
        return amountUsd * rate
    }

    fun convert(
        amountUsd: Int,
    ): Double {
        val rate = rates[selectedCurrency.name] ?: 1.0
        return amountUsd * rate
    }

    fun currencySymbol(): String {
        return when(selectedCurrency) {
            CurrencyCode.USD -> "$"
            CurrencyCode.INR -> "₹"
            CurrencyCode.EUR -> "€"
            CurrencyCode.GBP -> "£"
            CurrencyCode.JPY -> "JP¥"
            CurrencyCode.AUD -> "A$"
            CurrencyCode.CAD -> "C$"
            CurrencyCode.CHF -> "CHF"
            CurrencyCode.CNY -> "CN¥"
            CurrencyCode.SGD -> "S$"
        }
    }
}