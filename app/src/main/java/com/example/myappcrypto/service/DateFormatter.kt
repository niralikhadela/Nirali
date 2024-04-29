package com.example.myappcrypto.service

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormatter {
    private val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("dd MMM HH:mm", Locale.getDefault())

    fun formatDate(timestamp: Long): String = dateFormat.format(Date(timestamp))
    fun formatLastUpdatedTime(time: Long): String = timeFormat.format(Date(time))
}
