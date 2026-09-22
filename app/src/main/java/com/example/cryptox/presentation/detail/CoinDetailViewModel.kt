package com.example.cryptox.presentation.detail

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptox.common.Constants
import com.example.cryptox.common.Resource
import com.example.cryptox.domain.use_case.coins.get_coin_detail.GetCoinDetailUseCase
import com.example.cryptox.domain.use_case.coins.get_coin_price.GetCoinPriceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import com.example.cryptox.BuildConfig
import com.example.cryptox.domain.use_case.currency_exchange.CurrencyExchangeUseCase
import kotlinx.coroutines.launch

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    private val getCoinDetailUseCase: GetCoinDetailUseCase,
    private val getCoinPriceUseCase: GetCoinPriceUseCase,
    private val currencyExchangeUseCase: CurrencyExchangeUseCase,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val _state = mutableStateOf(CoinDetailState())
    val state: State<CoinDetailState> = _state

    init {
        savedStateHandle.get<String>(Constants.COIN_KEY)?.let { coin ->
            getCoinDetail(coin)
        }
    }

    fun refresh() {
        savedStateHandle.get<String>(Constants.COIN_KEY)?.let { coin ->
            getCoinDetail(coin)
        }
    }

    fun shareCoin(context: Context, coinId: String) {
        val link = "https://${BuildConfig.DEEP_LINK_HOST}/coin/$coinId"
        val shareText = """
        Check out this coin on CryptoX 📈
        
        View live price, charts, market cap, and more.
        
        $link
    """.trimIndent()
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        context.startActivity(
            Intent.createChooser(intent, "Share Coin via")
        )
    }

    private fun getCoinDetail(coin: String) {
        viewModelScope.launch {
            currencyExchangeUseCase()

            getCoinDetailUseCase(coin).collect { result ->
                when(result) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true
                        )
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = result.message
                                ?: "An unexpected error has occurred!"
                        )
                    }
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            coin = result.data
                        )

                        getCoinPriceUseCase(coin).collect { priceResult ->
                            when(priceResult) {
                                is Resource.Loading -> {
                                    _state.value = _state.value.copy(
                                        isLoading = true
                                    )
                                }
                                is Resource.Error -> {
                                    _state.value = _state.value.copy(
                                        isLoading = false,
                                        error = priceResult.message
                                            ?: "An unexpected error has occurred!"
                                    )
                                }
                                is Resource.Success -> {
                                    _state.value = _state.value.copy(
                                        isLoading = false,
                                        coinPrice = priceResult.data
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
//    private fun getCoinDetail(coin: String) {
//        viewModelScope.launch {
//            currencyExchangeUseCase()
//            getCoinDetailUseCase(coin).onEach { result ->
//                when(result) {
//                    is Resource.Success<*> -> {
//                        _state.value = _state.value.copy(
//                            coin = result.data
//                        )
//
//                        getCoinPriceUseCase(coin).onEach { priceResult ->
//                            when(priceResult) {
//                                is Resource.Loading<*> -> {}
//                                is Resource.Success<*> -> {
//                                    _state.value = _state.value.copy(
//                                        isLoading = false,
//                                        coinPrice = priceResult.data
//                                    )
//                                }
//                                is Resource.Error<*> -> {
//                                    _state.value = _state.value.copy(
//                                        isLoading = false,
//                                        error = priceResult.message ?: "An unexpected error has occurred!"
//                                    )
//                                }
//                            }
//                        }.launchIn(viewModelScope)
//                    }
//                    is Resource.Loading<*> -> {
//                        _state.value = _state.value.copy(
//                            isLoading = true
//                        )
//                    }
//                    is Resource.Error<*> -> {
//                        _state.value = _state.value.copy(
//                            isLoading = false,
//                            error = result.message ?: "An unexpected error has occurred!"
//                        )
//                    }
//                }
//            }.launchIn(this)
//        }
//    }
}