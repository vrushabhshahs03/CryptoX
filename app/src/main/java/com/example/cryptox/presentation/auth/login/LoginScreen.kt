package com.example.cryptox.presentation.auth.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cryptox.presentation.auth.common.GoogleButton
import com.example.cryptox.presentation.auth.common.AuthUiEvent
import com.example.cryptox.presentation.loading.CustomLoader

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    onLogin: () -> Unit,
    onSigUpClicked: () -> Unit,
) {
    val state = viewModel.state.value
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    var showGoogleLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(key1 = state.isSuccess) {
        if(state.isSuccess) {
            showGoogleLoading = false
            onLogin()
        }
    }

    LaunchedEffect(key1 = state.error) {
        if(state.error.isNotBlank()) {
            showGoogleLoading = false
            snackbarHostState.showSnackbar(
                message = state.error
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            if(event is AuthUiEvent.ShowSnackBar) {
                snackbarHostState.showSnackbar(
                    message = event.message
                )
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                "Login",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(30.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { value ->
                    email = value
                },
                label = {
                    Text("Email")
                },
                placeholder = {
                    Text("Email")
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Email,
                        "Email Icon"
                    )
                },
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { value ->
                    password = value
                },
                label = {
                    Text("Password")
                },
                placeholder = {
                    Text("Password")
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Password,
                        contentDescription = "Password Icon"
                    )
                },
                trailingIcon = {
                    IconButton(onClick = {
                        passwordVisible = !passwordVisible
                    }) {
                        Icon(
                            if(passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Visibility Icon"
                        )
                    }
                },
                singleLine = true,
                visualTransformation = if(passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(30.dp))
            Button(
                onClick = {
                    viewModel.login(
                        email = email,
                        password = password
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(0.5f),
                enabled = !state.isLoading
            ) {
                Text(
                    text = "Log In",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 5.dp)
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                "Forgot Password?",
                style = MaterialTheme.typography.bodyLarge,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable(
                    onClick = {
                        viewModel.forgotPassword(
                            email = email
                        )
                        email = ""
                        password = ""
                    }
                )
            )
            Spacer(modifier = Modifier.height(15.dp))
            if(state.isLoading and !showGoogleLoading) {
                CustomLoader(
                    modifier = Modifier.size(25.dp)
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            GoogleButton(
                modifier = Modifier,
                isLoading = state.isLoading and showGoogleLoading,
                onClick = {
                    showGoogleLoading = true
                    viewModel.signUpWithGoogle(context)
                }
            )
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                modifier = Modifier.clickable(
                    onClick = {
                        onSigUpClicked()
                    }
                ),
                text = "Don't have an account? Create Account"
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LoginScreenPreview() {
    LoginScreen(onLogin = {}, onSigUpClicked = {})
}