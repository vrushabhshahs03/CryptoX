package com.example.cryptox.domain.use_case.coins.get_prices

import com.example.cryptox.common.Resource
import com.example.cryptox.data.remote.dto.toCoin
import com.example.cryptox.domain.model.Coin
import com.example.cryptox.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetPricesUseCase @Inject constructor(
    private val repository: CoinRepository,
) {
    operator fun invoke(): Flow<Resource<List<Coin>>> = flow {
        try {
            emit(Resource.Loading())
            val prices = repository.getPrices()
            emit(Resource.Success(data = prices.map { it.toCoin() }))
        } catch(e: HttpException) {
            emit(Resource.Error(message = e.localizedMessage ?: "An unexpected error has occurred!"))
        } catch(e: IOException) {
            emit(Resource.Error(message = "An error has occurred. Try checking your internet connection!"))
        }
    }
}