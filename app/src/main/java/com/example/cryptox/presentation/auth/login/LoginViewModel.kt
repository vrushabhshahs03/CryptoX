package com.example.cryptox.presentation.auth.login

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptox.common.Resource
import com.example.cryptox.data.auth.GoogleAuthService
import com.example.cryptox.domain.model.toUser
import com.example.cryptox.domain.use_case.auth.AuthUseCases
import com.example.cryptox.domain.use_case.user.UserUseCases
import com.example.cryptox.presentation.auth.common.AuthState
import com.example.cryptox.presentation.auth.common.AuthUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val userUseCases: UserUseCases,
    private val googleAuthService: GoogleAuthService,
): ViewModel() {
    private val _state = mutableStateOf(AuthState())
    val state: State<AuthState> = _state
    private val _eventFlow = MutableSharedFlow<AuthUiEvent>()
    val eventFlow: SharedFlow<AuthUiEvent> = _eventFlow
    fun login(
        email: String,
        password: String,
    ) {
        viewModelScope.launch {
            authUseCases.login(email, password)
                .collect { resource ->
                    when(resource) {
                        is Resource.Loading<*> -> {
                            _state.value = AuthState(
                                isLoading = true
                            )
                        }
                        is Resource.Success<*> -> {
                            _state.value = AuthState(
                                isSuccess = true
                            )
                        }
                        is Resource.Error<*> -> {
                            _state.value = AuthState(
                                error = resource.message ?: "An error has occurred. Try again!"
                            )
                        }
                    }
                }
        }
    }

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            authUseCases.forgotPassword(email)
            _eventFlow.emit(
                AuthUiEvent.ShowSnackBar(
                    "Password reset email has been sent to your email address. Check inbox for further steps!"
                )
            )
        }
    }

    fun signUpWithGoogle(context: Context) {
        viewModelScope.launch {
            val idToken = googleAuthService.signIn(context)
            authUseCases
                .signUpWithGoogle(idToken)
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
                            val user = authResource.data!!.toUser()

                            if(!userUseCases.userExists(user.uid)) {
                                userUseCases.createUser(user)
                            } else {
                                flowOf(Resource.Success(user))
                            }
                        }
                    }
                }.collect { result ->
                    when(result) {
                        is Resource.Loading -> {
                            _state.value = AuthState(
                                isLoading = true
                            )
                        }
                        is Resource.Success -> {
                            _state.value = AuthState(
                                isLoading = false,
                                isSuccess = true
                            )
                        }
                        is Resource.Error -> {
                            _state.value = AuthState(
                                isLoading = false,
                                error = result.message
                                    ?: "An unknown error occurred!"
                            )
                        }
                    }
                }
        }
    }
}