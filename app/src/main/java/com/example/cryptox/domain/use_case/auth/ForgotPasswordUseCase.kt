package com.example.cryptox.domain.use_case.auth

import com.example.cryptox.domain.repository.AuthRepository
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(email:String){
        repository.forgotPassword(email)
    }
}