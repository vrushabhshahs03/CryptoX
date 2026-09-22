package com.example.cryptox.domain.use_case.onboarding

import com.example.cryptox.domain.model.CurrencyCode
import com.example.cryptox.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadCurrencyUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(): Flow<CurrencyCode> {
        return userPreferencesRepository.getSelectedCurrency()
    }
}