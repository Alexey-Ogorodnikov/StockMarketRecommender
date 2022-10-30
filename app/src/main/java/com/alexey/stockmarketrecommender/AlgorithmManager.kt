package com.alexey.stockmarketrecommender

import com.alexey.stockmarketrecommender.json.TimeSeriesDaily

class AlgorithmManager(
    val timeSeriesDaily: TimeSeriesDaily,
    val timeSeriesDailyList: ArrayList<TimeSeriesDaily>
) {

    private var algorithm = Algorithm.EASY

    enum class Recommendation(label: String) {
        BUY("BUY"), SELL("SELL"), HOLD("HOLD")
    }

    enum class Algorithm {
        EASY, BASIC, SMART
    }

    fun getRecommendation(): Recommendation {
        return when (algorithm) {
            Algorithm.EASY -> takeDecisionEasy()
            Algorithm.BASIC -> takeDecisionBasic()
            Algorithm.SMART -> takeDecisionSmart()
            else -> takeDecisionEasy()
        }
    }

    public fun selectAlgo(selectedAlgo : Algorithm) {
        algorithm = selectedAlgo
    }

    private fun takeDecisionEasy(): Recommendation {
        if(timeSeriesDaily.Social>80)
            return Recommendation.BUY
        else if(timeSeriesDaily.Social<20)
            return Recommendation.SELL
        else
            return Recommendation.HOLD
    }

    private fun takeDecisionBasic(): Recommendation {
        if (timeSeriesDaily.Social > 80 && timeSeriesDaily.Close > timeSeriesDaily.Open)
            return Recommendation.BUY
        else if(timeSeriesDaily.Social < 20 && timeSeriesDaily.Close < timeSeriesDaily.Open)
            return Recommendation.SELL
        else
            return Recommendation.HOLD
    }

    private fun takeDecisionSmart(): Recommendation {
        if (timeSeriesDaily.Social > 80 && timeSeriesDaily.Close > timeSeriesDaily.Open && timeSeriesDaily.Close > findMediumPrice())
            return Recommendation.BUY
        else if (timeSeriesDaily.Social < 20 && timeSeriesDaily.Close < timeSeriesDaily.Open && timeSeriesDaily.Close < findMediumPrice())
            return Recommendation.SELL
        else
            return Recommendation.HOLD
    }

    private fun findMediumPrice(): Float {
        var mediumPrice = 0f
        var allPrices = 0f
        for(item in timeSeriesDailyList){
            allPrices = allPrices.plus(item.Close)
        }

        if (timeSeriesDailyList.size > 0)
            mediumPrice = allPrices/timeSeriesDailyList.size

        return mediumPrice
    }

}