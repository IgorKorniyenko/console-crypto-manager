package auth

import MenuStack
import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.input.KeyType
import lang.Strings
import menus.MainMenu
import menus.Menu
import models.enums.CountryCode
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

    override suspend fun run() {
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

    private suspend fun evaluateOption(){
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

    private suspend fun signIn() {
        while (true) {
            ScreenManager.clearScreen()

            val username = promptUserInput("Username: ", 5)
            if (username.isBlank()) return

            val password = promptUserInput("Password: ", 7, true)
            if (password.isBlank()) return

            if (authManager.signIn(username, password)) {
                MenuStack.addMenuToStack(MainMenu())
                return
            } else {
                graphics.foregroundColor = TextColor.ANSI.RED
                graphics.putString(10, 9, "Login failed. Please try again.")
                ScreenManager.refreshScreen()
                Thread.sleep(1000)
            }
        }
    }

    private fun signUp() {
        var signUpRunning = true
        var username = ""
        var password = ""
        var signupError = false

        while (signUpRunning) {
            ScreenManager.clearScreen()

            if (signupError) {
                graphics.foregroundColor = TextColor.ANSI.RED
                graphics.putString(10, 9, Strings.failedSignUp[CountryCode.ENG])
                ScreenManager.refreshScreen()
            }

            while (signUpRunning && (username == "")) {
                username = promptUserInput(Strings.username[CountryCode.ENG]!!, 5)
                if (username.isBlank()) signUpRunning = false

                if (signUpRunning && !checkUsername(username)) {
                    showError(Strings.invalidUsername[CountryCode.ENG]!!)
                    clearLine(5, 20, username.length)
                    username = ""
                } else {
                    clearLine(9, 10, Strings.invalidUsername[CountryCode.ENG]!!.length)
                }
            }

            while (signUpRunning && (password == "")) {
                password = promptUserInput(Strings.password[CountryCode.ENG]!!, 7)
                if (password.isBlank()) signUpRunning = false

                if (signUpRunning && !checkPassword(password)) {
                    showError(Strings.invalidPassword[CountryCode.ENG]!!)
                    clearLine(7, 20, password.length)
                    password = ""
                } else {
                    clearLine(9, 10, Strings.invalidPassword[CountryCode.ENG]!!.length)
                }
            }

            if (authManager.signUp(username, password)) {
                signUpRunning = false
                signupError = false
            } else {
                signupError = true
                username = ""
                password = ""
            }
        }
    }

    private fun promptUserInput(prompt: String, y: Int, isPassword: Boolean = false): String {
        var input = ""

        while (input.isBlank()) {
            graphics.foregroundColor = TextColor.ANSI.WHITE
            graphics.putString(10, y, prompt)
            ScreenManager.refreshScreen()
            input = Utils.readUserInput(20, y, isPassword)

            if (input == "\\exit") return ""
        }

        return input
    }

    private fun showError(errorMessage: String) {
        graphics.foregroundColor = TextColor.ANSI.RED
        graphics.putString(10, 9, errorMessage)

        ScreenManager.refreshScreen()
    }

    private fun clearLine(line: Int, start: Int, cleanLength: Int) {
        graphics.putString(start, line, " ".repeat(cleanLength))
        ScreenManager.refreshScreen()
    }

    private fun checkUsername(username: String): Boolean {
        var result = false
        val nameRegex = Regex(".*[A-Za-z].*[A-Za-z].*[A-Za-z].*")
        if (nameRegex.matches(username)) {
             if (!authManager.isUserRegistered(username)) result = true
        }
        return result
    }

    private fun checkPassword(password: String): Boolean {
        val passwordRegex = Regex(".*[A-Za-z].*[A-Za-z].*[A-Za-z].*")
        return passwordRegex.matches(password)
    }
}



