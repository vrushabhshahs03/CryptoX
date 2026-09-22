package com.example.cryptox.domain.use_case.auth

import com.example.cryptox.domain.repository.AuthRepository
import javax.inject.Inject

class IsUserLoggedInUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    operator fun invoke(): Boolean {
        return repository.isUserLoggedIn()
    }
}