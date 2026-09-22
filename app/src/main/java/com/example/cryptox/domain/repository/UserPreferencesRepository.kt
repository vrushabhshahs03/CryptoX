package com.example.cryptox.domain.repository

import com.example.cryptox.domain.model.CurrencyCode
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    suspend fun saveOnBoardingState(completed: Boolean)
    fun readOnBoardingState(): Flow<Boolean>

    suspend fun setSelectedCurrency(currency: CurrencyCode)
    fun getSelectedCurrency(): Flow<CurrencyCode>
}