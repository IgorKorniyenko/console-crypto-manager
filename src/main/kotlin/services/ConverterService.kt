package services

import models.enums.CoinName
import models.enums.CurrencyCode
import repository.CoinGeckoRepository

class ConverterService {
    suspend fun doConversion(coinA: CoinName, amountA: Double, coinB: CoinName): Double? {
        var convertedAmount: Double? = null

        val tokensString = "$coinA, $coinB"
        val pricesFromApi = CoinGeckoRepository().getCryptoPrice(tokensString, CurrencyCode.USD.toString())

        val aCoinPrice = pricesFromApi[coinA.toString()]?.get(CurrencyCode.USD.toString()) ?: 0.0
        val bCoinPrice = pricesFromApi[coinB.toString()]?.get(CurrencyCode.USD.toString()) ?: 0.0

        if (aCoinPrice != 0.0 && bCoinPrice != 0.0) {
            val aCoinTotal = aCoinPrice * amountA
            convertedAmount = aCoinTotal / bCoinPrice
        }

        return convertedAmount
    }
}