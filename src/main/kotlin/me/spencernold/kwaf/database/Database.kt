package me.spencernold.kwaf.database

import edu.csus.recipedb.framework.annotation.Instantiated

abstract class Database {

    @Instantiated
    private var driver: Driver? = null

    abstract fun open()

    protected fun <T> getDriver(driver: Class<T>): T {
        return driver.cast(this.driver!!)
    }
}