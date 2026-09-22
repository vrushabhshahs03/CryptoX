package com.example.cryptox.domain.model

import android.net.Uri

data class User(
    val uid: String,
    val email: String?,
    val name: String?,
    val phoneNumber: String?,
    val photoUri: String?,
    val favorites: Map<String, Boolean>,
    val portfolio: Map<String, PortfolioItem>,
)