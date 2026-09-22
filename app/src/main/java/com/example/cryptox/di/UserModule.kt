package com.example.cryptox.di

import com.example.cryptox.data.repository.UserRepositoryImpl
import com.example.cryptox.domain.repository.UserRepository
import com.example.cryptox.domain.use_case.user.AddFavoriteUseCase
import com.example.cryptox.domain.use_case.user.AddPortfolioItemUseCase
import com.example.cryptox.domain.use_case.user.CreateUserUseCase
import com.example.cryptox.domain.use_case.user.DeleteUserUseCase
import com.example.cryptox.domain.use_case.user.GetUserUseCase
import com.example.cryptox.domain.use_case.user.RemoveFavoriteUseCase
import com.example.cryptox.domain.use_case.user.RemovePortfolioItemUseCase
import com.example.cryptox.domain.use_case.user.UserExistsUseCase
import com.example.cryptox.domain.use_case.user.UserUseCases
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {
    @Provides
    @Singleton
    fun provideUserRepository(
        firestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth,
    ): UserRepository {
        return UserRepositoryImpl(
            firebaseStore = firestore,
            firebaseAuth = firebaseAuth
        )
    }

    @Provides
    @Singleton
    fun provideUserUseCases(
        repository: UserRepository,
    ): UserUseCases {
        return UserUseCases(
            createUser = CreateUserUseCase(repository),
            getUser = GetUserUseCase(repository),
            addFavorite = AddFavoriteUseCase(repository),
            removeFavorite = RemoveFavoriteUseCase(repository),
            addPortfolioItem = AddPortfolioItemUseCase(repository),
            removePortfolioItem = RemovePortfolioItemUseCase(repository),
            deleteUser = DeleteUserUseCase(repository),
            userExists = UserExistsUseCase(repository)
        )
    }
}