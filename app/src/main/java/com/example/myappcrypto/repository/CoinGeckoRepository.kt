package com.example.myappcrypto.repository

import com.example.myappcrypto.model.BitcoinPriceData
import com.example.myappcrypto.service.RetrofitClient
import retrofit2.HttpException
import java.io.IOException

class CoinGeckoRepository {
    private val apiService = RetrofitClient.create()

    suspend fun getBitcoinMarketData(currency: String, days: Int): BitcoinPriceData? {
        return try {
            val response = apiService.getBitcoinMarketData(currency, days)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: IOException) {
            null
        } catch (e: HttpException) {
            null
        }
    }
}



