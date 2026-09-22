package com.example.cryptox.domain.use_case.auth

import com.example.cryptox.common.Resource
import com.example.cryptox.domain.repository.AuthRepository
import com.example.cryptox.presentation.auth.common.AuthProvider
import javax.inject.Inject

class GetProvidersUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    operator fun invoke(): Resource<List<AuthProvider>> {
        return repository.getProviders()
    }
}