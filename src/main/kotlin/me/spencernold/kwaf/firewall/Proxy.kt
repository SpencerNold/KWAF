package me.spencernold.kwaf.firewall

import java.lang.reflect.Method
import java.net.URI

class Proxy {

    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.FUNCTION)
    annotation class Hook

    enum class Result {
        ALLOW, BLOCK, TARPIT
    }

    data class Context(val requestURI: URI, val requestMethod: String, val requestHeaders: Map<String, String>, val localAddress: String, val localPort: Int, val remoteAddress: String, val remotePort: Int)

    class HookInstance(internal val instance: Any, internal val method: Method)

    companion object {

        private val hooks = mutableListOf<HookInstance>()
        var defaultResult: Result? = null

        fun hook(context: Context): Result {
            var result = defaultResult ?: Result.ALLOW
            for (hook in hooks) {
                val res = hook.method.invoke(hook.instance, context)
                if (res is Result && res.ordinal > result.ordinal) {
                    result = res
                }
            }
            return result
        }

        fun register(hook: HookInstance) {
            hooks.add(hook)
        }
    }
}