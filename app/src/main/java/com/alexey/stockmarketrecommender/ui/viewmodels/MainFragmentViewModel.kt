package com.alexey.stockmarketrecommender.ui.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alexey.stockmarketrecommender.json.StockData

class MainFragmentViewModel : ViewModel() {

    private var _stockData = MutableLiveData<StockData>()
    var stockData : LiveData<StockData> =  _stockData


    fun setupData(stockData: StockData) {
        _stockData.value = stockData
    }

    fun countSocialMediaPosts(): Int {
        var countSocialMediaPosts = 0
        stockData.value?.timeSeriesDaily?.let {
            for (item in it) {
                countSocialMediaPosts = countSocialMediaPosts.plus(item.Social)
            }
        }
        return countSocialMediaPosts
    }

}