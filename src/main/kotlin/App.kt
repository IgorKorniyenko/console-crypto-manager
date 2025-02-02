import MenuStack.addMenuToStack
import MenuStack.menuStack
import MenuStack.actualMenu
import repository.DatabaseController
import auth.AuthenticationMenu
import menus.Menu
import models.enums.CountryCode
import models.User

object MenuStack {
    val menuStack = ArrayDeque<Menu>()

    fun addMenuToStack(menu: Menu) {
        menuStack.addFirst(menu)
    }

    fun goBack() {
        menuStack.removeFirst()
        if (menuStack.size == 1) Session.currentUser = null
    }

    fun actualMenu(): Menu {
        return menuStack.first()
    }

    fun reload(menu: Menu) {
        menuStack.removeFirst()
        menuStack.addFirst(menu)
    }

    fun exitApp() {
        println("Closing app..")
        menuStack.clear()
    }
}

object Session {
    var currentUser: User? = null
    val lang = CountryCode.ENG
}

class App {
    fun run() {
        DatabaseController.initDatabase()

        addMenuToStack(AuthenticationMenu())

        while (menuStack.isNotEmpty()) {
            actualMenu().run()
        }
    }
}
fun main() {
    val app = App()
    app.run()
}