package com.example.cryptox.domain.use_case.auth

import android.R.attr.name
import com.example.cryptox.common.Resource
import com.example.cryptox.domain.model.AuthUser
import com.example.cryptox.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    operator fun invoke(
        email: String,
        password: String,
        name: String,
    ): Flow<Resource<AuthUser>> {
        return repository.signUp(email, password, name)
    }
}