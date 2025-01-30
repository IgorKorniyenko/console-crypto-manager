package menus

import MenuStack.goBack
import utils.Utils

class MainMenu: Menu() {
    override val menuWidth = 60
    private val options = listOf<String>(
        "1. Products Management",
        "2. Sell Management",
        "3. Reports and Statistics",
        "0. Close Session"
    )
    override fun run() {
        print(surroundOptions(options))

        when (Utils.readInput("Select an option: ").toIntOrNull()) {
            1 -> println("Number one")
            0 -> goBack()
            else -> println("Invalid option")
        }
    }
}