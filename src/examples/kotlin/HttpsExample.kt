import me.spencernold.kwaf.Protocol
import me.spencernold.kwaf.Resource
import me.spencernold.kwaf.Route
import me.spencernold.kwaf.WebServer
import me.spencernold.kwaf.services.Service
import java.io.InputStream
import java.util.concurrent.Executors

@Service.Controller
class Controller {

    @Route.File(path = "/")
    fun root(): InputStream? {
        return Resource.get("basic_example.html")
    }
}

fun main() {
    val server = WebServer.Builder(Protocol.HTTPS, 443)
        .executor(Executors.newCachedThreadPool())
        .services(Controller::class.java)
        .build()
    server.start()
}