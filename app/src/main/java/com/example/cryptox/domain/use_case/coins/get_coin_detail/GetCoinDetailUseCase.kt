package com.example.cryptox.domain.use_case.coins.get_coin_detail

import android.net.http.HttpException
import com.example.cryptox.common.Resource
import com.example.cryptox.data.remote.dto.toCoinDetail
import com.example.cryptox.domain.model.CoinDetail
import com.example.cryptox.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject

class GetCoinDetailUseCase @Inject constructor(
    private val repository: CoinRepository,
) {
    operator fun invoke(coin: String): Flow<Resource<CoinDetail>> = flow {
        try {
            emit(Resource.Loading())
            val coinDetail = repository.getCoinDetails(coin)
            emit(Resource.Success(data = coinDetail.toCoinDetail()))
        } catch(e: HttpException) {
            emit(Resource.Error(message = e.localizedMessage ?: "An unexpected error has occurred!"))
        } catch(e: IOException) {
            emit(Resource.Error(message = "An error has occurred, check your internet connection!"))
        }
    }
}