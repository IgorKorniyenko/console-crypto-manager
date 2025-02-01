package repository

import java.sql.Connection
import java.sql.DriverManager

object DatabaseController {
    private const val DB_URL = "jdbc:sqlite:src/main/data/mydatabase.db"

    fun connect(): Connection {
        return DriverManager.getConnection(DB_URL)
    }

    fun initDatabase() {
        val createUsersTable = """
        CREATE TABLE IF NOT EXISTS Users (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            username TEXT NOT NULL,
            password TEXT NOT NULL
        );
    """.trimIndent()

        val createCoinsTable = """
        CREATE TABLE IF NOT EXISTS UsersCoins (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            userId INTEGER NOT NULL,
            coinName TEXT NOT NULL,
            quantity DECIMAL(40, 2) NOT NULL,
            buyValue DECIMAL(10,2) NOT NULL,
            FOREIGN KEY (userId) REFERENCES Users(id)
        );
    """.trimIndent()



        connect().use { conn ->
            val stmt = conn.createStatement()
            stmt.execute(createUsersTable)
            stmt.execute(createCoinsTable)
        }
    }

}
