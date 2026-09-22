package com.example.cryptox.di

import com.example.cryptox.data.local.datastore.UserPreferencesDataSource
import com.example.cryptox.data.repository.UserPreferencesRepositoryImpl
import com.example.cryptox.domain.repository.UserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(
        onBoardingPreferencesDataSource: UserPreferencesDataSource
    ): UserPreferencesRepository {
        return UserPreferencesRepositoryImpl(onBoardingPreferencesDataSource)
    }
}