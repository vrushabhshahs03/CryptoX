package com.example.cryptox.data.repository

import com.example.cryptox.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.example.cryptox.data.local.datastore.UserPreferencesDataSource
import com.example.cryptox.domain.model.CurrencyCode

class UserPreferencesRepositoryImpl @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
): UserPreferencesRepository {
    override suspend fun saveOnBoardingState(completed: Boolean) {
        userPreferencesDataSource.saveOnBoardingState(completed = completed)
    }

    override fun readOnBoardingState(): Flow<Boolean> {
        return userPreferencesDataSource.readOnBoardingState()
    }

    override suspend fun setSelectedCurrency(currency: CurrencyCode) {
        userPreferencesDataSource.setSelectedCurrency(currency)
    }

    override fun getSelectedCurrency(): Flow<CurrencyCode> {
        return userPreferencesDataSource.getSelectedCurrency()
    }
}