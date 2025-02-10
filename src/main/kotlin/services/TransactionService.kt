package services

import models.Transaction
import repository.TransactionsRepository

class TransactionService {
    fun addTransaction(transaction: Transaction): Boolean {
        return TransactionsRepository.addTransaction(transaction)
    }

    fun getUserTransactions(userId: Int): List<Transaction> {
        return TransactionsRepository.getUserTransactions(userId)
    }
}