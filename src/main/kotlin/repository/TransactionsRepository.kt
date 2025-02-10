package repository

import models.Transaction
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object TransactionsRepository {
    fun addTransaction(transaction: Transaction): Boolean {
        DatabaseController.connect().use { conn ->
            val stmt = conn.prepareStatement("INSERT INTO Transactions (userId, coinName, operation, quantity, transactionDate) VALUES (?, ?, ?, ?, ?)")
            stmt.setInt(1, transaction.userId)
            stmt.setString(2, transaction.coinName)
            stmt.setString(3, transaction.operation)
            stmt.setDouble(4, transaction.quantity)
            stmt.setString(5, transaction.transactionDate)

            return try {
                stmt.executeUpdate()
                true
            } catch (e: Exception) {
                println(e)
                false
            }
        }
    }

    fun getUserTransactions(userId: Int): List<Transaction> {
        val transactionList = mutableListOf<Transaction>()

        DatabaseController.connect().use { conn ->
            val stmt = conn.prepareStatement("SELECT id, userId, coinName, operation, quantity, transactionDate FROM Transactions WHERE userId = ?")
            stmt.setInt(1, userId)

            val rs = stmt.executeQuery()

            while (rs.next()) {
                transactionList.add(Transaction(
                    rs.getInt("id"),
                    rs.getInt("userId"),
                    rs.getString("coinName"),
                    rs.getString("operation"),
                    rs.getDouble("quantity"),
                    rs.getString("transactionDate")
                ))
            }
        }

        return transactionList
    }
}