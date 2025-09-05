package me.spencernold.kwaf.handlers

import me.spencernold.kwaf.Route

abstract class FileHandler: Handler() {
    fun createResponseHeaders(route: Route.File): Map<String, String> {
        val map = mutableMapOf<String, String>()
        if (route.contentType != Route.ContentType.NONE)
            map["Content-Type"] = route.contentType.text
        if (route.cacheControl.isNotEmpty())
            map["Cache-Control"] = route.cacheControl
        return map
    }
}