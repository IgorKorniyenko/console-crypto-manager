package menus

import MenuStack
import MenuStack.goBack
import Session
import models.Coin
import models.enums.WalletManagementResponse
import services.WalletManagementService
import utils.Utils

class WalletManagementMenu: Menu() {
    private val options = listOf(
        "1. View My Assets",
        "2. Add Assets",
        "3. Decrease Assets",
        "4. Remove Assets",
        "0. Back"
    )

    override fun run() {
        print(surroundOptions(options))

        when (Utils.readInput("Select an option: ").toIntOrNull()) {
            1 -> showAssets()
            2 -> addAssets()
            3 -> decreaseAsset()
            4 -> deleteAsset()
            0 -> goBack()
            else -> println("Invalid option")
        }
    }

    private fun showAssets() {
        val walletService = WalletManagementService()
        val userAssets = walletService.getAssets(Session.currentUser!!.id)

        if (userAssets.isNotEmpty()) {
            userAssets.forEach { asset ->
                println("%-5s %-15s %-5s %-3s".format(
                    asset.coinName,
                    asset.quantity,
                    "500.00",
                    "EUR"
                ))
            }
        }
        Utils.readInput("Press enter to back to menu.")
    }

    private fun addAssets() {
        val walletService = WalletManagementService()

        val selectedToken = getCoinName()
        if (selectedToken == "\\exit") return

        val quantity = getQuantity("add")
        if (quantity == 0.0) return

        val originalPrice = getOriginalPrice()
        if (originalPrice == 0.0) return

        val coin = Coin(
            id = 0,
            coinName = selectedToken,
            quantity = quantity,
            buyValue = originalPrice,
            userId = Session.currentUser!!.id
        )

        if (walletService.addAsset(coin)) {
            println("Asset has been added successfully.")
        } else {
            println("Error has been occurred when adding asset. Please try later.")
        }
    }

    private fun getCoinName(): String {
        val walletService = WalletManagementService()
        var isValidInput = false
        var selectedToken: String

        do {
            selectedToken = Utils.readInput("Insert token name: ")
            if (selectedToken == "\\exit") return "\\exit"

            isValidInput = walletService.checkIfCoinExists(selectedToken)

            if (!isValidInput) println("Unknown token name. Try with another crypto.")
        } while (!isValidInput)
        return selectedToken
    }

    private fun getQuantity(mode: String): Double {
        var isValidInput = false
        var convertedQuantity: Double?

        val prompt = if (mode == "add") {
            "Insert bought quantity: "
        } else {
            "Insert quantity to decrease: "
        }

        do {
            val quantity = Utils.readInput(prompt)
            if (quantity == "\\exit") return 0.0

            convertedQuantity = quantity.toDoubleOrNull()

            if (convertedQuantity != null) {
                isValidInput = convertedQuantity > 0
            }
            if (!isValidInput) println("Invalid input. Quantity must be a decimal number and superior to zero.")
        } while (!isValidInput)

        return convertedQuantity?:0.0
    }

    private fun getOriginalPrice(): Double {
        var isValidInput = false
        var convertedPrice: Double? = null

        do {
            val originalPrice = Utils.readInput("Insert total price")
            if (originalPrice == "\\exit") return 0.0

            convertedPrice = originalPrice.toDoubleOrNull()

            if (convertedPrice != null) {
                isValidInput = convertedPrice > 0
            }
            if (!isValidInput) println("Invalid input. Price must be a decimal number and superior to zero.")
        } while (!isValidInput)

        return convertedPrice?:0.0
    }

    private fun decreaseAsset() {
        val walletService = WalletManagementService()

        val selectedToken = getCoinName()
        if (selectedToken == "\\exit") return

        val existingCoin = walletService.getAsset(selectedToken, Session.currentUser!!.id)

        if (existingCoin != null) {
            println("Actual $selectedToken balance: ${existingCoin.quantity}")
            val quantity = getQuantity("decrease")
            if (quantity == 0.0) return
            existingCoin.quantity -= quantity

            if (walletService.updateAsset(existingCoin)) {
                println("$selectedToken quantity has been successfully updated.")
            } else {
                println("$selectedToken has not been updated due to internal error. Please try again later.")
            }
        } else {
            println("You can't decrease $selectedToken balance because you don't have any")
        }
    }

    private fun deleteAsset() {
        val walletService = WalletManagementService()

        val selectedToken = getCoinName()
        if (selectedToken == "\\exit") return

        val existingCoin = walletService.getAsset(selectedToken, Session.currentUser!!.id)

        if (existingCoin != null) {
            if (walletService.deleteAsset(selectedToken, existingCoin.userId)) {
                println("$selectedToken has been removed from you wallet")
            } else {
                println("Error occurred when removing $selectedToken. Please try again later.")
            }
        } else {
            println("You cant delete $selectedToken because you don't have it in you wallet.")
        }
    }
}