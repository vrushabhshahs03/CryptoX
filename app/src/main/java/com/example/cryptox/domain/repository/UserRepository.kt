package com.example.cryptox.domain.repository

import com.example.cryptox.common.Resource
import com.example.cryptox.domain.model.PortfolioItem
import com.example.cryptox.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun createUser(user: User): Flow<Resource<Unit>>
    fun getUser(): Flow<Resource<User>>
    fun addFavorite(symbol: String): Flow<Resource<Unit>>
    fun removeFavorites(symbol: String): Flow<Resource<Unit>>
    fun addPortfolioItem(portFolioItem: PortfolioItem): Flow<Resource<Unit>>
    fun removePortfolioItem(symbol: String): Flow<Resource<Unit>>
    suspend fun userExists(uid: String): Boolean
    suspend fun deleteUser(uid: String): Boolean

}