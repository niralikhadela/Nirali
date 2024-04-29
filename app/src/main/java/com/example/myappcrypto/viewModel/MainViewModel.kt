package com.example.myappcrypto.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myappcrypto.model.BitcoinPriceData
import com.example.myappcrypto.repository.CoinGeckoRepository
import com.example.myappcrypto.service.DateFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val repository: CoinGeckoRepository) : ViewModel() {

    val bitcoinPriceData = MutableLiveData<BitcoinPriceData>()
    val lastUpdatedTime = MutableLiveData<String>()

    init {
        viewModelScope.launch {
            fetchData()
            while (true) {
                delay(10_000)
                fetchData()
            }
        }
    }

    suspend fun fetchData() {
        withContext(Dispatchers.IO) {
            val data = repository.getBitcoinMarketData("eur", 14)
            if (data != null) {
                bitcoinPriceData.postValue(data!!)
                lastUpdatedTime.postValue(DateFormatter.formatLastUpdatedTime(System.currentTimeMillis()))
            }
        }
    }

}