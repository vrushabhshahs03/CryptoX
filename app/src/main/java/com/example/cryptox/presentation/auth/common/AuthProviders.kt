package com.example.cryptox.presentation.auth.common

sealed class AuthProvider() {
    object EmailPassword: AuthProvider()
    object Google: AuthProvider()
    object Facebook: AuthProvider()
    object Apple: AuthProvider()
    object Phone: AuthProvider()
    object Anonymous: AuthProvider()
    object Unknown: AuthProvider()
}