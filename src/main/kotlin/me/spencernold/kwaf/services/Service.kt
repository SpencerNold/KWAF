package me.spencernold.kwaf.services

import me.spencernold.kwaf.WebServer
import me.spencernold.kwaf.database.Driver
import me.spencernold.kwaf.firewall.Proxy
import me.spencernold.kwaf.logger.Logger

abstract class Service(protected val type: Type, protected val clazz: Class<*>) {

    protected val logger: Logger = Logger.getSystemLogger()

    abstract fun start(server: WebServer): Any?

    protected fun implement(instance: Any, server: WebServer) {
        if (instance !is Implementation)
            return
        val field = Implementation::class.java.getDeclaredField("server")
        field.isAccessible = true
        field.set(instance, server)
    }

    enum class Type {
        CONTROLLER, DATABASE, FIREWALL
    }

    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.CLASS)
    annotation class Controller(val path: String = "", val domain: String = "", val domainPriority: Boolean = false)

    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.CLASS)
    annotation class Database(val driver: Driver.Type, val url: String, val username: String = "", val password: String = "")

    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.CLASS)
    annotation class Firewall(val defaultResult: Proxy.Result = Proxy.Result.ALLOW)
}
