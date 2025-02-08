package services

import models.Coin
import repository.CoinsRepository

class WalletManagementService {
    fun addAsset(coin: Coin): Boolean {
        val registeredCoin = CoinsRepository.getCoin(coin.coinName.toString(), coin.userId)

        if (registeredCoin != null) {
            registeredCoin.quantity += coin.quantity
            return CoinsRepository.updateCoin(registeredCoin)
        } else {
            return CoinsRepository.insertCoin(coin)
        }
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
            if (coin.quantity > 0) {
                result = CoinsRepository.updateCoin(coin)
            } else {
                result = CoinsRepository.deleteCoin(coin.coinName.toString(), coin.userId )
            }
        }
        return result
    }

    fun deleteAsset(coinName: String, userId: Int): Boolean {
        var result = false
        val registeredCoin = CoinsRepository.getCoin(coinName, userId)

        if (registeredCoin != null) {
            result = CoinsRepository.deleteCoin(coinName, userId)
        }
        return result
    }
}