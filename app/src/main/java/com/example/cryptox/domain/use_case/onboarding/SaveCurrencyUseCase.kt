package com.example.cryptox.domain.use_case.onboarding

import com.example.cryptox.domain.model.CurrencyCode
import com.example.cryptox.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class SaveCurrencyUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(currency: CurrencyCode){
        userPreferencesRepository.setSelectedCurrency(currency)
    }
}