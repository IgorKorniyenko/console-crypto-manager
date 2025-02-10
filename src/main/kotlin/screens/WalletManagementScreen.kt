package screens

import MenuStack
import Session
import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.input.KeyType
import models.Coin
import models.enums.CoinName
import models.enums.CurrencyCode
import services.WalletManagementService
import utils.ScreenManager
import utils.Utils
import kotlinx.coroutines.*

class WalletManagementScreen: Screen() {
    private val options = listOf(
        "View My Assets",
        "Add Assets",
        "Decrease Assets",
        "Remove Assets",
        "Back"
    )
    private var running = true
    private val screen = ScreenManager.screen
    private val graphics = ScreenManager.graphics
    private var selectedIndex = 0
    private val walletManagementService = WalletManagementService()
    private var lastY = 0
    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.Default + job)

    override suspend fun run() {
        while (running) {
            drawOptions(options)
            evaluateOption()
        }
        coroutineScope.cancel()
    }

    private fun drawOptions(options: List<String>, isMainMenu: Boolean = true) {
        ScreenManager.clearScreen()
        var x = 10
        if (!isMainMenu) x = 8
        for (i in options.indices) {
            if (i == selectedIndex) {
                graphics.foregroundColor = TextColor.ANSI.YELLOW
                graphics.putString(x, 5 + i, "> ${options[i]} <")
            } else {
                graphics.foregroundColor = TextColor.ANSI.WHITE
                graphics.putString(x, 5 + i, "  ${options[i]}  ")
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
                    0 -> showAssets()
                    1 -> {
                        selectedIndex = 0
                        addAssets()
                    }
                    2 -> {
                        selectedIndex = 0
                        decreaseAsset()
                    }
                    3 -> {
                        selectedIndex = 0
                        deleteAsset()
                    }
                    else -> {
                        running = false
                        MenuStack.goBack()
                    }
                }
            }
            else -> {}
        }
    }

    private suspend fun showAssets() {
        val userAssets = walletManagementService.getAssets(Session.currentUser!!.id)

        ScreenManager.clearScreen()

        if (userAssets.isNotEmpty()) {
            for (i in userAssets.indices) {
                graphics.foregroundColor = TextColor.ANSI.WHITE
                graphics.putString(10, 5 + i, userAssets[i].coinName.toString())
                graphics.putString(25, 5 + i, userAssets[i].quantity.toString())
                graphics.putString(50, 5 + i, "...")
                coroutineScope.launch {
                    printFormattedAmount(userAssets[i], 5 + i)
                }

            }
            graphics.putString(10, userAssets.size + 7, "Press ESC key to go back")
            ScreenManager.refreshScreen()
            Utils.readUserInput(4, userAssets.size + 7, false)
        } else {
            ScreenManager.showError("Can't found assets for this user.", 10, 5)
            Thread.sleep(1000)
        }

    }

    private suspend fun printFormattedAmount(coin: Coin, y: Int) {
        val formattedAmount = walletManagementService.getAssetAmountInFiat(coin.coinName, coin.quantity, CurrencyCode.USD)
        graphics.putString(50, y, formattedAmount)
        ScreenManager.refreshScreen()

    }

    private fun addAssets() {
        val walletService = WalletManagementService()

        val selectedCoin = selectCoinFromMenu() ?: return
        val quantity = getCoinQuantityFromUser() ?: return

        val coin = Coin(
            id = 0,
            coinName = selectedCoin,
            quantity = quantity,
            buyValue = 10.0,
            userId = Session.currentUser!!.id
        )

        ScreenManager.clearScreen()
        if (walletService.addAsset(coin)) {
            ScreenManager.showOkMessage("$quantity ${selectedCoin.toString()} has been added to wallet", 10, 5)
        } else {
            ScreenManager.showError("Error has occurred when adding coin. Please try again later.", 10, 5)
        }
        Thread.sleep(1000)

    }

    private fun decreaseAsset() {
        ScreenManager.clearScreen()

        val selectedToken = selectCoinFromMenu(false) ?: return
        val quantity = getCoinQuantityFromUser() ?: return

        val tokenToDecrease = walletManagementService.getAsset(selectedToken.toString(), Session.currentUser!!.id)

        if (tokenToDecrease != null) {
            if (tokenToDecrease.quantity >= quantity) {
                tokenToDecrease.quantity -= quantity
                if (walletManagementService.updateAsset(tokenToDecrease)) {
                    ScreenManager.showOkMessage("$selectedToken has been updated", 10, lastY + 10)
                } else {
                    ScreenManager.showError("Error updating $selectedToken quantity. Please try again later", 10, lastY + 10)
                }
            } else {
                ScreenManager.showError("Available $selectedToken amount is lower than introduced quantity", 10, lastY + 10)
            }
        } else {
            ScreenManager.showError("Internal error", 10, 10)
        }
        Thread.sleep(1000)
    }

    private fun selectCoinFromMenu(isAddingAssets: Boolean = true): CoinName? {
        var selectedCoin: CoinName? = null

        val availableCoins = getCoinsList(isAddingAssets)

        if (availableCoins.isNotEmpty()) {
            lastY = availableCoins.size
            while (selectedCoin == null) {
                drawOptions(availableCoins, false)
                val coinIsSelected = evaluateCoinMenuOption() ?: return null

                if (coinIsSelected) {
                    selectedCoin = if (isAddingAssets) {
                        CoinName.valueOf(availableCoins[selectedIndex])
                    } else {
                        walletManagementService
                            .getAssets(Session.currentUser!!.id)
                            .map { it.coinName }
                            .distinct()[selectedIndex]
                    }

                    showSelectedAssetAmount(selectedCoin.toString(), lastY + 6)
                    selectedIndex = 0
                }
            }
        } else if (!isAddingAssets){
            ScreenManager.showError("Can't found assets for this user.", 10, 5)
            Thread.sleep(1000)
        }

        return selectedCoin
    }

    private fun getCoinsList(isAddingAssets: Boolean): List<String> {
        return if (isAddingAssets) {
            CoinName.entries.toTypedArray().map { it.toString() }
        } else {
            walletManagementService
                .getAssets(Session.currentUser!!.id)
                .map { it.coinName.toString() }
                .distinct()
        }
    }

    private fun evaluateCoinMenuOption(): Boolean?{
        val inputKey = screen.readInput()
        var coinIsSelected: Boolean? = false

        when (inputKey.keyType) {
            KeyType.ArrowUp -> if (selectedIndex > 0)  selectedIndex--
            KeyType.ArrowDown -> if (selectedIndex < CoinName.entries.size - 1) selectedIndex++
            KeyType.Enter -> coinIsSelected = true
            KeyType.Escape -> coinIsSelected = null
            else -> {}
        }

        return coinIsSelected
    }

    private fun showSelectedAssetAmount(coinName: String, y: Int) {
        val coin = walletManagementService.getAsset(coinName, Session.currentUser!!.id)

        if (coin != null) {
            ScreenManager.showOkMessage("You have ${coin.coinName} ${coin.quantity} available", 10, y)
        } else {
            ScreenManager.showError("You're $coinName balance is 0", 10, y)
        }

        Thread.sleep(1000)
    }

    private fun getCoinQuantityFromUser(): Double? {
        var convertedQuantity: Double? = null
        while (convertedQuantity == null) {
            try {
                val quantity = Utils.promptUserInput("Amount: ", lastY + 7)
                if (quantity.isBlank()) return null
                convertedQuantity = quantity.toDouble()
            } catch (e: NumberFormatException) {
                ScreenManager.showError("Invalid amount. Try again with a number", 10, lastY + 8)
                graphics.putString(10, lastY + 7, " ".repeat(50))
                Thread.sleep(1000)
            } finally {
                ScreenManager.clearError(41, 10, lastY + 8)
            }
        }
        return convertedQuantity
    }

    private fun deleteAsset() {
        ScreenManager.clearScreen()

        val selectedToken = selectCoinFromMenu(false) ?: return

        val result = walletManagementService.deleteAsset(selectedToken.toString(), Session.currentUser!!.id)

        if (result) {
            ScreenManager.showOkMessage("$selectedToken has been successfully deleted", 10, lastY + 7)
        } else {
            ScreenManager.showError("Error has occurred when deleting $selectedToken", 10, lastY + 7)
        }
        Thread.sleep(1000)
    }

}