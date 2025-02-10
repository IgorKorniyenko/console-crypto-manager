import MenuStack.addMenuToStack
import repository.DatabaseController
import auth.AuthenticationScreen
import screens.Screen
import models.enums.CountryCode
import models.User
import utils.ScreenManager
import kotlin.system.exitProcess

object MenuStack {
    private val menuStack = ArrayDeque<Screen>()

    suspend fun addMenuToStack(menu: Screen) {
        menuStack.addFirst(menu)
        runCurrentMenu()
    }

    suspend fun goBack() {
        if (menuStack.isNotEmpty()) {
            menuStack.removeFirst()
        }

        if (menuStack.isNotEmpty()) {
            if (menuStack.size == 1) Session.closeSession()
            runCurrentMenu()
        } else {
            ScreenManager.stopScreen()
            exitProcess(0)
        }
    }

    private suspend fun runCurrentMenu() {
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
    suspend fun run() {
        DatabaseController.createDatabaseIfNotExists()

        addMenuToStack(AuthenticationScreen())
    }
}
suspend fun main() {
    val app = App()
    app.run()
}