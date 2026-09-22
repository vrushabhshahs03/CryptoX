package com.example.cryptox.presentation.home.component


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cryptox.presentation.home.HomeUiEvent
import com.example.cryptox.presentation.home.HomeViewModel
import com.example.cryptox.presentation.loading.CustomLoader
import kotlinx.coroutines.launch

@Composable
fun ChangePassword(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }
    var showCurrentPassword by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmNewPassword by remember { mutableStateOf(false) }
    var showLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            if(event is HomeUiEvent.PasswordChangeScreenLoading) {
                showLoading = event.isLoading
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        if(showLoading) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable(
                        onClick = {}, indication = null,
                        interactionSource = remember {
                            MutableInteractionSource()
                        })
                    .background(
                        Color.Black.copy(alpha = 0.1f)
                    )
            ) {
                CustomLoader(modifier = Modifier.align(Alignment.Center))
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                "Change Password",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.8f),
                value = currentPassword,
                label = {
                    Text("Current Password")
                },
                trailingIcon = {
                    IconButton(onClick = {
                        showCurrentPassword = !showCurrentPassword
                    }) {
                        Icon(
                            imageVector = if(showCurrentPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Visibility Icon"
                        )
                    }
                },
                visualTransformation = if(showCurrentPassword) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Password,
                        contentDescription = "Password Icon"
                    )
                },
                placeholder = {
                    Text("Current Password")
                },
                singleLine = true,
                onValueChange = { value ->
                    currentPassword = value
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.8f),
                value = newPassword,
                singleLine = true,
                label = {
                    Text("New Password")
                },
                trailingIcon = {
                    IconButton(onClick = {
                        showNewPassword = !showNewPassword
                    }) {
                        Icon(
                            imageVector = if(showNewPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Visibility Icon"
                        )
                    }
                },
                visualTransformation = if(showNewPassword) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Password,
                        contentDescription = "Password Icon"
                    )
                },
                placeholder = {
                    Text("New Password")
                },
                onValueChange = { value ->
                    newPassword = value
                },
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.8f),
                value = confirmNewPassword,
                label = {
                    Text("Confirm New Password")
                },
                trailingIcon = {
                    IconButton(onClick = {
                        showConfirmNewPassword = !showConfirmNewPassword
                    }) {
                        Icon(
                            imageVector = if(showConfirmNewPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Visibility Icon"
                        )
                    }
                },
                visualTransformation = if(showConfirmNewPassword) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Password,
                        contentDescription = "Password Icon"
                    )
                },
                placeholder = {
                    Text("Confirm New Password")
                },
                singleLine = true,
                onValueChange = { value ->
                    confirmNewPassword = value
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    viewModel.changePassword(
                        currentPassword = currentPassword,
                        newPassword = newPassword,
                        confirmPassword = confirmNewPassword,
                    )
                },
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Text("Update Password", color = Color.White)
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ChangePasswordPreview() {
    ChangePassword(
    )
}