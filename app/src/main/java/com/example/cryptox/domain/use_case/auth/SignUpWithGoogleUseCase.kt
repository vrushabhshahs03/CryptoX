package com.example.cryptox.domain.use_case.auth

import com.example.cryptox.common.Resource
import com.example.cryptox.domain.model.AuthUser
import com.example.cryptox.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignUpWithGoogleUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
     operator fun invoke(idToken: String): Flow<Resource<AuthUser>> {
        return repository.signUpWithGoogle(idToken)
    }
}