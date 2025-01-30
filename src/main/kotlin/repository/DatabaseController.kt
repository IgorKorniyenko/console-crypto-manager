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
            name TEXT NOT NULL
        );
    """.trimIndent()

        val createCoinsTable = """
        CREATE TABLE IF NOT EXISTS Coins (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            userId INTEGER NOT NULL,
            name TEXT NOT NULL,
            FOREIGN KEY (userId) REFERENCES Users(id)
        );
    """.trimIndent()

        val createCoinValuesTable = """
        CREATE TABLE IF NOT EXISTS CoinValues (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            coinId INTEGER NOT NULL,
            value REAL NOT NULL,
            date TEXT NOT NULL,
            FOREIGN KEY (coinId) REFERENCES Coins(id)
        );
    """.trimIndent()

        connect().use { conn ->
            val stmt = conn.createStatement()
            stmt.execute(createUsersTable)
            stmt.execute(createCoinsTable)
            stmt.execute(createCoinValuesTable)
        }
    }

}
