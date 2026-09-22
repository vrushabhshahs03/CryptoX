package com.example.cryptox.domain.use_case.user

data class UserUseCases(
    val createUser: CreateUserUseCase,
    val getUser: GetUserUseCase,
    val addFavorite: AddFavoriteUseCase,
    val removeFavorite: RemoveFavoriteUseCase,
    val addPortfolioItem: AddPortfolioItemUseCase,
    val removePortfolioItem: RemovePortfolioItemUseCase,
    val deleteUser: DeleteUserUseCase,
    val userExists: UserExistsUseCase,
)