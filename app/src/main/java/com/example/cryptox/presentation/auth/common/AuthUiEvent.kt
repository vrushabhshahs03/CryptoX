package com.example.cryptox.presentation.auth.common

import com.example.cryptox.presentation.home.HomeUiEvent

sealed class AuthUiEvent {
    data class ShowSnackBar(val message: String): AuthUiEvent()
    data class ShowToast(val message: String): AuthUiEvent()
    data class Loading(val isLoading: Boolean = false): AuthUiEvent()
    object OTPSent: AuthUiEvent()
    object OTPVerified: AuthUiEvent()
    object onSignup: AuthUiEvent()
}