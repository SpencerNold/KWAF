package me.spencernold.kwaf.database

import me.spencernold.kwaf.annotation.Instantiated

abstract class Database {

    @Instantiated
    private var driver: Driver? = null

    abstract fun open()

    protected fun <T> getDriver(driver: Class<T>): T {
        return driver.cast(this.driver!!)
    }
}