package me.spencernold.kwaf.database.impl

import me.spencernold.kwaf.database.Driver
import me.spencernold.kwaf.logger.Logger
import java.sql.Connection
import java.sql.DriverManager

class SQLiteDriver(url: String, username: String, password: String) : Driver(url, username, password) {

    private lateinit var connection: Connection
    private var open: Boolean = false

    override fun init() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (e: ClassNotFoundException) {
            Logger.getSystemLogger().error("Can't open SQLite database, no driver found!")
            return
        }
        connection =
            if (username == "" || password == "") DriverManager.getConnection(url) else DriverManager.getConnection(
                url,
                username,
                password
            )
        open = true
    }

    fun getConnection(): Connection {
        return connection
    }
}