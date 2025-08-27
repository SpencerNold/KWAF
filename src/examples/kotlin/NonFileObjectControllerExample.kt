import me.spencernold.kwaf.Http
import me.spencernold.kwaf.Protocol
import me.spencernold.kwaf.Route
import me.spencernold.kwaf.WebServer
import me.spencernold.kwaf.services.Service
import java.util.concurrent.Executors

@Service.Controller
class NonFileObjectController {

    // Returns JSON Object of a string
    // Objects are encoded before being sent, default is JSON
    @Route(method = Http.Method.GET, path = "/string")
    fun root(): String {
        return "Example String"
    }

    // Accepts a POST request
    // functions without a return value respond with a 204 response code
    @Route(method = Http.Method.POST, path = "/post_request")
    fun postRequest() {
    }
}

fun main() {
    val server = WebServer.Builder(Protocol.HTTP, 80)
        .executor(Executors.newCachedThreadPool())
        .services(NonFileObjectController::class.java)
        .build()
    server.start()
}