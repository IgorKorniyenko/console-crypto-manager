package repository

import models.Coin
import models.enums.CoinName

object CoinsRepository {
    fun insertCoin(coin: Coin): Boolean {
        DatabaseController.connect().use { conn ->
            val stmt = conn.prepareStatement("INSERT INTO UsersCoins (coinName, quantity, buyValue, userId) VALUES (?, ?, ?, ?);")
            stmt.setString(1, coin.coinName.toString())
            stmt.setDouble(2, coin.quantity)
            stmt.setDouble(3, coin.buyValue)
            stmt.setInt(4, coin.userId)

            return try {
                stmt.executeUpdate()
                true
            } catch (e: Exception) {
                println(e)
                false
            }
        }
    }

    fun getCoin(coinName: String, usrId: Int): Coin? {
        DatabaseController.connect().use { conn ->
            val stmt = conn.prepareStatement("SELECT id, coinName, quantity, buyValue, userId FROM UsersCoins where coinName = ? and userId = ?;")
            stmt.setString(1, coinName)
            stmt.setInt(2, usrId)

            val rs = stmt.executeQuery()

             return if (rs.next()) {
                Coin(
                    id = rs.getInt("id"),
                    coinName = CoinName.valueOf(rs.getString("coinName")),
                    quantity = rs.getDouble("quantity"),
                    buyValue = rs.getDouble("buyValue"),
                    userId = rs.getInt("userId")
                )
            } else {
                null
            }
        }
    }

    fun updateCoin(coin: Coin): Boolean {
        DatabaseController.connect().use { conn ->
            val stmt = conn.prepareStatement("UPDATE UsersCoins SET quantity = ? WHERE coinName = ? AND userId = ?;")
            stmt.setDouble(1, coin.quantity)
            stmt.setString(2, coin.coinName.toString())
            stmt.setInt(3, coin.userId)

            return try {
                stmt.executeUpdate()
                true
            } catch (e: Exception) {
                println(e)
                false
            }
        }
    }

    fun getUserCoins(id: Int): List<Coin> {
        val coinList = mutableListOf<Coin>()

        DatabaseController.connect().use { conn ->
            val stmt = conn.prepareStatement("SELECT id, coinName, quantity, buyValue, userId FROM UsersCoins where userId = ?;")
            stmt.setInt(1, id)

            val rs = stmt.executeQuery()

            while (rs.next()) {
                coinList.add(Coin(
                    id = rs.getInt("id"),
                    coinName = CoinName.valueOf(rs.getString("coinName")),
                    quantity = rs.getDouble("quantity"),
                    buyValue = rs.getDouble("buyValue"),
                    userId = rs.getInt("userId")
                ))
            }
            return coinList
        }
    }

    fun deleteCoin(coinName: String, usrId: Int): Boolean {
        DatabaseController.connect().use { conn ->
            val stmt = conn.prepareStatement("DELETE FROM UsersCoins WHERE coinName = ? AND userId = ?")
            stmt.setString(1, coinName)
            stmt.setInt(2, usrId)

            return try {
                stmt.executeUpdate()
                true
            } catch (e: Exception) {
                println(e)
                false
            }
        }
    }
}