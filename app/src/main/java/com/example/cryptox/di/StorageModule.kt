package com.example.cryptox.di

import com.example.cryptox.data.repository.StorageRepositoryImpl
import com.example.cryptox.domain.repository.StorageRepository
import com.google.android.play.integrity.internal.f
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {
    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Provides
    @Singleton
    fun provideStorageRepository(
        firebaseStorage: FirebaseStorage,
        firebaseAuth: FirebaseAuth,
    ): StorageRepository {
        return StorageRepositoryImpl(
            firebaseStorage,
            firebaseAuth
        )
    }
}