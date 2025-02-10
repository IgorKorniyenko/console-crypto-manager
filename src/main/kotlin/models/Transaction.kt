package models

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Transaction(
    val id: Int,
    val userId: Int,
    val coinName: String,
    val operation: String,
    val quantity: Double,
    val transactionDate: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))