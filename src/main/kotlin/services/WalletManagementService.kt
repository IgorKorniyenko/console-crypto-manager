package services

import kotlinx.coroutines.runBlocking
import models.Coin
import models.Transaction
import models.enums.CoinName
import models.enums.CurrencyCode
import repository.CoinGeckoRepository
import repository.CoinsRepository
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.ceil

class WalletManagementService {
    fun addAsset(coin: Coin): Boolean {
        var result = false
        val registeredCoin = CoinsRepository.getCoin(coin.coinName.toString(), coin.userId)
        val mode: String

        if (registeredCoin != null) {
            registeredCoin.quantity += coin.quantity
            result = CoinsRepository.updateCoin(registeredCoin)
            mode = "ADD"
        } else {
            result = CoinsRepository.insertCoin(coin)
            mode = "NEW"
        }

        if (result) {
            val transaction = Transaction(
                0,
                coin.userId,
                coin.coinName.toString(),
                mode,
                coin.quantity
            )

            TransactionService().addTransaction(transaction)
        }

        return result
    }

    fun checkIfCoinExists(coin: String): Boolean {
        return true
    }

    fun getAssets(id: Int): List<Coin> {
        return CoinsRepository.getUserCoins(id)
    }

    fun getAsset(coinName: String, userId: Int): Coin? {
        return CoinsRepository.getCoin(coinName, userId)
    }

    fun updateAsset(coin: Coin): Boolean {
        var result = false
        val registeredCoin = CoinsRepository.getCoin(coin.coinName.toString(), coin.userId)

        if (registeredCoin != null) {
            result = if (coin.quantity > 0) {
                CoinsRepository.updateCoin(coin)
            } else {
                CoinsRepository.deleteCoin(coin.coinName.toString(), coin.userId )
            }

            if (result) {
                val transaction = Transaction(
                    0,
                    coin.userId,
                    coin.coinName.toString(),
                    "DECREASE",
                    registeredCoin.quantity - coin.quantity
                )

                TransactionService().addTransaction(transaction)
            }
        }
        return result
    }

    fun deleteAsset(coinName: String, userId: Int): Boolean {
        var result = false
        val registeredCoin = CoinsRepository.getCoin(coinName, userId)

        if (registeredCoin != null) {
            result = CoinsRepository.deleteCoin(coinName, userId)

            if (result) {
                val transaction = Transaction(
                    0,
                    userId,
                    coinName,
                    "DELETE",
                    0.0
                )

                TransactionService().addTransaction(transaction)
            }
        }
        return result
    }

    suspend fun getAssetAmountInFiat(coin: CoinName, quantity: Double, currency: CurrencyCode): String {
        var formattedAmount = "0.0 USD"

            val coinPrice = CoinGeckoRepository().getCryptoPrice(coin.toString(), currency.toString())

            coinPrice.forEach { (_, values) ->
                values.forEach { (_, price) ->
                    val roundedAmount = BigDecimal(price * quantity).setScale(2, RoundingMode.HALF_EVEN)
                    formattedAmount = "$roundedAmount $currency"
                }
            }


        return formattedAmount
    }
}