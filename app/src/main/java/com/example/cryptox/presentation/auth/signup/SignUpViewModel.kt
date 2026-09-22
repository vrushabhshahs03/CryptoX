package com.example.cryptox.presentation.auth.signup

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptox.common.Resource
import com.example.cryptox.domain.model.toUser
import com.example.cryptox.domain.use_case.auth.AuthUseCases
import com.example.cryptox.domain.use_case.onboarding.SaveOnBoardingStateUseCase
import com.example.cryptox.domain.use_case.user.UserUseCases
import com.example.cryptox.presentation.auth.common.AuthState
import com.example.cryptox.presentation.auth.common.AuthUiEvent
import com.example.cryptox.presentation.home.HomeUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val userUseCases: UserUseCases,
    private val saveOnBoardingStateUseCase: SaveOnBoardingStateUseCase,
): ViewModel() {
    private val _eventFlow = MutableSharedFlow<AuthUiEvent>()
    val eventFlow: SharedFlow<AuthUiEvent> = _eventFlow
    var verificationId: String = ""
    var userCreated: Boolean = false

    //    fun signUp(
//        email: String,
//        password: String,
//        name: String,
//        phoneNumber: String,
//        confirmPassword: String,
//    ) {
//        viewModelScope.launch {
//            if(name.isBlank()) {
//                _eventFlow.emit(
//                    AuthUiEvent.ShowSnackBar(
//                        message = "Enter a valid name!"
//                    )
//                )
//                return@launch
//            }
//            if(password != confirmPassword) {
//                _eventFlow.emit(
//                    AuthUiEvent.ShowSnackBar(
//                        message = "Passwords don't match!"
//                    )
//                )
//                return@launch
//            }
//            authUseCases
//                .signUp(email, password, name)
//                .flatMapConcat { authResource ->
//                    when(authResource) {
//                        is Resource.Loading -> {
//                            flowOf(Resource.Loading())
//                        }
//                        is Resource.Error -> {
//                            flowOf(
//                                Resource.Error(
//                                    authResource.message
//                                        ?: "Signup failed"
//                                )
//                            )
//                        }
//                        is Resource.Success -> {
//                            var user = authResource.data!!.toUser()
//                            user = user.copy(
//                                phoneNumber = phoneNumber,
//                                photoUri = null
//                            )
//                            userUseCases.createUser(user)
//                        }
//                    }
//                }
//                .collect { result ->
//                    when(result) {
//                        is Resource.Loading -> {
//                            _state.value = AuthState(
//                                isLoading = true
//                            )
//                        }
//                        is Resource.Success -> {
//                            _eventFlow.emit(
//                                AuthUiEvent.ShowSnackBar(
//                                    "Verification email sent to your email address!"
//                                )
//                            )
//
//                            _state.value = AuthState(
//                                isLoading = false,
//                                isSuccess = true
//                            )
//                        }
//                        is Resource.Error -> {
//                            _state.value = AuthState(
//                                isLoading = false,
//                                error = result.message
//                                    ?: "An unknown error occurred!"
//                            )
//                        }
//                    }
//                }
//        }
//    }
    fun signUp(
        activity: Activity,
        email: String,
        password: String,
        name: String,
        phoneNumber: String,
        confirmPassword: String,
    ) {
        viewModelScope.launch {
            if(name.isBlank()) {
                _eventFlow.emit(
                    AuthUiEvent.ShowSnackBar(
                        message = "Enter a valid name!"
                    )
                )
                return@launch
            }
            if(password != confirmPassword) {
                _eventFlow.emit(
                    AuthUiEvent.ShowSnackBar(
                        message = "Passwords don't match!"
                    )
                )
                return@launch
            }
            if(userCreated) {
                sendOTP(activity = activity, phoneNumber = phoneNumber)
            } else {
                authUseCases
                    .signUp(email, password, name)
                    .flatMapConcat { authResource ->
                        when(authResource) {
                            is Resource.Loading -> {
                                flowOf(Resource.Loading())
                            }
                            is Resource.Error -> {
                                flowOf(
                                    Resource.Error(
                                        authResource.message
                                            ?: "Signup failed"
                                    )
                                )
                            }
                            is Resource.Success -> {
                                var user = authResource.data!!.toUser()
                                user = user.copy(
                                    phoneNumber = phoneNumber,
                                    photoUri = null
                                )
                                userUseCases.createUser(user)
                            }
                        }
                    }
                    .collect { result ->
                        when(result) {
                            is Resource.Loading -> {
                                _eventFlow.emit(
                                    AuthUiEvent.Loading(
                                        isLoading = true
                                    )
                                )
                            }
                            is Resource.Success -> {
                                _eventFlow.emit(
                                    AuthUiEvent.Loading(
                                        isLoading = false
                                    )
                                )
                                userCreated = true
                                saveOnBoardingStateUseCase(completed = false)
                                sendOTP(activity = activity, phoneNumber = phoneNumber)
                            }
                            is Resource.Error -> {
                                _eventFlow.emit(
                                    AuthUiEvent.Loading(
                                        isLoading = false
                                    )
                                )
                                _eventFlow.emit(
                                    AuthUiEvent.ShowSnackBar(
                                        message = result.message
                                            ?: "An unknown error occurred!"
                                    )
                                )
                            }
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
                        AuthUiEvent.ShowSnackBar(
                            result.message ?: "Couldn't send OTP at the moment!"
                        )
                    )
                }
                is Resource.Success<*> -> {
                    verificationId = result.data ?: ""
                    _eventFlow.emit(
                        AuthUiEvent.OTPSent
                    )
                    _eventFlow.emit(
                        AuthUiEvent.ShowSnackBar(
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
                _eventFlow.emit(
                    AuthUiEvent.OTPVerified
                )
                _eventFlow.emit(
                    AuthUiEvent.Loading(isLoading = true)
                )
                val result = authUseCases.verifyPhoneOTP(verificationId = verificationId, otp = otp)
                when(result) {
                    is Resource.Loading<*> -> {}
                    is Resource.Success<*> -> {
                        _eventFlow.emit(
                            AuthUiEvent.Loading(isLoading = false)
                        )
                        authUseCases.logout()
                        _eventFlow.emit(
                            AuthUiEvent.ShowSnackBar(
                                message = "Verification email sent to your email address!"
                            )
                        )
                        _eventFlow.emit(
                            AuthUiEvent.onSignup
                        )
                    }
                    is Resource.Error<*> -> {
                        _eventFlow.emit(
                            AuthUiEvent.Loading(isLoading = false)
                        )
                        _eventFlow.emit(
                            AuthUiEvent.ShowSnackBar(
                                message = "Verification Failed. Enter a valid phone number!"
                            )
                        )
                    }
                }
            } else {
                _eventFlow.emit(
                    AuthUiEvent.OTPVerified
                )
                _eventFlow.emit(
                    AuthUiEvent.Loading(isLoading = false)
                )
                _eventFlow.emit(
                    AuthUiEvent.ShowSnackBar(
                        message = "Verification Failed. Enter a valid phone number!"
                    )
                )
            }
        }
    }
}