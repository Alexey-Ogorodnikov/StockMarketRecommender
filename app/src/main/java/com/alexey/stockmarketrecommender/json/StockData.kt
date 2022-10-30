package com.alexey.stockmarketrecommender.json

data class StockData(
    val stockName: String,
    val daysWindow: String,
    val timeSeriesDaily: ArrayList<TimeSeriesDaily>)