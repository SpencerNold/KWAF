import com.google.gson.JsonObject
import me.spencernold.kwaf.*
import me.spencernold.kwaf.services.Service
import java.io.InputStream
import java.util.concurrent.Executors

@Service.Controller
class BasicController {

    // Returns /resources/basic_example.html
    @Route.File(path = "/")
    fun root(): InputStream? {
        return Resource.get("basic_example.html")
    }

    @Route(method = Http.Method.POST, path = "/test", input = true)
    fun test(obj: JsonObject): Int {
        println("Bruh")
        return 2
    }
}

fun main() {
    val server = WebServer.Builder(Protocol.HTTP, 80)
        .executor(Executors.newCachedThreadPool())
        .services(BasicController::class.java)
        .build()
    server.start()
}