package me.spencernold.kwaf.handlers

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler

class RawStaticHttpHandler(private val bytes: ByteArray) : Handler(), HttpHandler {
    override fun handle(exchange: HttpExchange) {
        exchange.sendResponseHeaders(200, bytes.size.toLong())
        val output = exchange.responseBody
        output.write(bytes)
        output.flush()
        output.close()
    }
}