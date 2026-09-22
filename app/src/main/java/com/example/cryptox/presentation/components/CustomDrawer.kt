package com.example.cryptox.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cryptox.domain.model.CurrencyCode

@Composable
fun BoxScope.CustomDrawer(
    onProfileUpdate: () -> Unit,
    onPasswordChange: () -> Unit,
    onAccountDelete: () -> Unit,
    onLogOut: () -> Unit,
    onToggle: () -> Unit,
    onCurrencyChange: (String) -> Unit,
    showDrawer: Boolean,
    name: String?,
    selectedCurrency: CurrencyCode,
    showPasswordChange: Boolean,
) {
    var menuExpanded by remember { mutableStateOf(false) }
    var currencyMenuExpanded by remember { mutableStateOf(false) }
    val currencies = listOf(
        "🇺🇸 USD",
        "🇮🇳 INR",
        "🇪🇺 EUR",
        "🇬🇧 GBP",
        "🇯🇵 JPY",
        "🇦🇺 AUD",
        "🇨🇦 CAD",
        "🇨🇭 CHF",
        "🇨🇳 CNY",
        "🇸🇬 SGD"
    )

    if(showDrawer) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { onToggle() }
        )
    }
    AnimatedVisibility(
        visible = showDrawer,
        modifier = Modifier.align(Alignment.CenterEnd),
        enter = slideInHorizontally(
            animationSpec = spring(
                dampingRatio = 0.8f,
                stiffness = Spring.StiffnessLow
            ),
            { it }
        ),
        exit = slideOutHorizontally(
            animationSpec = tween(1000),
            { it }
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(250.dp)
                .background(
                    color = Color.Blue,
                    shape = RoundedCornerShape(
                        topStart = 20.dp,
                        bottomStart = 20.dp
                    )
                )
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while(true) {
                            awaitPointerEvent()
                        }
                    }
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, top = 50.dp)
            ) {
                Text("Hello, $name", fontSize = 20.sp, color = Color.White)

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    "Settings",
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            menuExpanded = !menuExpanded
                        }
                )

                AnimatedVisibility(menuExpanded) {
                    Column(modifier = Modifier.padding(start = 15.dp)) {
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            "Update Profile",
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onProfileUpdate() }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        if(showPasswordChange) {
                            Text(
                                "Update Password",
                                color = Color.White,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onPasswordChange() }
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        Text(
                            "Change Currency",
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { currencyMenuExpanded = !currencyMenuExpanded }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        AnimatedVisibility(currencyMenuExpanded) {
                            Column(modifier = Modifier.padding(start = 15.dp)) {
                                Spacer(modifier = Modifier.height(10.dp))
                                currencies.forEach { currency ->
                                    val borderColor =
                                        if(selectedCurrency.name == currency.takeLast(3)) Color.White else Color.Transparent
                                    Text(
                                        text = currency,
                                        color = Color.White,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .border(2.dp, color = borderColor)
                                            .padding(start = 5.dp)
                                            .clickable(onClick = {
                                                onCurrencyChange(currency.takeLast(3))
                                            })
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))
                                }
                            }
                        }

                        Text(
                            "Delete Account",
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onAccountDelete() }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    "Logout",
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onLogOut() }
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = Color.Transparent
                        )
                        .clickable(
                            onClick = {},
                            enabled = false
                        )
                        .pointerInput(Unit) {
                            awaitPointerEventScope {
                                while(true) {
                                    awaitPointerEvent()
                                }
                            }
                        }
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun CustomDrawerPreview() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CustomDrawer(
            onProfileUpdate = {},
            onPasswordChange = {},
            onAccountDelete = {},
            onLogOut = {},
            onToggle = {},
            showDrawer = true,
            name = "john",
            selectedCurrency = CurrencyCode.USD,
            onCurrencyChange = {},
            showPasswordChange = true,
        )
    }
}