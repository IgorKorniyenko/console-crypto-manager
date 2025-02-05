package auth

import Session
import lang.Strings
import models.enums.LoginResponse
import models.enums.CountryCode
import repository.DatabaseController
import repository.UserRepository
import utils.CryptoHelper
import utils.Utils

class AuthManager {
    fun signIn(username: String, password: String): Boolean {
        var success = false

        val user = UserRepository.getUser(username)
        if (user != null) {
            val decryptedPassword = CryptoHelper.decrypt(user.password)
            if (decryptedPassword == password) {
                Session.currentUser = user
                success = true
            }
        }
        return success
    }

    fun isUserRegistered(username: String): Boolean {
        return UserRepository.getUser(username) != null
    }
    fun signUp(username: String, password: String): Boolean {
        var success = false

        val user = UserRepository.getUser(username)
        if (user == null) {
            val encryptedPass = CryptoHelper.encrypt(password)
            success = UserRepository.insertUser(username, encryptedPass)
        }
        return success
    }

}