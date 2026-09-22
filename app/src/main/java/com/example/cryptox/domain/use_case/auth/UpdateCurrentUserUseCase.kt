package com.example.cryptox.domain.use_case.auth

import android.net.Uri
import com.example.cryptox.common.Resource
import com.example.cryptox.domain.repository.AuthRepository
import javax.inject.Inject

class UpdateCurrentUserUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(name: String?, imageUri: Uri?): Resource<Unit> {
        return repository.updateCurrentUser(name = name, imageUri = imageUri)
    }
}