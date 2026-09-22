package com.example.cryptox.domain.model

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import kotlin.String

data class AuthUser(
    val uid: String,
    val email: String?,
    val displayName: String?,
    val isEmailVerified: Boolean,
    val phoneNumber: String?,
    val photoUrl: Uri?,
    val tenantId: String?,
)

fun FirebaseUser.toAuthUser(): AuthUser {
    return AuthUser(
        uid = uid,
        email = email,
        displayName = displayName,
        isEmailVerified = isEmailVerified,
        phoneNumber = phoneNumber,
        photoUrl = photoUrl,
        tenantId = tenantId
    )
}

fun AuthUser.toUser(): User {
    return User(
        uid = uid,
        email = email,
        name = displayName,
        phoneNumber = phoneNumber,
        photoUri = photoUrl.toString(),
        favorites = emptyMap(),
        portfolio = emptyMap()
    )
}