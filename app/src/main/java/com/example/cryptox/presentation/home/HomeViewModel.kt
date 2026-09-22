package com.example.cryptox.presentation.home

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptox.common.Resource
import com.example.cryptox.common.exchange.CurrencyInitializer
import com.example.cryptox.domain.model.Coin
import com.example.cryptox.domain.model.CurrencyCode
import com.example.cryptox.domain.model.User
import com.example.cryptox.domain.use_case.auth.AuthUseCases
import com.example.cryptox.domain.use_case.coins.get_prices.GetPricesUseCase
import com.example.cryptox.domain.use_case.currency_exchange.CurrencyExchangeUseCase
import com.example.cryptox.domain.use_case.onboarding.ReadCurrencyUseCase
import com.example.cryptox.domain.use_case.onboarding.SaveCurrencyUseCase
import com.example.cryptox.domain.use_case.storage.DeleteProfileImageUseCase
import com.example.cryptox.domain.use_case.storage.UploadProfileImageUseCase
import com.example.cryptox.domain.use_case.user.UserUseCases
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPricesUseCase: GetPricesUseCase,
    private val authUseCases: AuthUseCases,
    private val userUseCases: UserUseCases,
    private val saveCurrencyUseCase: SaveCurrencyUseCase,
    private val readCurrencyUseCase: ReadCurrencyUseCase,
    private val deleteProfileImageUseCase: DeleteProfileImageUseCase,
    private val currencyExchangeUseCase: CurrencyExchangeUseCase,
    private val profileImageUseCase: UploadProfileImageUseCase,
    private val currencyInitializer: CurrencyInitializer,
): ViewModel() {
    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state
    private val _eventFlow = MutableSharedFlow<HomeUiEvent>()
    val eventFlow: SharedFlow<HomeUiEvent> = _eventFlow
    private var allCoins: List<Coin>? = emptyList()
    private val _selectedCurrency = mutableStateOf<CurrencyCode>(CurrencyCode.USD)
    val selectedCurrency: State<CurrencyCode> = _selectedCurrency
    var verificationId: String = ""

    private companion object {
        const val TAG = "HomeViewModel"
    }

    init {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )
            currencyInitializer.initialize()
            getCoins()
            getProviders()
            getCurrency()
        }
    }

    fun getCurrency() {
        viewModelScope.launch {
            _selectedCurrency.value = readCurrencyUseCase().first()
        }
    }

    fun saveCurrency(currency: CurrencyCode) {
        viewModelScope.launch {
            saveCurrencyUseCase(currency)
            currencyInitializer.initialize()
            _selectedCurrency.value = currency
            refresh()
        }
    }

    fun refresh() {
        getCoins()
    }

    fun logout() {
        viewModelScope.launch {
            authUseCases.logout()
            _eventFlow.emit(
                HomeUiEvent.LoggedOut
            )
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

    fun deleteAccount() {
        viewModelScope.launch {
            _eventFlow.emit(HomeUiEvent.DeleteAccountScreenLoading(isLoading = true))
            val uid = authUseCases.getUserId()
            authUseCases.deleteAccount().collect { resource ->
                when(resource) {
                    is Resource.Loading<*> -> {
                    }
                    is Resource.Success<*> -> {
                        if(uid == null) {
                            _eventFlow.emit(HomeUiEvent.DeleteAccountScreenLoading(isLoading = false))
                            _eventFlow.emit(HomeUiEvent.ShowSnackBar(message = "User not found. Try Logging In!"))
                            return@collect
                        }
                        val deleted = userUseCases.deleteUser(uid)

                        if(deleted) {
                            deleteProfileImageUseCase(uid)
                            _eventFlow.emit(HomeUiEvent.DeleteAccountScreenLoading(isLoading = false))

                            _eventFlow.emit(
                                HomeUiEvent.ShowSnackBar("Account deleted successfully!")
                            )
                            delay(1000)

                            logout()
                        } else {
                            _eventFlow.emit(HomeUiEvent.DeleteAccountScreenLoading(isLoading = false))
                            _eventFlow.emit(
                                HomeUiEvent.ShowSnackBar("Failed to delete user data")
                            )
                        }
                    }
                    is Resource.Error<*> -> {
                        _eventFlow.emit(HomeUiEvent.DeleteAccountScreenLoading(isLoading = false))
                        _eventFlow.emit(
                            HomeUiEvent.ShowSnackBar(
                                message = resource.message ?: "An unexpected error has occurred!"
                            )
                        )
                    }
                }
            }
        }
    }

    fun updateUser(user: User, imageUpdated: Boolean = false) {
        viewModelScope.launch {
            _eventFlow.emit(HomeUiEvent.EditScreenLoading(isLoading = true))
            if(imageUpdated) {
                when(val result = profileImageUseCase(user.photoUri?.toUri() ?: "".toUri())) {
                    is Resource.Error<*> -> {
                        _eventFlow.emit(HomeUiEvent.EditScreenLoading(isLoading = false))
                        _eventFlow.emit(
                            HomeUiEvent.ShowToast(message = result.message ?: "An unexpected error has occurred!")
                        )
                    }
                    is Resource.Success<*> -> {
                        var user = user.copy(
                            photoUri = result.data
                        )
                        when(val authResult = authUseCases.updateCurrentUser(user.name, result.data?.toUri())) {
                            is Resource.Loading<*> -> {}
                            is Resource.Success<*> -> {
                                userUseCases.createUser(user).collect { resource ->
                                    when(resource) {
                                        is Resource.Loading<*> -> {}
                                        is Resource.Success<*> -> {
                                            _eventFlow.emit(HomeUiEvent.EditScreenLoading(isLoading = false))
                                            _eventFlow.emit(
                                                HomeUiEvent.ProfileUpdated
                                            )
                                            _eventFlow.emit(
                                                HomeUiEvent.ShowSnackBar(
                                                    message = "Profile Updated Successfully!"
                                                )
                                            )
                                        }
                                        is Resource.Error<*> -> {
                                            _eventFlow.emit(HomeUiEvent.EditScreenLoading(isLoading = false))
                                            _eventFlow.emit(
                                                HomeUiEvent.ShowToast(
                                                    message = resource.message ?: "An unexpected error has occurred!"
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                            is Resource.Error<*> -> {
                                _eventFlow.emit(HomeUiEvent.EditScreenLoading(isLoading = false))
                                _eventFlow.emit(
                                    HomeUiEvent.ShowToast(
                                        message = authResult.message ?: "An unexpected error has occurred!"
                                    )
                                )
                            }
                        }
                    }
                    is Resource.Loading<*> -> {}
                }
            } else {
                when(val authResult = authUseCases.updateCurrentUser(user.name, user.photoUri?.toUri())) {
                    is Resource.Loading<*> -> {}
                    is Resource.Success<*> -> {
                        userUseCases.createUser(user).collect { resource ->
                            when(resource) {
                                is Resource.Loading<*> -> {}
                                is Resource.Success<*> -> {
                                    _eventFlow.emit(
                                        HomeUiEvent.ProfileUpdated
                                    )
                                    _eventFlow.emit(
                                        HomeUiEvent.ShowSnackBar(
                                            message = "Profile Updated Successfully!"
                                        )
                                    )
                                }
                                is Resource.Error<*> -> {
                                    _eventFlow.emit(
                                        HomeUiEvent.ShowToast(
                                            message = resource.message ?: "An unexpected error has occurred!"
                                        )
                                    )
                                }
                            }
                        }
                    }
                    is Resource.Error<*> -> {
                        _eventFlow.emit(
                            HomeUiEvent.ShowToast(
                                message = authResult.message ?: "An unexpected error has occurred!"
                            )
                        )
                    }
                }
            }
        }
    }

    fun getProviders() {
        viewModelScope.launch {
            when(val result = authUseCases.getProviders()) {
                is Resource.Loading<*> -> {}
                is Resource.Success<*> -> {
                    _state.value = _state.value.copy(
                        providers = result.data ?: emptyList()
                    )
                }
                is Resource.Error<*> -> {
                    Log.d(TAG, "getProviders " + result.message ?: "An unexpected error has occurred!")
                }
            }
        }
    }

    fun changePassword(
        newPassword: String,
        currentPassword: String,
        confirmPassword: String,
    ) {
        viewModelScope.launch {
            _eventFlow.emit(HomeUiEvent.PasswordChangeScreenLoading(isLoading = true))
            authUseCases.changePassword(
                currentPassword = currentPassword,
                newPassword = newPassword,
                confirmPassword = confirmPassword
            ).collect { resource ->
                when(resource) {
                    is Resource.Loading<*> -> {
                    }
                    is Resource.Success<*> -> {
                        _eventFlow.emit(HomeUiEvent.PasswordChangeScreenLoading(isLoading = false))
                        _eventFlow.emit(HomeUiEvent.PasswordChanged)
                        _eventFlow.emit(
                            HomeUiEvent.ShowSnackBar(
                                message = "Password changed successfully! Please login again."
                            )
                        )
                        delay(1000)
                        logout()
                    }
                    is Resource.Error<*> -> {
                        _eventFlow.emit(HomeUiEvent.PasswordChangeScreenLoading(isLoading = false))
                        _eventFlow.emit(
                            HomeUiEvent.ShowToast(
                                message = resource.message ?: "An unexpected error has occurred!"
                            )
                        )
                    }
                }
            }
        }
    }

    private fun getCoins() {
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
                        allCoins = resource.data ?: emptyList()
                        _state.value = _state.value.copy(
                            coins = resource.data ?: emptyList()
                        )
                        getUser()
                    }
                    is Resource.Error<*> -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = resource.message ?: "An unexpected error occurred!"
                        )
                    }
                }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        if(query.isBlank()) {
            _state.value = _state.value.copy(
                coins = allCoins ?: emptyList()
            )
        } else {
            _state.value = _state.value.copy(
                coins = allCoins?.filter { coin ->
                    coin.name.contains(query, true) ||
                        coin.symbol.contains(query, true)
                } ?: emptyList())
        }
    }

    fun getUser() {
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
                            _state.value = _state.value.copy(
                                user = it,
                                favoriteCoinIds = it.favorites.keys.toSet(),
                                isLoading = false
                            )
                        }
                    }
                    is Resource.Error<*> -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = resource.message ?: "An unexpected error occurred!"
                        )
                    }
                }
            }
        }
    }

    fun addFavorite(symbol: String) {
        viewModelScope.launch {
            userUseCases.addFavorite(symbol).collect { resource ->
                when(resource) {
                    is Resource.Loading<*> -> {
                    }
                    is Resource.Success<*> -> {
                        _eventFlow.emit(
                            HomeUiEvent.ShowSnackBar(
                                message = "Coin added to Favorites!"
                            )
                        )
                    }
                    is Resource.Error<*> -> {
                        Log.d(TAG, "addFavorite " + resource.message)
                    }
                }
            }
        }
    }

    fun removeFavorite(symbol: String) {
        viewModelScope.launch {
            userUseCases.removeFavorite(symbol).collect { resource ->
                when(resource) {
                    is Resource.Loading<*> -> {
                    }
                    is Resource.Success<*> -> {
                        _eventFlow.emit(
                            HomeUiEvent.ShowSnackBar(
                                message = "Coin removed from Favorites!"
                            )
                        )
                    }
                    is Resource.Error<*> -> {
                        Log.d(TAG, "removeFavorite " + resource.message)
                    }
                }
            }
        }
    }

    fun sendOTP(activity: Activity, phoneNumber: String) {
        viewModelScope.launch {
            val result = authUseCases.sendPhoneOTP(
                activity = activity,
                phoneNumber = phoneNumber
            )
            when(result) {
                is Resource.Error<*> -> {
                    _eventFlow.emit(
                        HomeUiEvent.ShowToast(
                            result.message ?: "Couldn't send OTP at the moment!"
                        )
                    )
                }
                is Resource.Success<*> -> {
                    verificationId = result.data ?: ""
                    _eventFlow.emit(
                        HomeUiEvent.OTPSent
                    )
                    _eventFlow.emit(
                        HomeUiEvent.ShowToast(
                            "Verify OTP sent to your phone number!"
                        )
                    )
                }
                is Resource.Loading<*> -> {
                }
            }
        }
    }

    fun verifyOTP(otp: String) {
        viewModelScope.launch {
            if(verificationId.isNotBlank()) {
                val result = authUseCases.verifyPhoneOTP(verificationId = verificationId, otp = otp)
                when(result) {
                    is Resource.Loading<*> -> {}
                    is Resource.Success<*> -> {
                        _eventFlow.emit(
                            HomeUiEvent.ShowToast(
                                message = "Verification completed!"
                            )
                        )
                        _eventFlow.emit(
                            HomeUiEvent.OTPVerified
                        )
                    }
                    is Resource.Error<*> -> {
                        _eventFlow.emit(
                            HomeUiEvent.ShowToast(
                                message = "Verification Failed. Enter a valid phone number!"
                            )
                        )
                    }
                }
            } else {
                _eventFlow.emit(
                    HomeUiEvent.ShowToast(
                        message = "Verification Failed. Enter a valid phone number!"
                    )
                )
            }
        }
    }
}