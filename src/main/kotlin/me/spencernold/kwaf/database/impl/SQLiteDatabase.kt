package me.spencernold.kwaf.database.impl

import me.spencernold.kwaf.database.Database
import java.sql.Connection

abstract class SQLiteDatabase() : Database() {

    override fun open() {
        getDriver(SQLiteDriver::class.java).init()
    }

    fun getConnection(): Connection {
        return getDriver(SQLiteDriver::class.java).getConnection()
    }
}