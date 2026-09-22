package com.example.cryptox.presentation.auth.signup

import android.R.attr.text
import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.cryptox.presentation.auth.common.AuthUiEvent
import com.example.cryptox.presentation.components.OTPTextField
import com.example.cryptox.presentation.loading.CustomLoader
import com.example.cryptox.presentation.navigation.Screens
import com.rejowan.ccpc.Country
import com.rejowan.ccpc.CountryCodePicker
import com.rejowan.ccpc.ViewCustomization
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SignUpScreen(
    navController: NavHostController,
    viewModel: SignUpViewModel = hiltViewModel(),
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    var showLoading by remember { mutableStateOf(false) }
    var otp by remember { mutableStateOf("") }
    var showModal by remember { mutableStateOf(false) }
    var selectedCountry by remember { mutableStateOf(Country.India) }
    var goLoginEnabled by remember { mutableStateOf(true) }
    val sheetState = rememberModalBottomSheetState(
        confirmValueChange = { targetValue ->
            targetValue != SheetValue.Hidden
        }
    )
    val context = LocalContext.current

    if(!goLoginEnabled) {
        BackHandler() {
        }
    }


    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            if(event is AuthUiEvent.ShowSnackBar) {
            }
            when(event) {
                is AuthUiEvent.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(message = event.message)
                }
                is AuthUiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
                is AuthUiEvent.Loading -> {
                    showLoading = event.isLoading
                }
                is AuthUiEvent.onSignup -> {
                    delay(1000)
                    navController.navigate(Screens.Login.route) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                }
                is AuthUiEvent.OTPSent -> {
                    showModal = true
                }
                is AuthUiEvent.OTPVerified -> {
                    otp = ""
                    showModal = false
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { it ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "SignUp",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { value ->
                    name = value
                },
                singleLine = true,
                label = {
                    Text("Name")
                },
                placeholder = {
                    Text("Name")
                },
                leadingIcon = {
                    Icon(Icons.Default.Person, "Email Icon")
                },
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { value ->
                    email = value
                },
                singleLine = true,
                label = {
                    Text("Email")
                },
                placeholder = {
                    Text("Email")
                },
                leadingIcon = {
                    Icon(Icons.Default.Email, "Email Icon")
                },
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { value ->
                    phoneNumber = value
                },
                singleLine = true,
                label = {
                    Text("Phone")
                },
                placeholder = {
                    Text("Phone")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                leadingIcon = {
                    //Icon(Icons.Default.Phone, "Phone Icon")
                    CountryCodePicker(
                        selectedCountry = selectedCountry,
                        onCountrySelected = {
                            selectedCountry = it
                        },
                        showSheet = true,
                        viewCustomization = ViewCustomization(
                            showCountryName = false,
                            showCountryIso = false,
                            showCountryCode = true,
                            showFlag = true,
                            showArrow = true
                        )
                    )
                },
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                "Number needs to be verified to create account!",
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { value ->
                    password = value
                },
                singleLine = true,
                label = {
                    Text("Password")
                },
                placeholder = {
                    Text("Password")
                },
                leadingIcon = {
                    Icon(Icons.Default.Password, "Password Icon")
                },
                trailingIcon = {
                    IconButton(onClick = {
                        passwordVisible = !passwordVisible
                    }) {
                        Icon(
                            imageVector = if(passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Visibility Icon",
                        )
                    }
                },
                visualTransformation = if(passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { value ->
                    confirmPassword = value
                },
                singleLine = true,
                label = {
                    Text("Confirm Password")
                },
                placeholder = {
                    Text("Confirm Password")
                },
                leadingIcon = {
                    Icon(Icons.Default.Password, "Password Icon")
                },
                trailingIcon = {
                    IconButton(onClick = {
                        confirmPasswordVisible = !confirmPasswordVisible
                    }) {
                        Icon(
                            imageVector = if(confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Visibility Icon",
                        )
                    }
                },
                visualTransformation = if(confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    if(name.isBlank()) {
                        Toast.makeText(context, "Enter a valid name!", Toast.LENGTH_LONG).show()
                        return@Button
                    }
                    if(
                        phoneNumber.length != 10 ||
                        !phoneNumber.all(Char::isDigit)
                    ) {
                        Toast.makeText(
                            context,
                            "Enter a valid phone number!",
                            Toast.LENGTH_LONG
                        ).show()

                        return@Button
                    }
                    goLoginEnabled = false
                    viewModel.signUp(
                        email = email,
                        password = password,
                        name = name,
                        confirmPassword = confirmPassword,
                        phoneNumber = selectedCountry.countryCode + phoneNumber,
                        activity = context as Activity
                    )
                },
                enabled = !showLoading,
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Text(
                    "Create Account",
                    modifier = Modifier.padding(vertical = 10.dp),
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            if(showLoading) {
                CustomLoader(modifier = Modifier.size(25.dp))
            }
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "Already have an account? Login",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.clickable(
                    enabled = goLoginEnabled,
                    onClick = {
                        navController.navigate(Screens.Login.route) {
                            popUpTo(0)
                            launchSingleTop = true
                        }
                    }
                )
            )
            if(showModal) {
                ModalBottomSheet(
                    onDismissRequest = {},
                    sheetState = sheetState,
                    dragHandle = null
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 30.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Enter OTP",
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            OTPTextField(
                                otp = otp,
                                onOtpChange = { value -> otp = value },
                                otpLength = 6,
                                onOtpComplete = { completeOTP ->
                                    viewModel.verifyOTP(completeOTP)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SignUpScreenPreview() {
    SignUpScreen(
        navController = rememberNavController(),
    )
}