import me.spencernold.kwaf.Protocol
import me.spencernold.kwaf.Resource
import me.spencernold.kwaf.Route
import me.spencernold.kwaf.WebServer
import me.spencernold.kwaf.database.Driver
import me.spencernold.kwaf.database.impl.SQLiteDatabase
import me.spencernold.kwaf.services.Implementation
import me.spencernold.kwaf.services.Service
import java.io.InputStream
import java.util.concurrent.Executors

@Service.Controller
class BasicDBController: Implementation() {
    @Route.File(path = "/")
    fun root(): InputStream? {
        println(getDB())
        return Resource.get("basic_example.html")
    }

    private fun getDB(): BasicDBImpl {
        return getService(BasicDBImpl::class.java)
    }
}

@Service.Database(Driver.Type.SQLITE, url = "jdbc:sqlite:test.db")
class BasicDBImpl: SQLiteDatabase() {
}

fun main() {
    val server = WebServer.Builder(Protocol.HTTP, 80)
        .executor(Executors.newCachedThreadPool())
        .services(BasicDBImpl::class.java, BasicDBController::class.java)
        .build()
    server.start()
}