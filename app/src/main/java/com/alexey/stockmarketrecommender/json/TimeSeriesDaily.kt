package com.alexey.stockmarketrecommender.json

import com.google.gson.annotations.SerializedName
import kotlin.random.Random

// data was taken from https://rapidapi.com/dubois4and/api/quotient/

data class TimeSeriesDaily(
    @SerializedName("Date")
    val Date: String = "",
    @SerializedName("Open")
    val Open: Float = 0f,
    @SerializedName("High")
    val High: Float = 0f,
    @SerializedName("Low")
    val Low: Float = 0f,
    @SerializedName("Close")
    val Close: Float = 0f,
    @SerializedName("Volume")
    val Volume: Int = 0,
    @SerializedName("Social")
    val Social: Int = Random.nextInt(20, 100)
)
