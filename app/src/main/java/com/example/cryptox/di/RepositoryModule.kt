package com.example.cryptox.di

import com.example.cryptox.common.exchange.CurrencyManager
import com.example.cryptox.data.remote.Api
import com.example.cryptox.data.remote.CurrencyExchangeApi
import com.example.cryptox.data.repository.CoinRepositoryImpl
import com.example.cryptox.data.repository.CurrencyExchangeRepositoryImpl
import com.example.cryptox.domain.repository.CoinRepository
import com.example.cryptox.domain.repository.CurrencyExchangeRepository
import com.example.cryptox.domain.repository.UserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideCoinRepository(api: Api): CoinRepository {
        return CoinRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideCurrencyExchangeRepository(
        api: CurrencyExchangeApi,
        currencyManager: CurrencyManager,
        userPreferencesRepository: UserPreferencesRepository,
    ): CurrencyExchangeRepository {
        return CurrencyExchangeRepositoryImpl(api, currencyManager, userPreferencesRepository)
    }

    @Provides
    @Singleton
    fun provideCurrencyManager(
    ): CurrencyManager {
        return CurrencyManager()
    }
}