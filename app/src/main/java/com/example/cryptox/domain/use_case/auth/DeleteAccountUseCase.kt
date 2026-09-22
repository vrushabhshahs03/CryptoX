package com.example.cryptox.domain.use_case.auth

import com.example.cryptox.common.Resource
import com.example.cryptox.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    operator fun invoke(): Flow<Resource<Unit>> {
        return repository.deleteAccount()
    }
}