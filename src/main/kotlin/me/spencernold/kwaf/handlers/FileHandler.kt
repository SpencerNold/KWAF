package me.spencernold.kwaf.handlers

import me.spencernold.kwaf.Route
import java.security.MessageDigest

abstract class FileHandler: Handler() {

    fun createResponseHeaders(bytes: ByteArray, route: Route.File): Map<String, String> {
        val map = mutableMapOf<String, String>()
        if (route.contentType != Route.ContentType.NONE)
            map["Content-Type"] = route.contentType.text
        if (route.cacheControl.isNotEmpty()) {
            map["Cache-Control"] = route.cacheControl
            map["ETag"] = md5(bytes)
        }
        return map
    }

    fun md5(bytes: ByteArray): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(bytes)
        return digest.joinToString("") { "%02x".format(it) }
    }
}