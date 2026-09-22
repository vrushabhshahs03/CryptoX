package com.example.cryptox.data.remote.dto

import android.net.Uri
import com.example.cryptox.domain.model.PortfolioItem
import com.example.cryptox.domain.model.User

data class UserDto(
    val uid: String = "",
    val email: String? = "",
    val name: String? = "",
    val phoneNumber: String? = null,
    val photoUri: String? = null,
    val favorites: Map<String, Boolean> = emptyMap(),
    val portfolio: Map<String, PortfolioItem> = emptyMap(),
)

fun UserDto.toUser(): User {
    return User(
        uid = uid,
        email = email,
        name = name,
        phoneNumber = phoneNumber,
        photoUri = photoUri,
        favorites = favorites,
        portfolio = portfolio
    )
}

fun User.toUserDto(): UserDto {
    return UserDto(
        uid = uid,
        email = email,
        name = name ?: "",
        phoneNumber = phoneNumber,
        photoUri = photoUri,
        favorites = favorites,
        portfolio = portfolio
    )
}