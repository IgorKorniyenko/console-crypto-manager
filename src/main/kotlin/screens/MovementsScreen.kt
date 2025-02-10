package screens

import MenuStack
import services.TransactionService
import utils.ScreenManager
import utils.Utils

class MovementsScreen: Screen() {
    private val graphics = ScreenManager.graphics

    override suspend fun run() {
        showMovements()
        MenuStack.goBack()
    }

    private fun showMovements() {
        val userTransactions = TransactionService().getUserTransactions(Session.currentUser!!.id)
        ScreenManager.clearScreen()

        if (userTransactions.isNotEmpty()) {
            for (i in userTransactions.indices) {
                graphics.putString(10, 5 + i, userTransactions[i].coinName)
                graphics.putString(20, 5 + i, userTransactions[i].operation)
                graphics.putString(30, 5 + i, userTransactions[i].quantity.toString())
                graphics.putString(50, 5 + i, userTransactions[i].transactionDate)
            }
            graphics.putString(10, userTransactions.size + 7, "Press ESC key to go back")
            ScreenManager.refreshScreen()
            Utils.readUserInput(4, userTransactions.size + 7, false)
        } else {
            ScreenManager.showError("No transactions found", 10, 5)
            Thread.sleep(1000)
        }
    }
}