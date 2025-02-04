package auth

import MenuStack
import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.input.KeyType
import menus.MainMenu
import menus.Menu
import models.enums.LoginResponse
import utils.ScreenManager
import utils.Utils


class AuthenticationMenu: Menu() {
    private val options = listOf(
        "Sign In",
        "Sign Up",
        "Exit"
    )
    private val authManager = AuthManager()
    private var running = true
    private val screen = ScreenManager.screen
    private val graphics = ScreenManager.graphics
    private var selectedIndex = 0

    override fun run() {
        while (running) {
            drawOptions()
            evaluateOption()
        }
    }

    private fun drawOptions() {
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
    }

    private fun evaluateOption(){
        val inputKey = screen.readInput()

        when (inputKey.keyType) {
            KeyType.ArrowUp -> if (selectedIndex > 0)  selectedIndex--
            KeyType.ArrowDown -> if (selectedIndex < options.size - 1) selectedIndex++
            KeyType.Enter -> {
                when (selectedIndex) {
                    0 -> signIn()
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

    private fun signIn() {
        var signInRunning = true
        var username = ""
        var password = ""
        var loginError = false

        while (signInRunning) {
            ScreenManager.clearScreen()

            if (loginError) {
                graphics.foregroundColor = TextColor.ANSI.RED
                graphics.putString(10, 9, "Login failed. Please try again.")
                ScreenManager.refreshScreen()
            }

            while (signInRunning && (username == "")) {
                graphics.foregroundColor = TextColor.ANSI.WHITE
                graphics.putString(10, 5, "Username: ")
                ScreenManager.refreshScreen()
                username = Utils.readUserInput(20, 5)

                if (username == "\\exit") signInRunning = false
            }
            while (signInRunning && (password == "")) {
                graphics.foregroundColor = TextColor.ANSI.WHITE
                graphics.putString(10, 7, "Password: ")
                ScreenManager.refreshScreen()
                password = Utils.readUserInput(20, 7, true)

                if (password == "\\exit") signInRunning = false
            }

            if (signInRunning) {
                if (authManager.signIn(username, password)) {
                    signInRunning = false
                    loginError = false
                    MenuStack.addMenuToStack(MainMenu())
                } else {
                    username = ""
                    password = ""
                    loginError = true
                }
            }
        }
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



