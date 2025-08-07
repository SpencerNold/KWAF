package me.spencernold.kwaf.services

import me.spencernold.kwaf.WebServer
import me.spencernold.kwaf.annotation.Instantiated

abstract class Implementation {

    @Instantiated
    private var server: WebServer? = null

    protected fun <T> getService(clazz: Class<T>): T {
        return server!!.service(clazz)!!
    }
}