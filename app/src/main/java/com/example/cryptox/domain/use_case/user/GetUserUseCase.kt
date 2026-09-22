package com.example.cryptox.domain.use_case.user

import com.example.cryptox.common.Resource
import com.example.cryptox.domain.model.User
import com.example.cryptox.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(): Flow<Resource<User>> {
        return repository.getUser()
    }
}