package me.spencernold.kwaf.services

import me.spencernold.kwaf.WebServer
import me.spencernold.kwaf.database.Driver

class DatabaseService(clazz: Class<*>, private val database: Database): Service(Type.DATABASE, clazz) {

    override fun start(server: WebServer): Any? {
        val url = database.url
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
        if (instance !is Driver) {
            logger.error("${clazz.name} must implement the Database class in order to be registered as a service")
        }
        val credentials = (instance as Driver).loadCredentials()
        instance.open(credentials)
        credentials.first.wipe()
        credentials.second.wipe()
        return instance
    }
}