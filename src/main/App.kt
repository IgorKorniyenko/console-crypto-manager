import MenuStack.addMenuToStack
import MenuStack.menuStack
import MenuStack.actualMenu
import menus.AuthenticationMenu
import menus.Menu
import models.CountryCode
import models.User

object MenuStack {
    val menuStack = ArrayDeque<Menu>()

    fun addMenuToStack(menu: Menu) {
        menuStack.addFirst(menu)
    }

    fun goBack() {
        menuStack.removeFirst()
    }

    fun actualMenu(): Menu {
        return menuStack.first()
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