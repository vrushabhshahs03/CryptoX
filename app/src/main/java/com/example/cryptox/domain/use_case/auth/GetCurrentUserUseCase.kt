package com.example.cryptox.domain.use_case.auth

import com.example.cryptox.domain.model.AuthUser
import com.example.cryptox.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    operator fun invoke(): AuthUser? {
        return repository.getCurrentUser()
    }
}