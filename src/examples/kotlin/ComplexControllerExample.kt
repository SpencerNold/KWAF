import me.spencernold.kwaf.*
import me.spencernold.kwaf.http.HttpRequest
import me.spencernold.kwaf.services.Service
import me.spencernold.kwaf.util.InputStreams
import java.util.concurrent.Executors

@Service.Controller
class ComplexController {

    // Route involving getting a page WITH headers
    @Route(method = Http.Method.GET, path = "/string", encoding = Route.Encoding.RAW, contentType = Route.ContentType.HTML)
    fun root(request: HttpRequest): String? {
        val headers = request.headers
        println(headers.size)
        for (entry in headers)
            println(entry.key + ": " + entry.value)
        val input = Resource.get("basic_example.html") ?: return null
        return String(InputStreams.readAllBytes(input))
    }
}

fun main() {
    val server = WebServer.Builder(Protocol.HTTP, 80)
        .executor(Executors.newCachedThreadPool())
        .services(ComplexController::class.java)
        .build()
    server.start()
}