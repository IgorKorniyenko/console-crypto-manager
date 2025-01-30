package utils

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
}