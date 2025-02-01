package services

import models.Coin
import repository.CoinsRepository

class WalletManagementService {
    fun addAsset(coin: Coin): Boolean {
        val registeredCoin = CoinsRepository.getCoin(coin.coinName)

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
}