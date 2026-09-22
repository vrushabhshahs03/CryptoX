package com.example.cryptox.data.local.datastore

import android.content.Context
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.cryptox.common.utils.extensions.dataStore
import com.example.cryptox.data.local.datastore.UserPreferencesDataSource.PreferencesKeys.SELECTED_CURRENCY
import com.example.cryptox.domain.model.CurrencyCode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private object PreferencesKeys {
        val ONBOARDING_KEY = booleanPreferencesKey("on_boarding_completed")
        val SELECTED_CURRENCY = stringPreferencesKey("selected_currency")
    }

    suspend fun saveOnBoardingState(completed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ONBOARDING_KEY] = completed
        }
    }

    suspend fun setSelectedCurrency(currency: CurrencyCode) {
        context.dataStore.edit { preferences ->
            preferences[SELECTED_CURRENCY] = currency.name
        }
    }

    fun getSelectedCurrency(): Flow<CurrencyCode> {
        return context.dataStore.data
            .catch { exception ->
                if(exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                CurrencyCode.valueOf(
                    preferences[SELECTED_CURRENCY] ?: CurrencyCode.USD.name
                )
            }
    }

    fun readOnBoardingState(): Flow<Boolean> {
        return context.dataStore.data
            .catch { exception ->
                if(exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[PreferencesKeys.ONBOARDING_KEY] ?: false
            }
    }
}