package com.example.cryptox.presentation.home.component

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cryptox.common.utils.extensions.findActivity
import com.example.cryptox.domain.model.User
import com.example.cryptox.presentation.components.OTPTextField
import com.example.cryptox.presentation.home.HomeUiEvent
import com.example.cryptox.presentation.home.HomeViewModel
import com.example.cryptox.presentation.loading.CustomLoader

@Composable
fun EditUserProfile(
    modifier: Modifier = Modifier,
    user: User,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    var name by remember { mutableStateOf(user.name) }
    var email by remember { mutableStateOf(user.email) }
    var phoneNumber by remember { mutableStateOf(user.phoneNumber) }
    var photoUrl by remember { mutableStateOf(user.photoUri) }
    var showOTP by remember { mutableStateOf(false) }
    var otp by remember { mutableStateOf("") }
    var phoneVerified by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val activity = LocalContext.current.findActivity() ?: context as Activity
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) {
        if(it != null) {
            photoUrl = it.toString()
        }
    }
    var showLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when(event) {
                is HomeUiEvent.EditScreenLoading -> {
                    showLoading = event.isLoading
                }
                is HomeUiEvent.OTPSent -> {
                    showOTP = true
                }
                is HomeUiEvent.OTPVerified -> {
                    phoneVerified = true
                }
                else -> {}
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
                "Update Profile",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.8f),
                value = name ?: "",
                label = {
                    Text("Name")
                },
                placeholder = {
                    Text("Name")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        "Person Icon"
                    )
                },
                singleLine = true,
                onValueChange = { value ->
                    name = value
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.8f),
                value = email ?: "",
                readOnly = true,
                singleLine = true,
                label = {
                    Text("Email")
                },
                placeholder = {
                    Text("Email")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        "Email Icon"
                    )
                },
                onValueChange = { value ->
                    email = value
                },
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.8f),
                value = phoneNumber ?: "",
                label = {
                    Text("Phone Number")
                },
                placeholder = {
                    Text("Phone Number")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        "Phone Icon"
                    )
                },
                trailingIcon = {
                    Button(
                        onClick = {
                            viewModel.sendOTP(
                                activity = activity,
                                phoneNumber = phoneNumber ?: ""
                            )
                        },
                        modifier = Modifier.padding(end = 5.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.LightGray
                        ),
                        enabled = user.phoneNumber != phoneNumber,
                        shape = RoundedCornerShape(5.dp)
                    ) {
                        Text("Verify")
                    }
                },
                singleLine = true,
                onValueChange = { value ->
                    phoneNumber = value
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            if(showOTP) {
                OTPTextField(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    otp = otp,
                    onOtpChange = {
                        otp = it
                    },
                    otpLength = 6,
                    onOtpComplete = {
                        showOTP = false
                        viewModel.verifyOTP(it)
                        otp = ""
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.8f),
                value = photoUrl ?: "",
                readOnly = true,
                label = {
                    Text("Profile Photo")
                },
                placeholder = {
                    Text("Profile Photo")
                },
                leadingIcon = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.37f)
                            .padding(start = 8.dp)
                            .background(color = Color.Gray)
                            .clickable(onClick = {
                                photoPicker.launch(
                                    PickVisualMediaRequest(
                                        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            })
                    ) {
                        Text(
                            "Choose a file",
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                },
                singleLine = true,
                onValueChange = { value ->
                    photoUrl = value
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    var user = user
                    if(name == user.name && phoneNumber == user.phoneNumber && photoUrl == user.photoUri) {
                        Toast.makeText(context, "No changes made to Profile!", Toast.LENGTH_LONG).show()
                    } else {
                        name?.let {
                            if(it.isBlank()) {
                                Toast.makeText(context, "Enter your name!", Toast.LENGTH_LONG).show()
                                return@Button
                            }
                        }
                        phoneNumber?.let {
                            if((it != user.phoneNumber) and !phoneVerified) {
                                Toast.makeText(context, "Verify your phone number!", Toast.LENGTH_LONG).show()
                                return@Button
                            }
                        }
                        val imageUpdated = user.photoUri != photoUrl
                        user = user.copy(
                            name = name ?: "",
                            phoneNumber = phoneNumber,
                            photoUri = photoUrl
                        )
                        viewModel.updateUser(
                            user,
                            imageUpdated = imageUpdated
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Text("Update Profile", color = Color.White)
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun EditUserProfilePreview() {
    EditUserProfile(
        user = User(
            uid = "dgdgvdvdvgvdgvdg",
            email = "vsdf@s.com",
            name = "sanjay",
            phoneNumber = "8765432314",
            photoUri = null,
            favorites = emptyMap(),
            portfolio = emptyMap()
        ),
    )
}