package me.spencernold.kwaf.handlers

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import me.spencernold.kwaf.logger.Logger
import me.spencernold.kwaf.util.InputStreams
import java.io.File
import java.io.InputStream
import java.lang.reflect.Method
import java.nio.file.Files

class DynamicFileHttpHandler(private val instance: Any, private val method: Method): Handler(), HttpHandler {

    companion object {
        private val logger = Logger.getSystemLogger()

        fun getHandler(instance: Any, method: Method): DynamicFileHttpHandler? {
            if (method.parameterCount != 0) {
                logger.error("${method.name} in ${instance.javaClass.name} is invalid, dynamic file handlers may not have input arguments")
                return null
            }
            if (method.returnType != InputStream::class.java && method.returnType != File::class.java) {
                logger.error("${method.name} in ${instance.javaClass.name} must return an InputStream or a File to be used as a static file handler")
                return null
            }
            return DynamicFileHttpHandler(instance, method)
        }
    }

    override fun handle(exchange: HttpExchange) {
        if (exchange.requestMethod == "GET") {
            val response = method.invoke(instance)
            if (response == null) {
                logger.error("${method.name} in ${instance.javaClass.name} returns a null page when it shouldn't")
            }
            val bytes = if (response is InputStream) InputStreams.readAllBytes(response) else { if (response is File) Files.readAllBytes(response.toPath()) else ByteArray(0) }

            exchange.sendResponseHeaders(200, bytes.size.toLong())
            val output = exchange.responseBody
            output.write(bytes)
            output.flush()
            output.close()
        } else {
            exchange.sendResponseHeaders(400, -1)
            exchange.responseBody.close()
        }
    }
}