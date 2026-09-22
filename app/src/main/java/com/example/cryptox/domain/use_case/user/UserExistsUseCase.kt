package com.example.cryptox.domain.use_case.user

import com.example.cryptox.domain.repository.UserRepository
import javax.inject.Inject

class UserExistsUseCase @Inject constructor(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(uid: String): Boolean {
        return repository.userExists(uid)
    }
}