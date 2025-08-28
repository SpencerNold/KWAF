package me.spencernold.kwaf.impl

import com.sun.net.httpserver.HttpsConfigurator
import com.sun.net.httpserver.HttpsServer
import me.spencernold.kwaf.Resource
import java.io.FileNotFoundException
import java.security.KeyStore
import java.security.SecureRandom
import java.util.concurrent.ExecutorService
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext

class HttpsWebServerImpl(port: Int, executor: ExecutorService): HttpxWebServer(port, executor, true) {

    override fun start() {
        val server = this.server as HttpsServer

        val password = if (System.getenv("TLS_KEYSTORE_PASSWD") == null) {
            val console = System.console() ?: throw IllegalArgumentException("TLS_KEYSTORE_PASSWD not set in env")
            console.readPassword("TLS KeyStore Password: ")
        } else {
            System.getenv("TLS_KEYSTORE_PASSWD").toCharArray()
        }
        val keyStoreFile = Resource.get("keystore.jks") ?: throw FileNotFoundException("https impl must have a /resources/keystore.jks")
        val ks = KeyStore.getInstance("JKS")
        ks.load(keyStoreFile, password)

        val kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
        kmf.init(ks, password)

        val context = SSLContext.getInstance("TLS")
        context.init(kmf.keyManagers, null, SecureRandom.getInstanceStrong())

        server.httpsConfigurator = HttpsConfigurator(context)

        for (i in password.indices)
            password[i] = '\u0000'

        super.start()
    }

    override fun reload() {
        TODO("Not yet implemented")
    }
}