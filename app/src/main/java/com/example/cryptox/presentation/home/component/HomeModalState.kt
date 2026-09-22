package com.example.cryptox.presentation.home.component

sealed class HomeModalState() {
    object Hidden: HomeModalState()
    object EditProfile: HomeModalState()
    object ChangePassword: HomeModalState()
}