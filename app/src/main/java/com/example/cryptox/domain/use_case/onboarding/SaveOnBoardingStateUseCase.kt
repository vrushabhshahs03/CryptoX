package com.example.cryptox.domain.use_case.onboarding

import com.example.cryptox.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class SaveOnBoardingStateUseCase @Inject constructor(
    val repository: UserPreferencesRepository,
) {
    suspend operator fun invoke(completed: Boolean) {
        repository.saveOnBoardingState(completed = completed)
    }
}