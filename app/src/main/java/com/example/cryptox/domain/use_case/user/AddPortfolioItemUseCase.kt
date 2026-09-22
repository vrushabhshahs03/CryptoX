package com.example.cryptox.domain.use_case.user

import com.example.cryptox.common.Resource
import com.example.cryptox.domain.model.PortfolioItem
import com.example.cryptox.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddPortfolioItemUseCase @Inject constructor(
    private val repository: UserRepository,
) {
    operator fun invoke(portfolioItem: PortfolioItem): Flow<Resource<Unit>> {
        return repository.addPortfolioItem(portfolioItem)
    }
}