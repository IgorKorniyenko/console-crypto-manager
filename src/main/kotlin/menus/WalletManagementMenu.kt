package menus

import MenuStack.goBack
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
            1 -> println("First option")
            2 -> addAssets()
            0 -> goBack()
            else -> println("Invalid option")
        }
    }

    private fun addAssets() {

    }
}