package menus

import auth.AuthManager
import auth.LoginResponse
import MenuStack.exitApp
import MenuStack.addMenuToStack
import utils.Utils


class AuthenticationMenu: Menu() {
    override val menuWidth = 60
    private val options = listOf<String>(
        "1. Sign In",
        "2. Sign Up",
        "0. Exit"
    )
    private val authManager = AuthManager()

    override fun run() {
        print(surroundOptions(options))

        when (Utils.readInput("Select an option: ").toIntOrNull()) {
            1 -> signIn()
            2 -> signUp()
            0 -> exitApp()
            else -> println("Invalid option")
        }
    }

    private fun signIn() {
        var response: LoginResponse

        do {
            response = authManager.signIn()

            if (response == LoginResponse.OK) {
                addMenuToStack(MainMenu())
            } else if (response == LoginResponse.FAILED) {
                println("Invalid username or password. Try again or type \\exit to close login screen.")
            }
        } while (response == LoginResponse.FAILED)
    }

    private fun signUp() {
        var response: LoginResponse

        do {
            response = authManager.signUp()

            if (response == LoginResponse.OK) {
                println("User has been registered successfully. Now you can login.")
            } else {
                println("Error registering user. Try again with different username")
            }
        } while (response == LoginResponse.FAILED)
    }
}



