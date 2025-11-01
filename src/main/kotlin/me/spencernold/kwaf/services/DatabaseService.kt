package me.spencernold.kwaf.services

import me.spencernold.kwaf.WebServer
import me.spencernold.kwaf.database.Driver
import me.spencernold.kwaf.translator.SystemPropertyTranslator

class DatabaseService(clazz: Class<*>, private val database: Database): Service(Type.DATABASE, clazz) {

    private val translator = SystemPropertyTranslator()

    override fun start(server: WebServer): Any? {
        val url = translator.translate(database.url)
        val username = translator.translate(database.username)
        val password = translator.translate(database.password)
        if (url == "") {
            logger.error("unable to connect to database: invalid url")
            return null
        }
        val instance = try {
            clazz.getDeclaredConstructor().newInstance()
        } catch (e: NoSuchMethodException) {
            logger.error("unable to start database of class: ${clazz.name}")
            return null
        }
        implement(instance, server)
        if (instance !is me.spencernold.kwaf.database.Database) {
            logger.error("${clazz.name} must implement the Database class in order to be registered as a service")
        }
        val driver = Driver.Factory(database.driver).create(url, username, password)
        val field = me.spencernold.kwaf.database.Database::class.java.getDeclaredField("driver")
        field.isAccessible = true
        field.set(instance, driver)
        (instance as me.spencernold.kwaf.database.Database).open()
        return instance
    }
}