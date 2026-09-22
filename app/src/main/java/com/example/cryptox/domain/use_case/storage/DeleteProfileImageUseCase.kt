package com.example.cryptox.domain.use_case.storage

import androidx.compose.runtime.retain.retain
import com.example.cryptox.common.Resource
import com.example.cryptox.domain.repository.StorageRepository
import javax.inject.Inject

class DeleteProfileImageUseCase @Inject constructor(
    private val repository: StorageRepository,
) {
    suspend operator fun invoke(uid: String): Resource<Unit> {
        return repository.deleteProfileImage(uid)
    }
}