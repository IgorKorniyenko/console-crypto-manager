package network

import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoApiService {
    @GET("simple/price")
    suspend fun getCryptoPrice(
        @Query("ids") cryptoIds: String,
        @Query("vs_currencies") currency: String
    ): Map<String, Map<String, Double>>
}