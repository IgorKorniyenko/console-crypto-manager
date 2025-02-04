package auth

import MenuStack
import MenuStack.addMenuToStack
import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.input.KeyType
import menus.MainMenu
import menus.Menu
import models.enums.LoginResponse
import utils.ScreenManager


class AuthenticationMenu: Menu() {
    override val menuWidth = 60
    private val options = listOf(
        "Sign In",
        "Sign Up",
        "Exit"
    )
    private val authManager = AuthManager()
    private var running = true

    override fun run() {
        val screen = ScreenManager.screen
        val graphics = ScreenManager.graphics

        var selectedIndex = 0

        while (running) {
            ScreenManager.clearScreen()

            for (i in options.indices) {
                if (i == selectedIndex) {
                    graphics.foregroundColor = TextColor.ANSI.YELLOW
                    graphics.putString(10, 5 + i, "> ${options[i]} <")
                } else {
                    graphics.foregroundColor = TextColor.ANSI.WHITE
                    graphics.putString(10, 5 + i, "  ${options[i]}  ")
                }
            }

            ScreenManager.refreshScreen()

            val inputKey = screen.readInput()

            when (inputKey.keyType) {
                KeyType.ArrowUp -> if (selectedIndex > 0)  selectedIndex--
                KeyType.ArrowDown -> if (selectedIndex < options.size - 1) selectedIndex++
                KeyType.Enter -> {
                    when (selectedIndex) {
                        0 -> {
                            signIn()
                        }
                        1 -> signUp()
                        else -> {
                            running = false
                            MenuStack.goBack()
                        }
                    }
                }
                else -> {}
            }
        }
    }

    private fun signIn() {
        var response: LoginResponse
        val screen = ScreenManager.screen
        val graphics = ScreenManager.graphics

        graphics.foregroundColor = TextColor.ANSI.WHITE
        graphics.putString(10, 5, "Username: ")
        

        do {
            ScreenManager.clearScreen()

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

            when (response) {
                LoginResponse.OK -> println("User has been registered successfully. Now you can login.")
                LoginResponse.USED -> println("Provided username is already in use. Please choose another one")
                else -> println("Error occurred when saving user. Please try later")
            }

        } while (response == LoginResponse.FAILED || response == LoginResponse.USED)
    }
}



