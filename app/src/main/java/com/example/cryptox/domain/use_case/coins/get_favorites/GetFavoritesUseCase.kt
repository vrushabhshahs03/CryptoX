package com.example.cryptox.domain.use_case.coins.get_favorites

import com.example.cryptox.common.Resource
import com.example.cryptox.domain.model.AuthUser
import com.example.cryptox.domain.model.User
import com.example.cryptox.domain.repository.CoinRepository
import com.example.cryptox.domain.use_case.user.UserUseCases
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val userUseCases: UserUseCases,
) {
    operator fun invoke(): Flow<Resource<User>> {
        return userUseCases.getUser()
    }
}