package me.spencernold.kwaf.handlers

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import me.spencernold.kwaf.firewall.Proxy

class RawStaticHttpHandler(private val bytes: ByteArray) : Handler(), HttpHandler {
    override fun handle(exchange: HttpExchange) {
        val requestHeaders = mutableMapOf<String, String>()
        for (entry in exchange.requestHeaders)
            requestHeaders[entry.key] = entry.value.joinToString("; ")
        val localAddress = exchange.localAddress
        val remoteAddress = exchange.remoteAddress
        val result = Proxy.hook(
            Proxy.Context(
                exchange.requestURI,
                exchange.requestMethod,
                requestHeaders,
                localAddress.address.hostAddress,
                localAddress.port,
                remoteAddress.address.hostAddress,
                remoteAddress.port
            )
        )
        if (result == Proxy.Result.BLOCK) {
            exchange.sendResponseHeaders(403, -1)
            exchange.responseBody.close()
            return
        }
        if (result == Proxy.Result.TARPIT) {
            return
        }
        exchange.sendResponseHeaders(200, bytes.size.toLong())
        val output = exchange.responseBody
        output.write(bytes)
        output.flush()
        output.close()
    }
}