package utils

import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.graphics.TextGraphics
import com.googlecode.lanterna.screen.Screen
import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory

object ScreenManager {
    private val terminal = DefaultTerminalFactory().createTerminal()
    val screen: Screen = TerminalScreen(terminal)
    val graphics: TextGraphics = screen.newTextGraphics()

    init {
        screen.startScreen()
    }

    fun clearScreen() {
        screen.clear()
    }

    fun refreshScreen() {
        screen.refresh()
    }

    fun stopScreen() {
        screen.stopScreen()
    }
    fun showOkMessage(message: String, x: Int, y: Int) {
        graphics.foregroundColor = TextColor.ANSI.GREEN
        graphics.putString(x, y, message)

        refreshScreen()
    }

    fun showError(errorMessage: String, x: Int, y: Int) {
        graphics.foregroundColor = TextColor.ANSI.RED
        graphics.putString(x, y, errorMessage)

        refreshScreen()
    }

    fun clearError(errorLength: Int, x: Int, y: Int) {
        graphics.foregroundColor = TextColor.ANSI.RED
        graphics.putString(x, y, " ".repeat(errorLength))

        refreshScreen()
    }
}
