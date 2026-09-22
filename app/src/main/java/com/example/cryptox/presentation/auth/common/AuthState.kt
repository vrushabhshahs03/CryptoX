package com.example.cryptox.presentation.auth.common

data class AuthState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String = "",
)