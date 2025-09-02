import me.spencernold.kwaf.*
import me.spencernold.kwaf.services.Service
import me.spencernold.kwaf.services.sitemap.Sitemap
import java.io.InputStream
import java.util.concurrent.Executors

@Service.Controller(domain = "https://www.example.com", domainPriority = true)
class SiteMapController {

    // Returns /resources/basic_example.html
    @Route.File( "/")
    @Sitemap
    fun root(): InputStream? {
        return Resource.get("basic_example.html")
    }

    @Route(Http.Method.GET, "/sitemap_arguments")
    @Sitemap(lastModified = "2000-01-01", priority = 1.0, changeFrequency = Sitemap.ChangeFrequency.NEVER)
    fun sitemapWithOptionalArguments(): String {
        return "Adds to the sitemap all the optional arguments. Any and all of them can be added at once."
    }

    @Route(Http.Method.GET, "/hidden")
    @Sitemap.Hidden
    fun hidden(): String {
        return "Ooh, this is a secret..."
    }
}

fun main() {
    val server = WebServer.Builder(Protocol.HTTP, 80)
        .executor(Executors.newCachedThreadPool())
        .services(SiteMapController::class.java)
        // IMPORTANT!!!
        .sitemap()
        .build()
    server.start()
}