package com.example.cryptox.domain.repository

import android.net.Uri
import com.example.cryptox.common.Resource

interface StorageRepository {
    suspend fun uploadProfileImage(
        imageUri: Uri,
    ): Resource<String>

    suspend fun deleteProfileImage(
        userId: String,
    ): Resource<Unit>
}