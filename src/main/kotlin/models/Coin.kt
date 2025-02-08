package models

import models.enums.CoinName

class Coin(
    val id: Int,
    val userId: Int,
    val coinName: CoinName,
    var quantity: Double,
    val buyValue: Double
)