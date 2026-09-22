package com.example.cryptox.common.utils.extensions

fun String.toReadableDate(): String {
    val instant = java.time.Instant.parse(this)
    val formatter = java.time.format.DateTimeFormatter.ofPattern(
        "dd MMM yyyy"
    ).withZone(java.time.ZoneId.systemDefault())

    return formatter.format(instant)
}