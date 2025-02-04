package utils

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
}
