package com.example.myappcrypto.service

import com.example.myappcrypto.model.BitcoinPriceData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinGeckoApiService {
    @GET("coins/bitcoin/market_chart")
    suspend fun getBitcoinMarketData(
        @Query("vs_currency") currency: String,
        @Query("days") days: Int
    ): Response<BitcoinPriceData>
}