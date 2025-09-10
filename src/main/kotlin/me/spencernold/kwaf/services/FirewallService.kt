package me.spencernold.kwaf.services

import me.spencernold.kwaf.WebServer
import me.spencernold.kwaf.firewall.Proxy

class FirewallService(clazz: Class<*>, private val firewall: Service.Firewall) : Service(Type.CONTROLLER, clazz) {

    override fun start(server: WebServer): Any? {
        val instance = try {
            clazz.getDeclaredConstructor().newInstance()
        } catch (e: NoSuchMethodException) {
            logger.error("Unable to start controller of class: ${clazz.name}")
            return null
        }
        implement(instance, server)
        Proxy.defaultResult = firewall.defaultResult
        for (method in clazz.declaredMethods) {
            if (method.isAnnotationPresent(Proxy.Hook::class.java)) {
                if (method.parameterCount == 1 && method.parameters[0].type == Proxy.Context::class.java) {
                    Proxy.register(Proxy.HookInstance(instance, method))
                }
            }
        }
        return instance
    }
}