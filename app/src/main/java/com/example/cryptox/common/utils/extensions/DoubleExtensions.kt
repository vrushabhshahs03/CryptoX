package com.example.cryptox.common.utils.extensions

import kotlin.math.pow
import kotlin.math.round

fun Double.toRounded(decimals: Int): Double {
    val factor = 10.0.pow(decimals)
    return round(this * factor) / factor
}

fun Double.toRoundedString(decimals: Int): String {
    return "%.${decimals}f".format(this)
}

fun Double.formatNumber(): String{
    return when {
        this >= 1_000_000_000_000 -> "${"%.2f".format(this / 1_000_000_000_000)}T"
        this >= 1_000_000_000 -> "${"%.2f".format(this / 1_000_000_000)}B"
        this >= 1_000_000 -> "${"%.2f".format(this / 1_000_000)}M"
        else -> this.toRoundedString(2)
    }
}