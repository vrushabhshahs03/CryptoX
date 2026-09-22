package com.example.cryptox.domain.use_case.onboarding

import com.example.cryptox.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadOnBoardingStateUseCase @Inject constructor(
    private val repository: UserPreferencesRepository,
) {
    operator fun invoke(): Flow<Boolean> {
        return repository.readOnBoardingState()
    }
}