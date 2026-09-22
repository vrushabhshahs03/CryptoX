package com.example.cryptox.common.exchange

import com.example.cryptox.common.utils.extensions.formatNumber

fun Double.toSelectedCurrency(): Double {
    return CurrencyProvider
        .currencyManager
        .convert(this)
}

fun Double.toSelectedCurrencyString(): String {
    val manager = CurrencyProvider.currencyManager
    return manager.currencySymbol() + " " + manager.convert(this).formatNumber()
}

fun Long.toSelectedCurrencyString(): String {
    val manager = CurrencyProvider.currencyManager
    return manager.currencySymbol() + " " + manager.convert(this).formatNumber()
}