import MenuStack.addMenuToStack
import repository.DatabaseController
import auth.AuthenticationMenu
import menus.Menu
import models.enums.CountryCode
import models.User
import utils.ScreenManager

object MenuStack {
    private val menuStack = ArrayDeque<Menu>()

    fun addMenuToStack(menu: Menu) {
        menuStack.addFirst(menu)
        runCurrentMenu()
    }

    fun goBack() {
        if (menuStack.isNotEmpty()) {
            menuStack.removeFirst()
        }

        if (menuStack.isNotEmpty()) {
            if (menuStack.size == 1) Session.closeSession()
            runCurrentMenu()
        } else {
            ScreenManager.stopScreen()
        }
    }

    private fun runCurrentMenu() {
        ScreenManager.clearScreen()
        menuStack.first().run()
        ScreenManager.refreshScreen()
    }

}

object Session {
    var currentUser: User? = null
    var lang = CountryCode.ENG

    fun closeSession() {
        currentUser = null
    }
}

class App {
    fun run() {
        DatabaseController.createDatabaseIfNotExists()

        addMenuToStack(AuthenticationMenu())
        /**
        while (menuStack.isNotEmpty()) {
            actualMenu().run()
        }
        **/
    }
}
fun main() {
    val app = App()
    app.run()
}