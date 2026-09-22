package com.example.cryptox.presentation.portfolio

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.cryptox.common.Resource
import com.example.cryptox.domain.model.PortfolioItem
import com.example.cryptox.domain.use_case.coins.get_prices.GetPricesUseCase
import com.example.cryptox.domain.use_case.currency_exchange.CurrencyExchangeUseCase
import com.example.cryptox.domain.use_case.user.UserUseCases
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@HiltViewModel
class PortfolioViewModel @Inject constructor(
    private val userUseCases: UserUseCases,
    private val getPricesUseCase: GetPricesUseCase,
    private val currencyExchangeUseCase: CurrencyExchangeUseCase,
): ViewModel() {
    private val _state = mutableStateOf(PortfolioState())
    val state: State<PortfolioState> = _state
    var cachedCoinsList: Map<String, String> = emptyMap()
    private val _eventFlow = MutableSharedFlow<PortfolioUiEvent>()
    val eventFlow: SharedFlow<PortfolioUiEvent> = _eventFlow

    private companion object {
        const val TAG = "PortfolioViewModel"
    }

    init {
        getPrices()
    }

    fun refresh() {
        getPrices()
    }

    private fun getPortfolio() {
        viewModelScope.launch {
            userUseCases.getUser().collect { resource ->
                when(resource) {
                    is Resource.Loading<*> -> {}
                    is Resource.Success<*> -> {
                        resource.data?.let { user ->
                            _state.value = _state.value.copy(
                                isLoading = false,
                                portfolioItems = user.portfolio
                            )
                        }
                        calculatePortfolio()
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
                        resource.data?.let { list ->
                            if(cachedCoinsList.isEmpty()) {
                                cachedCoinsList = list.associate { coin->
                                    coin.coinId to coin.name + " (" + coin.symbol + ")"

                                }
                            //
                            //                                list.map { coinPrice ->
//                                    coinPrice.name + " (" + coinPrice.symbol + ")"
//                                }
                            }
                            _state.value = _state.value.copy(
                                prices = list
                            )
                        }
                        getPortfolio()
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

    private fun calculatePortfolio() {
        val state = _state.value

        if(state.portfolioItems.isEmpty()) {
            _state.value = _state.value.copy(
                portfolioPrice = 0.0
            )
        }
        val priceMap = state.prices.associateBy { it.coinId }
        val total = state.portfolioItems.entries.sumOf { (id, coinPrice) ->
            val price = priceMap[id]?.price?.uSD?.price ?: 0.0
            price * coinPrice.quantity
        }

        _state.value = _state.value.copy(
            portfolioPrice = total
        )
    }

    fun removePortfolio(symbol: String) {
        viewModelScope.launch {
            userUseCases.removePortfolioItem(symbol).collect { resource ->
                when(resource) {
                    is Resource.Loading<*> -> {}
                    is Resource.Success<*> -> {
                        _eventFlow.emit(PortfolioUiEvent.ItemDeleted)
                        _eventFlow.emit(
                            PortfolioUiEvent.ShowSnackBar(
                                message = "Coin removed from Portfolio!"
                            )
                        )
                    }
                    is Resource.Error<*> -> {
                        Log.d(TAG, "removePortfolio " + resource.message)

                        _eventFlow.emit(
                            PortfolioUiEvent.ShowToast(
                                message = "Failed to remove item from Portfolio!"
                            )
                        )
                    }
                }
            }
        }
    }

    fun addPortfolioItem(portFolioItem: PortfolioItem) {
        viewModelScope.launch {
            userUseCases.addPortfolioItem(portFolioItem).collect { resource ->
                when(resource) {
                    is Resource.Loading<*> -> {
                        _eventFlow.emit(PortfolioUiEvent.ItemAddedLoading(isLoading = true))
                    }
                    is Resource.Success<*> -> {
                        _eventFlow.emit(PortfolioUiEvent.ItemAddedLoading(isLoading = false))
                        _eventFlow.emit(PortfolioUiEvent.ItemAdded)
                        _eventFlow.emit(
                            PortfolioUiEvent.ShowSnackBar(
                                message = "Coin added to Portfolio!"
                            )
                        )
                    }
                    is Resource.Error<*> -> {
                        Log.d(TAG, "addPortfolio " + resource.message)
                        _eventFlow.emit(PortfolioUiEvent.ItemAddedLoading(isLoading = false))
                        _eventFlow.emit(
                            PortfolioUiEvent.ShowToast(
                                message = "Failed to add item to Portfolio!"
                            )
                        )
                    }
                }
            }
        }
    }
}