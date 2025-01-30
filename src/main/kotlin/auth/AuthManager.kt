package auth

import Session
import lang.Strings
import models.CountryCode
import models.Roles
import models.User
import utils.Utils

enum class LoginResponse {
    OK, CANCELLED, FAILED
}

class AuthManager {
    fun signIn(): LoginResponse {

        val username = Utils.readInput(Strings.username[Session.lang]?: Strings.username[CountryCode.ENG]!!)
        if (username == "\\exit") return LoginResponse.CANCELLED

        val password = Utils.readInput(Strings.password[Session.lang]?: Strings.password[CountryCode.ENG]!!)
        if (password == "\\exit") return LoginResponse.CANCELLED

        return if (isUserRegistered(username, password)) {
            LoginResponse.OK
        } else {
            LoginResponse.FAILED
        }
    }

    private fun isUserRegistered(username: String, password: String): Boolean {
        val users = Utils.readUsers()
        val user = users.find { it.username == username }

        if (user != null ) {
            if(user.password == password) {
                Session.currentUser = user
                return true
            }
        }
        return false
    }

    fun signUp(): LoginResponse {
        val username = Utils.readInput(Strings.username[Session.lang]?: Strings.username[CountryCode.ENG]!!)
        if (username == "\\exit") return LoginResponse.CANCELLED

        val password = Utils.readInput(Strings.password[Session.lang]?: Strings.password[CountryCode.ENG]!!)
        if (password == "\\exit") return LoginResponse.CANCELLED

        if (!isUserRegistered(username, password)){
            val users = Utils.readUsers().toMutableList()
            users.add(User(username, password, Roles.USER))
            Utils.saveUsers(users)
            return LoginResponse.OK
        } else {
            return LoginResponse.FAILED
        }
    }

}