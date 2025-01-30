package menus

import MenuStack
import MenuStack.goBack
import utils.Utils

class MainMenu: Menu() {
    override val menuWidth = 60
    private val options = listOf(
        "1. Wallet Management",
        "2. Statistics and Reports",
        "3. Movements",
        "4. Converter",
        "5. Profile configuration",
        "0. Close Session"
    )
    override fun run() {
        print(surroundOptions(options))

        when (Utils.readInput("Select an option: ").toIntOrNull()) {
            1 -> walletManagement()
            0 -> goBack()
            else -> println("Invalid option")
        }
    }

    private fun walletManagement() {
        MenuStack.addMenuToStack(WalletManagementMenu())
    }
}