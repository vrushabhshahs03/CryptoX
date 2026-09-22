package com.example.cryptox.di

import android.content.Context
import com.example.cryptox.data.connectivity.ConnectivityObserverImpl
import com.example.cryptox.domain.connectivity.ConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConnectivityModule {
    @Provides
    @Singleton
    fun provideConnectivityObserver(
        @ApplicationContext context: Context,
    ): ConnectivityObserver {
        return ConnectivityObserverImpl(context)
    }
}