package menus

import java.lang.StringBuilder

abstract class Menu {
    open val menuWidth = 40
    private val leftPadding = 5
    private val verticalPadding = 1
    abstract suspend fun run()

    fun surroundOptions(options: List<String>): String {
        val surroundedMenu = StringBuilder()

        surroundedMenu
            .append("_".repeat(menuWidth) + "\n")
            .append("\n".repeat(verticalPadding))

        options.forEach { option ->
            surroundedMenu
                .append(" ".repeat(leftPadding))
                .append(option + "\n")
        }

        surroundedMenu
            .append("\n".repeat(verticalPadding))
            .append("_".repeat(menuWidth) + "\n")

        return surroundedMenu.toString()
    }
}