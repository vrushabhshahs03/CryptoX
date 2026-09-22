package com.example.cryptox.domain.use_case.auth

import com.example.cryptox.common.Resource
import com.example.cryptox.domain.model.AuthUser
import com.example.cryptox.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    operator fun invoke(
        email: String,
        password: String,
    ): Flow<Resource<AuthUser>> {
        return repository.login(email, password)
    }
}