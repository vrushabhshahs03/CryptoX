package com.example.cryptox.data.repository

import android.net.Uri
import android.util.Log.e
import com.example.cryptox.common.Resource
import com.example.cryptox.domain.repository.StorageRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth,
): StorageRepository {
    override suspend fun uploadProfileImage(
        imageUri: Uri,
    ): Resource<String> {
        try {
            val uid = auth.currentUser?.uid ?: return Resource.Error(message = "User not logged in!")
            val imageRef = storage.reference
                .child("profile_images/$uid.jpg")

            imageRef.putFile(imageUri).await()
            val downloadUrl = imageRef.downloadUrl.await().toString()

            return Resource.Success(data = downloadUrl)
        } catch(e: Exception) {
            return Resource.Error(message = e.localizedMessage ?: "An unexpected error has occurred!")
        }
    }

    override suspend fun deleteProfileImage(userId: String): Resource<Unit> {
        try {
            FirebaseStorage.getInstance()
                .reference
                .child("profile_images/$userId.jpg")
                .delete().await()
            return Resource.Success(Unit)
        } catch(e: Exception) {
            return Resource.Error(message = e.localizedMessage ?: "An unexpected error has occurred!")
        }
    }
}