package com.example.cryptox.common.utils.extensions

fun Long.formatNumber(): String {
    return when {
        this >= 1_000_000_000_000 ->
            "${"%.2f".format(this / 1_000_000_000_000.0)}T"

        this >= 1_000_000_000 ->
            "${"%.2f".format(this / 1_000_000_000.0)}B"

        this >= 1_000_000 ->
            "${"%.2f".format(this / 1_000_000.0)}M"

        this >= 1_000 ->
            "${"%.2f".format(this / 1_000.0)}K"

        else ->
            this.toString()
    }
}