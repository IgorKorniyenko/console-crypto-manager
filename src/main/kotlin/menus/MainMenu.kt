package menus

import MenuStack
import MenuStack.goBack
import Session
import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.input.KeyType
import repository.TransactionsRepository
import services.TransactionService
import utils.ScreenManager
import utils.Utils

class MainMenu: Menu() {
    private val options = listOf(
        "Wallet Management",
        "Statistics and Reports",
        "Movements",
        "Converter",
        "Profile configuration",
        "Close Session"
    )
    private var running = true
    private val screen = ScreenManager.screen
    private val graphics = ScreenManager.graphics
    private var selectedIndex = 0


    override suspend fun run() {
        while (running) {
            drawOptions()
            evaluateOption()
        }
    }

    private fun drawOptions() {
        ScreenManager.clearScreen()
        for (i in options.indices) {
            if (i == selectedIndex) {
                graphics.foregroundColor = TextColor.ANSI.YELLOW
                graphics.putString(10, 5 + i, "> ${options[i]} <")
            } else {
                graphics.foregroundColor = TextColor.ANSI.WHITE
                graphics.putString(10, 5 + i, "  ${options[i]}  ")
            }
        }
        ScreenManager.refreshScreen()
    }

    private suspend fun evaluateOption(){
        val inputKey = screen.readInput()

        when (inputKey.keyType) {
            KeyType.ArrowUp -> if (selectedIndex > 0)  selectedIndex--
            KeyType.ArrowDown -> if (selectedIndex < options.size - 1) selectedIndex++
            KeyType.Enter -> {
                when (selectedIndex) {
                    0 -> walletManagement()
                    1 -> {}
                    2 -> {showMovements()}
                    else -> {
                        running = false
                        MenuStack.goBack()
                    }
                }
            }
            else -> {}
        }
    }

    private suspend fun walletManagement() {
        MenuStack.addMenuToStack(WalletManagementMenu())
    }

    private fun showMovements() {
        val userTransactions = TransactionService().getUserTransactions(Session.currentUser!!.id)
        ScreenManager.clearScreen()

        if (userTransactions.isNotEmpty()) {
            for (i in userTransactions.indices) {
                graphics.putString(10, 5 + i, userTransactions[i].coinName)
                graphics.putString(20, 5 + i, userTransactions[i].operation)
                graphics.putString(30, 5 + i, userTransactions[i].quantity.toString())
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