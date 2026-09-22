package com.example.cryptox.presentation.components

import android.R.attr.onClick
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun OTPTextField(
    modifier: Modifier = Modifier,
    otp: String,
    otpLength: Int ,
    onOtpChange: (String) -> Unit,
    onOtpComplete: (String) -> Unit,
) {
    val focusRequester = remember {
        FocusRequester()
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = modifier.clickable(
            indication = null,
            interactionSource = remember {
                MutableInteractionSource()
            }
        ) {
            focusRequester.requestFocus()
        },
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = otp,
            onValueChange = { value ->
                if(
                    value.length <= otpLength &&
                    value.all(Char::isDigit)
                ) {
                    onOtpChange(value)
                    if (value.length == otpLength) {
                        onOtpComplete(value)
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword
            ),
            modifier = Modifier
                .matchParentSize()
                .alpha(0f)
                .focusRequester(focusRequester)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            repeat(otpLength) { index ->
                val char =
                    otp.getOrNull(index)
                        ?.toString()
                        ?: ""
                val isActive =
                    otp.length == index ||
                        (
                            otp.length == otpLength &&
                                index == otpLength - 1
                            )

                OutlinedCard(
                    modifier = Modifier
                        .size(
                            width = 40.dp,
                            height = 65.dp
                        )
                        .border(
                            width = if(isActive) 3.dp else 1.dp,
                            color = if(isActive)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun OTPTextFieldPreview() {
    OTPTextField(
        otp = "1234",
        otpLength = 4,
        onOtpChange = {},
        onOtpComplete = {}
    )
}