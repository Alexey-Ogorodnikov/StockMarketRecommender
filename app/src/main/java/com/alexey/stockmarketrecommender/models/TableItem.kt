package com.alexey.stockmarketrecommender.models

data class TableItem(
    val price: String,
    val date: String,
    val social: String,
    val recommendation: String)