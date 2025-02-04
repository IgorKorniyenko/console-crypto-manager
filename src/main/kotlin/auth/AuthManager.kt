package auth

import Session
import lang.Strings
import models.enums.LoginResponse
import models.enums.CountryCode
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

    fun signUp(): LoginResponse {
        var response = LoginResponse.OK

        val username = Utils.readInput(Strings.username[Session.lang]?: Strings.username[CountryCode.ENG]!!)
        if (username == "\\exit") response = LoginResponse.CANCELLED

        val user = UserRepository.getUser(username)
        if (user != null) {
            response = LoginResponse.USED
        }

        val password = Utils.readInput(Strings.password[Session.lang]?: Strings.password[CountryCode.ENG]!!)
        if (password == "\\exit") response =  LoginResponse.CANCELLED

        val encryptedPass = CryptoHelper.encrypt(password)

        val result = UserRepository.insertUser(username, encryptedPass)
        return if (result) {
            LoginResponse.OK
        } else {
            LoginResponse.FAILED
        }
    }

}