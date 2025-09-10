package me.spencernold.kwaf.handlers

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import me.spencernold.kwaf.Route
import me.spencernold.kwaf.exceptions.HandlerException
import me.spencernold.kwaf.firewall.Proxy
import me.spencernold.kwaf.http.HttpRequest
import me.spencernold.kwaf.http.HttpResponse
import me.spencernold.kwaf.util.InputStreams
import java.lang.reflect.Method

class GenericHttpHandler(private val route: Route, private val instance: Any, private val method: Method): Handler(), HttpHandler {

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
        if (exchange.requestMethod == route.method.name) {
            // Request Handling
            val body = InputStreams.readAllBytes(exchange.requestBody)
            if (body.isEmpty() == route.input) {
                exchange.sendResponseHeaders(400, -1)
                exchange.responseBody.close()
                return
            }
            val encoder = route.encoding.getEncoder()
            val response: Any?
            if (method.parameters.size == 1 && method.parameters[0].type == HttpRequest::class.java) {
                val uri = exchange.requestURI
                val parameters = mutableMapOf<String, String>()
                if (uri.query != null) {
                    val values = uri.query.split("&")
                    for (s in values) {
                        if (s.contains("=")) {
                            val pair = s.split("=")
                            parameters[pair[0]] = pair[1]
                        } else {
                            exchange.sendResponseHeaders(400, -1)
                            exchange.responseBody.close()
                            return
                        }
                    }
                }
                val request = HttpRequest(route.method, uri.path, parameters, requestHeaders, body)
                response = method.invoke(instance, request)
            } else if (route.input && method.parameters.size == 1) {
                val request = encoder?.decode(String(body), method.parameters[0].type) ?: String(body)
                response = method.invoke(instance, request)
            } else {
                if (method.parameters.isNotEmpty())
                    throw HandlerException("@Route is not expecting input, but parameters to the ${method.name} exist")
                response = method.invoke(instance)
            }
            // Response Handling
            if (response == null) {
                exchange.sendResponseHeaders(204, -1)
                exchange.responseBody.close()
                return
            } else if (response is HttpResponse) {
                val bytes = response.body
                for (entry in response.headers)
                    exchange.responseHeaders.set(entry.key, entry.value)
                exchange.sendResponseHeaders(response.code, if (bytes.isEmpty() && response.code == 204) -1 else bytes.size.toLong())
                if (bytes.isNotEmpty() ) {
                    exchange.responseBody.write(bytes)
                    exchange.responseBody.flush()
                }
            } else {
                val bytes = (encoder?.encode(response)?.toByteArray() ?: response.toString().toByteArray())
                if (bytes.isEmpty())
                    exchange.sendResponseHeaders(204, -1)
                else {
                    exchange.responseHeaders.set("Content-Type", encoder?.getContentType() ?: "text/plain;")
                    exchange.sendResponseHeaders(200, bytes.size.toLong())
                    exchange.responseBody.write(bytes)
                    exchange.responseBody.flush()
                }
            }
        } else {
            exchange.sendResponseHeaders(400, -1)
        }
        exchange.responseBody.close()
    }
}