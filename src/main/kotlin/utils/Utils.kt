package utils

import com.googlecode.lanterna.input.KeyType
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.User
import java.io.File

object Utils {
    fun readInput(prompt: String): String {
        print(prompt)
        return readln()
    }

    fun readUsers(): List<User> {
        val file = File(Routes.instance.usersFile)

        if (!file.exists()) return emptyList()

        val encryptedJson = file.readText()
        val json = CryptoHelper.decrypt(encryptedJson)

        return Json.decodeFromString<List<User>>(json)
    }

    fun saveUsers(users: List<User>): Boolean {
        var success: Boolean
        try {
            val usersJson = Json.encodeToString(users)
            val encryptedJson = CryptoHelper.encrypt(usersJson)

            val file = File(Routes.instance.usersFile)

            file.writeText(encryptedJson)
            success = true
        } catch (e: Exception) {
            //Save log
            println(e)
            success = false
        }
        return success
    }

    fun readUserInput(xPosition: Int, yPosition: Int, maskInput: Boolean = false): String {
        val screen = ScreenManager.screen
        val graphics = ScreenManager.graphics
        val inputBuffer = StringBuilder()
        var running = true

        var cursorX = xPosition
        graphics.putString(8, yPosition, "> ")
        ScreenManager.refreshScreen()

        while (running) {
            val key = screen.readInput()
            when (key.keyType) {
                KeyType.Enter -> {
                    running = false
                }
                KeyType.Backspace -> if (inputBuffer.isNotEmpty()) {
                    inputBuffer.deleteCharAt(inputBuffer.length - 1)
                    cursorX--

                    graphics.putString(cursorX, yPosition, " ")
                    ScreenManager.refreshScreen()
                }
                KeyType.Character -> {
                    inputBuffer.append(key.character)
                    val displayChar = if (maskInput) '*' else key.character

                    graphics.putString(cursorX, yPosition, displayChar.toString())
                    cursorX++
                    ScreenManager.refreshScreen()
                }
                KeyType.Escape -> {
                    running = false
                    inputBuffer.clear().append("\\exit")
                }
                else -> {}
            }
        }
        return inputBuffer.toString()
    }
}