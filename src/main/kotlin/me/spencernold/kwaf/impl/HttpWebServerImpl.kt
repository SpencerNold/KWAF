package me.spencernold.kwaf.impl

import java.util.concurrent.ExecutorService

class HttpWebServerImpl(port: Int, executor: ExecutorService): HttpxWebServer(port, executor, false) {
    override fun reload() {
        TODO("Not implemented yet!")
    }
}