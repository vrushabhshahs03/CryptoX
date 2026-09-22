package com.example.cryptox.presentation.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptox.common.exchange.CurrencyInitializer
import com.example.cryptox.common.exchange.CurrencyManager
import com.example.cryptox.common.exchange.CurrencyProvider
import com.example.cryptox.common.exchange.CurrencyProvider.currencyManager
import com.example.cryptox.domain.connectivity.ConnectivityObserver
import com.example.cryptox.domain.model.CurrencyCode
import com.example.cryptox.domain.use_case.auth.AuthUseCases
import com.example.cryptox.domain.use_case.onboarding.ReadOnBoardingStateUseCase
import com.example.cryptox.domain.use_case.onboarding.SaveCurrencyUseCase
import com.example.cryptox.domain.use_case.onboarding.SaveOnBoardingStateUseCase
import com.example.cryptox.presentation.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    connectivityObserver: ConnectivityObserver,
    private val saveOnBoardingStateUseCase: SaveOnBoardingStateUseCase,
    private val readOnBoardingStateUseCase: ReadOnBoardingStateUseCase,
    private val authUseCases: AuthUseCases,
    private val currencyManager: CurrencyManager,
    private val saveCurrencyUseCase: SaveCurrencyUseCase,
): ViewModel() {
    private val _eventFlow = MutableSharedFlow<MainUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()
    private val _onBoardingState = mutableStateOf(false)
    val onBoardingState: State<Boolean> = _onBoardingState

    init {
        CurrencyProvider.currencyManager = currencyManager
        viewModelScope.launch {
            val isOnboardingCompleted =
                readOnBoardingStateUseCase().first()

            _onBoardingState.value = isOnboardingCompleted

            when {
                !authUseCases.isLoggedIn() -> {
                    _eventFlow.emit(MainUiEvent.StartDestination(Screens.Login.route))
                }
                !isOnboardingCompleted -> {
                    _eventFlow.emit(MainUiEvent.StartDestination(Screens.Onboarding.route))
                }
                else -> {
                    _eventFlow.emit(MainUiEvent.StartDestination(Screens.Home.route))
                }
            }
        }
    }

    val isConnected: StateFlow<Boolean> =
        connectivityObserver.isConnected.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    fun saveOnBoardingState(completed: Boolean) {
        viewModelScope.launch {
            saveOnBoardingStateUseCase(completed)
        }
        viewModelScope.launch {
            saveCurrencyUseCase(CurrencyCode.USD)
        }
    }
}