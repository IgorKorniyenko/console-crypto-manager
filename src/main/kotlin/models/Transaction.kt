package models

data class Transaction(val id: Int, val userId: Int, val coinName: String, val operation: String, val quantity: Double)