package com.example.cryptox.domain.use_case.coins.get_coin_price

import android.net.http.HttpException
import com.example.cryptox.common.Resource
import com.example.cryptox.data.remote.dto.toCoin
import com.example.cryptox.domain.model.Coin
import com.example.cryptox.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject

class GetCoinPriceUseCase @Inject constructor(
    private val repository: CoinRepository,
) {
    operator fun invoke(coin: String): Flow<Resource<Coin>> = flow {
        try {
            emit(Resource.Loading())
            val coinPrice = repository.getCoinPrice(coin)
            emit(Resource.Success(data = coinPrice.toCoin()))
        } catch(e: HttpException) {
            emit(Resource.Error(message = e.localizedMessage ?: "An unexpected error has occurred!"))
        } catch(e: IOException) {
            emit(Resource.Error(message = "An error has occurred, check your internet connection!"))
        }
    }
}