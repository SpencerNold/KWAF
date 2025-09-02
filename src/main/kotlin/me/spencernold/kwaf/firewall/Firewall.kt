package me.spencernold.kwaf.firewall

import me.spencernold.kwaf.http.HttpRequest
import java.net.InetSocketAddress
import java.net.URI
import kotlin.reflect.KClass

abstract class Firewall {

    abstract fun call(obj: Firewall.Object): Response

    data class Object(val uri: URI, val address: InetSocketAddress, val request: HttpRequest)

    enum class Response {
        ALLOW, DENY, TARPIT
    }

    @Target(AnnotationTarget.FUNCTION)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Handler(val clazz: KClass<out Firewall>)
}