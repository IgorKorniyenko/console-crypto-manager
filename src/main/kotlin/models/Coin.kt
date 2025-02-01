package models

class Coin(
    val id: Int,
    val userId: Int,
    val coinName: String,
    var quantity: Double,
    val buyValue: Double
)