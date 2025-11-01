package me.spencernold.kwaf.database

import me.spencernold.kwaf.database.impl.SQLiteDriver

abstract class Driver(protected val url: String, protected val username: String, protected val password: String) {

    abstract fun init()

    data class Factory(private val type: Type) {
        fun create(url: String, username: String, password: String): Driver {
            return when (type) {
                Type.SQLITE -> SQLiteDriver(url, username, password)
            }
            //throw NotImplementedError("Database Support does not exist yet!")
        }
    }

    enum class Type {
        SQLITE
    }
}