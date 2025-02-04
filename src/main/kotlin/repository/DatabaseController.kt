package repository

import java.io.File
import java.sql.Connection
import java.sql.DriverManager

object DatabaseController {
    //private val dbPath = "jdbc:sqlite:${System.getProperty("user.dir")}/data/mydatabase.db"
    private val dbPath = "jdbc:sqlite:src/main/data/mydatabase.db"
    fun connect(): Connection {
        Class.forName("org.sqlite.JDBC")
        return DriverManager.getConnection(dbPath)
    }

    fun createDatabaseIfNotExists() {
        val dbFile = File(dbPath)

        if (!dbFile.parentFile.exists()) {
            dbFile.parentFile.mkdirs()
        }

        if (!dbFile.exists()) {
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
}
