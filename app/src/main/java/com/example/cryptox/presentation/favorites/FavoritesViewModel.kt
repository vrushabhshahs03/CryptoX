package com.example.cryptox.presentation.favorites

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptox.common.Resource
import com.example.cryptox.common.exchange.CurrencyProvider.currencyManager
import com.example.cryptox.common.exchange.toSelectedCurrencyString
import com.example.cryptox.domain.use_case.coins.get_prices.GetPricesUseCase
import com.example.cryptox.domain.use_case.currency_exchange.CurrencyExchangeUseCase
import com.example.cryptox.domain.use_case.user.UserUseCases
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private var getPricesUseCase: GetPricesUseCase,
    private var userUseCases: UserUseCases,
    private val currencyExchangeUseCase: CurrencyExchangeUseCase,
): ViewModel() {
    private val _state = mutableStateOf(FavoritesState())
    val state: State<FavoritesState> = _state
    private val _eventFlow = MutableSharedFlow<FavoriteUiEvent>()
    val eventFlow = _eventFlow

    private companion object {
        const val TAG = "FavoriteViewModel"
    }

    init {
        getPrices()
    }

    fun refresh() {
        getPrices()
    }

    fun isCurrentUserActive() {
        FirebaseAuth.getInstance().currentUser?.reload()
            ?.addOnSuccessListener {
                _state.value = _state.value.copy(
                    isActive = true
                )
            }
            ?.addOnFailureListener {
                _state.value = _state.value.copy(
                    isActive = false
                )
            }
    }

    private fun getFavorites() {
        viewModelScope.launch {
            userUseCases.getUser().collect { resource ->
                when(resource) {
                    is Resource.Loading<*> -> {
//                        _state.value = _state.value.copy(
//                            isLoading = true
//                        )
                    }
                    is Resource.Success<*> -> {
                        resource.data?.let {
                            val favorites = it.favorites.keys.toSet()
                            _state.value = _state.value.copy(
                                isLoading = false,
                                favorites = favorites
                            )
                        }
                    }
                    is Resource.Error<*> -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = resource.message ?: "An unexpected error has occurred!"
                        )
                    }
                }
            }
        }
    }

    private fun getPrices() {
        viewModelScope.launch {
            currencyExchangeUseCase()
            getPricesUseCase().collect { resource ->
                when(resource) {
                    is Resource.Loading<*> -> {
                        _state.value = _state.value.copy(
                            isLoading = true
                        )
                    }
                    is Resource.Success<*> -> {
                        resource.data?.let {
                            _state.value = _state.value.copy(
                                prices = it
                            )
                        }
                        getFavorites()
                    }
                    is Resource.Error<*> -> {
                        Log.d(TAG, "getPrices" + resource.message)
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = resource.message ?: "An unexpected error has occurred!"
                        )
                    }
                }
            }
        }
    }

    fun removeFavorite(symbol: String) {
        viewModelScope.launch {
            userUseCases.removeFavorite(symbol).collect { resource ->
                when(resource) {
                    is Resource.Loading<*> -> {}
                    is Resource.Success<*> -> {
                        _eventFlow.emit(FavoriteUiEvent.ShowSnackbar("Coin removed from Favorites!"))
                    }
                    is Resource.Error<*> -> {
                        Log.d(TAG, "removeFavorite" + resource.message)
                    }
                }
            }
        }
    }
}