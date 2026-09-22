package com.example.cryptox.presentation.home

sealed class HomeUiEvent {
    data class ShowSnackBar(val message: String): HomeUiEvent()
    data class ShowToast(val message: String): HomeUiEvent()
    object PasswordChanged: HomeUiEvent()
    data class PasswordChangeScreenLoading(val isLoading: Boolean = false): HomeUiEvent()
    object ProfileUpdated: HomeUiEvent()
    data class EditScreenLoading(val isLoading: Boolean = false): HomeUiEvent()
    object AccountDeleted: HomeUiEvent()
    data class DeleteAccountScreenLoading(val isLoading: Boolean = false): HomeUiEvent()
    object LoggedOut: HomeUiEvent()
    object OTPSent: HomeUiEvent()
    object OTPVerified: HomeUiEvent()
}