package com.example.cryptox.domain.use_case.storage

import android.net.Uri
import com.example.cryptox.common.Resource
import com.example.cryptox.domain.repository.StorageRepository
import javax.inject.Inject

class UploadProfileImageUseCase @Inject constructor(
    private val repository: StorageRepository,
) {
    suspend operator fun invoke(imageUri: Uri): Resource<String> {
        return repository.uploadProfileImage(imageUri)
    }
}