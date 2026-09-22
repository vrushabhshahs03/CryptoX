package com.example.cryptox.widget

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.text.Text
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.TextStyle
import com.example.cryptox.common.Resource
import com.example.cryptox.common.exchange.CurrencyInitializer
import com.example.cryptox.common.exchange.CurrencyManager
import com.example.cryptox.common.exchange.CurrencyProvider
import com.example.cryptox.common.exchange.toSelectedCurrencyString
import com.example.cryptox.domain.model.Coin
import com.example.cryptox.domain.model.PortfolioItem
import com.example.cryptox.domain.repository.UserPreferencesRepository
import com.example.cryptox.domain.use_case.coins.get_prices.GetPricesUseCase
import com.example.cryptox.domain.use_case.user.GetUserUseCase
import com.example.cryptox.widget.WidgetPrefs.KEY_ERROR
import com.example.cryptox.widget.WidgetPrefs.KEY_PORTFOLIO_TOTAL
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlin.jvm.java

class PortfolioWidget: GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            WidgetEntryPoint::class.java
        )

        CurrencyProvider.currencyManager =
            entryPoint.currencyManager()
        provideContent {
            val isLoading = currentState(WidgetPrefs.KEY_LOADING) ?: false
            val total = currentState(KEY_PORTFOLIO_TOTAL) ?: null
            val error = currentState(KEY_ERROR) ?: false

            Column(
                modifier = GlanceModifier
                    .cornerRadius(16.dp)
                    .background(color = Color.LightGray.copy(alpha = 0.4f))
                    .padding(16.dp)
                    .clickable(
                        onClick = actionRunCallback<RefreshActionCallback>()
                    ),
                horizontalAlignment = Alignment.Horizontal.CenterHorizontally
            ) {
                Text(
                    "CryptoX",
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = GlanceModifier.height(10.dp))
                Text(
                    "Current Holdings",
                    style = TextStyle(
                        fontSize = 24.sp
                    )
                )
                if(isLoading) {
                    CircularProgressIndicator()
                } else {

                    if(total==null){

                    }
                    val showValue = when{
                        total==null -> {"Tap the widget to refresh holdings"}
                        error -> {"Login to use the widget"}
                        else -> {total.toDouble().toSelectedCurrencyString()}
                    }

                    Text(
                        text = showValue,
                        style = TextStyle(
                            fontSize = if(error or total.isNullOrBlank()) 18.sp else 24.sp
                        )
                    )
                }
            }
        }
    }
}

class RefreshActionCallback: ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters,
    ) {
        updateAppWidgetState(context, glanceId) { prefs ->
            prefs[WidgetPrefs.KEY_LOADING] = true
        }

        PortfolioWidget().update(context, glanceId)
        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            WidgetEntryPoint::class.java
        )
        entryPoint.currencyInitializer.initialize()
        val getUserUseCase = entryPoint.getUserUseCase()
        val getPricesUseCase = entryPoint.getPricesUseCase()
        var total = 0.0
        var portfolioItems: Map<String, PortfolioItem> = emptyMap()
        var prices: List<Coin> = emptyList()
        var error: Boolean = false

        when(val userResource = getUserUseCase().first { it !is Resource.Loading }) {
            is Resource.Success -> {
                error = false
                portfolioItems = userResource.data?.portfolio ?: emptyMap()
            }
            is Resource.Error -> {
                error = true
            }
            else -> {}
        }

        when(val priceResource = getPricesUseCase().first { it !is Resource.Loading }) {
            is Resource.Success -> {
                prices = priceResource.data ?: emptyList()
            }
            else -> {}
        }
        val priceMap = prices.associateBy { it.coinId }

        total = portfolioItems.entries.sumOf { (id, item) ->
            val price = priceMap[id]?.price?.uSD?.price ?: 0.0
            price * item.quantity
        }

        updateAppWidgetState(context, glanceId) { prefs ->
            prefs[KEY_PORTFOLIO_TOTAL] = total.toString()
            prefs[WidgetPrefs.KEY_LOADING] = false
            prefs[WidgetPrefs.KEY_ERROR] = error

        }

        PortfolioWidget().update(context, glanceId)
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetEntryPoint {
    val currencyInitializer: CurrencyInitializer
    fun currencyManager(): CurrencyManager
    fun getUserUseCase(): GetUserUseCase
    fun getPricesUseCase(): GetPricesUseCase
}