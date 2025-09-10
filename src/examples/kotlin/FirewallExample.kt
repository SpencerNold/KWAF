import me.spencernold.kwaf.Protocol
import me.spencernold.kwaf.Resource
import me.spencernold.kwaf.Route
import me.spencernold.kwaf.WebServer
import me.spencernold.kwaf.firewall.Proxy
import me.spencernold.kwaf.services.Service
import java.io.InputStream
import java.util.concurrent.Executors

@Service.Controller
class ExampleFirewallController {

    // Returns /resources/basic_example.html
    @Route.File(path = "/")
    fun root(): InputStream? {
        return Resource.get("basic_example.html")
    }
}

@Service.Firewall
class ExampleFirewall {

    @Proxy.Hook
    fun hook(context: Proxy.Context): Proxy.Result {
        println(context.requestURI)
        println("From: ${context.remoteAddress}:${context.remotePort}")
        return Proxy.Result.ALLOW // BLOCK returns a 403 error, TARPIT pretends like the server doesn't exist
    }
}

fun main() {
    val server = WebServer.Builder(Protocol.HTTP, 80)
        .executor(Executors.newCachedThreadPool())
        .services(ExampleFirewallController::class.java, ExampleFirewall::class.java)
        .build()
    server.start()
}