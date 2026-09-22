package com.example.cryptox.domain.use_case.user

import com.example.cryptox.domain.repository.UserRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(uid: String): Boolean {
        return repository.deleteUser(uid = uid)
    }
}