package repository

import models.User

object UserRepository {
    fun insertUser(username: String, password: String): Boolean {
        DatabaseController.connect().use { conn ->
            val stmt = conn.prepareStatement("INSERT INTO Users (username, password) VALUES (?, ?);")
            stmt.setString(1, username)
            stmt.setString(2, password)

            return try {
                stmt.executeUpdate()
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    fun getUsers(): List<User> {
        val users = mutableListOf<User>()
        DatabaseController.connect().use { conn ->
            val stmt = conn.prepareStatement("SELECT id, name, password FROM Users;")
            val rs = stmt.executeQuery()
            while (rs.next()) {
                val user = User(
                    id = rs.getInt("id"),
                    username = rs.getString("username"),
                    password = rs.getString("password")
                )
                users.add(user)
            }
        }
        return users
    }

    fun getUser(username: String): User? {
        var user: User? = null

        DatabaseController.connect().use { conn ->
            val stmt = conn.prepareStatement("SELECT id, username, password FROM Users WHERE username = ?")
            stmt.setString(1, username)

            val rs = stmt.executeQuery()
            if (rs.next()) {
                user = User(
                    id = rs.getInt("id"),
                    username = rs.getString("username"),
                    password = rs.getString("password")
                )
            }
        }
        return user
    }

    fun updateUser(user: User): Boolean {
        return true
    }
}
