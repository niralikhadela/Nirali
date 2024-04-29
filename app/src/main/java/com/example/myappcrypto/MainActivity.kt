package com.example.myappcrypto

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.Observer
import com.example.myappcrypto.repository.CoinGeckoRepository
import com.example.myappcrypto.service.DateFormatter.formatDate
import com.example.myappcrypto.view.PriceAdapter
import com.example.myappcrypto.viewModel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var imgView: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var priceAdapter: PriceAdapter
    private lateinit var lastUpdatedTextView: TextView
    private lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        checkOnline()

    }

    private fun init() {
        recyclerView = findViewById(R.id.recyclerView)
        lastUpdatedTextView = findViewById(R.id.lastUpdatedTextView)
        imgView = findViewById(R.id.ivNoInternet)
        viewModel = MainViewModel(CoinGeckoRepository())
        priceAdapter = PriceAdapter()
    }

    private fun checkOnline() {
        if (!isOnline(this)) {
            imgView.visibility = VISIBLE
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show()

        } else {
            if (imgView.visibility == 0) {
                imgView.visibility = GONE
                setRecManager()
                getData()
            } else {
                setRecManager()
                getData()
            }
        }

    }

    private fun setRecManager() {
        recyclerView.adapter = priceAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    private fun getData() {
        viewModel.bitcoinPriceData.observe(this, Observer { data ->
            data?.prices?.let { prices ->

                val filteredPrices = mutableListOf<List<Double>>()
                var previousDate = 0L
                prices.forEach { price ->
                    val currentDate = price[0].toLong()
                    val currentDay =
                        Calendar.getInstance().apply { timeInMillis = currentDate }
                            .get(Calendar.DAY_OF_MONTH).toLong()
                    if (currentDay != previousDate) {
                        filteredPrices.add(price)
                        previousDate = currentDay
                    }
                }

                val sortedPrices = filteredPrices.sortedByDescending { it[0] }

                priceAdapter.clearItems()
                sortedPrices.forEach { price ->
                    val date = formatDate(price[0].toLong())
                    val formattedPrice = "Price: ${price[1]}"
                    priceAdapter.addItem(date, formattedPrice)
                }

            }
        })

        viewModel.lastUpdatedTime.observe(this, Observer { time ->
            lastUpdatedTextView.text = "Last Updated : $time"
        })

        CoroutineScope(Dispatchers.Default).launch {
            viewModel.fetchData()
        }
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }
}