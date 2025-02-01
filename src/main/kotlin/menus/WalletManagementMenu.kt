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
        "3. Modify Assets",
        "4. Remove Assets",
        "0. Back"
    )

    override fun run() {
        print(surroundOptions(options))

        when (Utils.readInput("Select an option: ").toIntOrNull()) {
            1 -> showAssets()
            2 -> addAssets()
            0 -> goBack()
            else -> println("Invalid option")
        }
    }

    private fun addAssets() {
        val walletService = WalletManagementService()

        val selectedToken = getCoinName()
        if (selectedToken == "\\exit") return

        val quantity = getQuantity()
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

    private fun getQuantity(): Double {
        var isValidInput = false
        var convertedQuantity: Double?

        do {
            val quantity = Utils.readInput("Insert bought quantity: ")
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

    private fun showAssets() {
        val walletService = WalletManagementService()
        val userAssets = walletService.getAssets(Session.currentUser!!.id)

        if (userAssets.isNotEmpty()) {
            userAssets.forEach { asset ->
                println("%-3s %-10s %-5s %-5s %-3s".format(
                    asset.id,
                    asset.coinName,
                    asset.quantity,
                    "500.00",
                    "EUR"
                ))
            }
        }
        Utils.readInput("Press enter to back to menu.")
    }
}