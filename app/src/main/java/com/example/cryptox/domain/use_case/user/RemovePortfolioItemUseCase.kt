package com.example.cryptox.domain.use_case.user

import com.example.cryptox.common.Resource
import com.example.cryptox.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemovePortfolioItemUseCase @Inject constructor(
    private val repository: UserRepository,
) {
    operator fun invoke(symbol: String): Flow<Resource<Unit>> {
        return repository.removePortfolioItem(symbol)
    }
}