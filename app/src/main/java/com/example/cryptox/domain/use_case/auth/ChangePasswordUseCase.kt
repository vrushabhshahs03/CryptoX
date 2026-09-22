package com.example.cryptox.domain.use_case.auth

import android.R.attr.password
import com.example.cryptox.common.Resource
import com.example.cryptox.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    operator fun invoke(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String,
    ): Flow<Resource<Unit>> {
        return repository.changePassword(
            currentPassword = currentPassword,
            newPassword = newPassword,
            confirmPassword = confirmPassword
        )
    }
}