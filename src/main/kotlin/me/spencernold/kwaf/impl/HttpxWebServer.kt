package me.spencernold.kwaf.impl

import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import com.sun.net.httpserver.HttpsServer
import me.spencernold.kwaf.WebServer
import me.spencernold.kwaf.handlers.Handler
import me.spencernold.kwaf.logger.Logger
import java.net.InetSocketAddress
import java.util.concurrent.ExecutorService

abstract class HttpxWebServer(port: Int, private val executor: ExecutorService, secure: Boolean) : WebServer(port) {

    private val logger: Logger = Logger.getSystemLogger()
    private val handlers: MutableMap<String, Handler> = mutableMapOf()
    private val services: MutableMap<Class<*>, Any> = mutableMapOf()
    private var running: Boolean = false
    protected var server: HttpServer = if (secure) HttpsServer.create(InetSocketAddress(port), 0) else HttpServer.create(InetSocketAddress(port), 0)

    override fun start() {
        if (running)
            return
        logger.info("Starting server on port: $port")
        server.executor = executor
        running = true
        server.start()
    }

    override fun stop() {
        if (!running)
            return
        running = false
        logger.info("Shutting down server...")
        server.stop(0)
    }

    override fun running(): Boolean {
        return running
    }

    override fun addHandler(path: String, handler: Handler) {
        if (handler !is HttpHandler) {
            logger.warn("Unable to create handler at path \"$path\"")
            return
        }
        server.createContext(path, handler as HttpHandler)
        handlers[path] = handler
        logger.info("Added handler at path \"$path\"")
    }

    override fun getInternalServerObject(): Any {
        return server
    }

    override fun register(service: Any) {
        services[service.javaClass] = service
    }

    override fun <T> service(clazz: Class<T>): T? {
        val service = services[clazz]
        return if (service == null) {
            logger.error("no such service of type: ${clazz.name}")
            null
        } else if (!clazz.isInstance(service)) {
            logger.error("no such service of type: ${clazz.name}")
            null
        } else {
            clazz.cast(service)
        }
    }

    override fun <T : Annotation> getServicesFromType(clazz: Class<T>): Array<Class<*>> {
        val services = services.keys
        val list = mutableListOf<Class<*>>()
        for (service in services) {
            if (service.isAnnotationPresent(clazz))
                list.add(service)
        }
        return list.toTypedArray()
    }
}