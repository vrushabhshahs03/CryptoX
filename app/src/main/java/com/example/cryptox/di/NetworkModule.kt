package com.example.cryptox.di

import com.example.cryptox.common.Constants
import com.example.cryptox.data.remote.Api
import com.example.cryptox.data.remote.CurrencyExchangeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideApi(): Api {
        return Retrofit.Builder()
            .baseUrl(Constants.COIN_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }

    @Provides
    @Singleton
    fun provideCurrencyExchangeApi(): CurrencyExchangeApi {
        return Retrofit.Builder()
            .baseUrl(Constants.EXCHANGE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyExchangeApi::class.java)
    }
}