package com.example.cryptox.presentation.detail

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cryptox.presentation.components.GeneralTopBar
import com.example.cryptox.presentation.detail.component.ATHSection
import com.example.cryptox.presentation.detail.component.CoinTag
import com.example.cryptox.presentation.detail.component.HeroCard
import com.example.cryptox.presentation.detail.component.PerformanceSection
import com.example.cryptox.presentation.detail.component.StatsSection
import com.example.cryptox.presentation.detail.component.TeamListItem
import com.example.cryptox.presentation.loading.CustomLoader

@Composable
fun CoinDetailScreen(
    modifier: Modifier = Modifier,
    viewmodel: CoinDetailViewModel = hiltViewModel(),
) {
    val state = viewmodel.state.value
    var statsCardExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(modifier = modifier.fillMaxSize()) {
        GeneralTopBar(title = "Details", showBack = true)
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if(state.isLoading) {
                CustomLoader(modifier = Modifier.align(Alignment.Center))
            }
            if(state.error.isNotBlank() and !state.isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        state.error,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(
                                horizontal = 40.dp
                            )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    IconButton(onClick = {
                        viewmodel.refresh()
                    }) {
                        Icon(
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp),
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh Icon"
                        )
                    }
                }
            } else {
                state.coin?.let { coinDetail ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(20.dp)
                    ) {
                        item {
                            state.coinPrice?.let { coinPrice ->
                                val coinId = coinPrice.coinId
                                coinPrice.price?.uSD?.let {
                                    HeroCard(
                                        logo = coinDetail.logo,
                                        title = "${coinDetail.name} (${coinDetail.symbol})",
                                        currentPrice = it.price,
                                        pctChange24h = it.percentChange24h,
                                        onShare = {
                                            viewmodel.shareCoin(context, coinId)
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "Key Statistics",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .animateContentSize(
                                                animationSpec = tween(
                                                    1000,
                                                    easing = LinearOutSlowInEasing
                                                )
                                            ),
                                        shape = RoundedCornerShape(30.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color.White
                                        )
                                    ) {
                                        Column(modifier = Modifier.fillMaxWidth()) {
                                            PerformanceSection(price = it)
                                            StatsSection(
                                                marketCap = it.marketCap,
                                                volume24h = it.volume24h,
                                                percentChange7d = it.percentChange7d,
                                                percentChange30d = it.percentChange30d
                                            )

                                            if(statsCardExpanded) {
                                                ATHSection(
                                                    athPrice = it.athPrice,
                                                    percentFromAth = it.percentFromPriceAth,
                                                    athDate = it.athDate
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Text(
                                                if(statsCardExpanded) "Hide More" else "Show More",
                                                style = MaterialTheme.typography.bodySmall,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(bottom = 20.dp)
                                                    .clickable(
                                                        indication = null,
                                                        interactionSource = remember { MutableInteractionSource() },
                                                        onClick = {
                                                            statsCardExpanded = !statsCardExpanded
                                                        }
                                                    )
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "Description",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = coinDetail.description,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "Tags",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    if(coinDetail.tags.isNotEmpty()){
                                        FlowRow(
                                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                                            verticalArrangement = Arrangement.spacedBy(10.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            coinDetail.tags.forEach { tag ->
                                                CoinTag(tag = tag)
                                            }
                                        }
                                    }else{
                                        Text(
                                            text = "Tags information is not available.",
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "Team Members",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if(coinDetail.team.isNotEmpty()) {
                                        coinDetail.team.forEach {
                                            TeamListItem(
                                                teamMember = it,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(10.dp)
                                            )
                                            HorizontalDivider()
                                        }
                                    } else {
                                        Text(
                                            text = "Team information is not available.",
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}