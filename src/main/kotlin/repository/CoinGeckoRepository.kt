package repository

import network.CryptoApiService
import network.RetrofitClient
import utils.ScreenManager

class CoinGeckoRepository {
    private val apiService = RetrofitClient.instance.create(CryptoApiService::class.java)

    suspend fun getCryptoPrice(cryptoIds: String, currency: String): Map<String, Map<String, Double>> {
        var response = mapOf<String,Map<String, Double>>()
        try {
            response = apiService.getCryptoPrice(cryptoIds, currency)
        } catch (e: Exception) {
            //
        }

        return response
    }
}