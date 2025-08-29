package me.spencernold.kwaf.sitemap

import me.spencernold.kwaf.Route
import me.spencernold.kwaf.WebServer
import me.spencernold.kwaf.handlers.RawStaticHttpHandler
import me.spencernold.kwaf.services.Service
import java.lang.reflect.Method

class MapGeneratorTool {

    // https://search.google.com/search-console/welcome
    companion object {
        fun generateMapXML(server: WebServer) {
            val builder = StringBuilder()
            builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
            builder.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n")
            val controllers = server.getServicesFromType(Service.Controller::class.java)
            for (c in controllers) {
                val controller = c.getAnnotation(Service.Controller::class.java)
                for (method in c.declaredMethods) {
                    if (isRouteAvailable(method) && method.isAnnotationPresent(Sitemap::class.java)) {
                        val sitemap = method.getAnnotation(Sitemap::class.java)
                        builder.append("\t<url>\n")
                        builder.append("\t\t<loc>${controller.domain + controller.path + getRoutePath(method)}</loc>\n")
                        if (sitemap.lastModified.isNotEmpty())
                            builder.append("\t\t<lastmod>${sitemap.lastModified}</lastmod>\n")
                        if (sitemap.priority != -1.0)
                            builder.append("\t\t<priority>${clamp(sitemap.priority, 0.0, 1.0)}</priority>\n")
                        if (sitemap.changeFrequency != Sitemap.ChangeFrequency.NONE)
                            builder.append("\t\t<changefreq>${sitemap.changeFrequency.stringify()}</changefreq>\n")
                        builder.append("\t</url>\n")
                    }
                }
            }
            builder.append("</urlset>\n")

            server.addHandler("/sitemap.xml", RawStaticHttpHandler(builder.toString().toByteArray()))
        }

        fun generateRobots(server: WebServer) {
            val disallow = mutableListOf<String>()
            var domain: String? = null
            val controllers = server.getServicesFromType(Service.Controller::class.java)
            for (controller in controllers) {
                val annotation = controller.getAnnotation(Service.Controller::class.java)
                domain = if (domain.isNullOrEmpty() || annotation.domainPriority) annotation.domain else domain
                for (method in controller.declaredMethods) {
                    if (isRouteAvailable(method) && !method.isAnnotationPresent(Sitemap.Hidden::class.java)) {
                        disallow.add(annotation.path + getRoutePath(method))
                    }
                }
            }
            if (domain == null)
                return
            val builder = StringBuilder()
            builder.append("User-Agent: *\n")
            if (disallow.isEmpty())
                builder.append("Allow: /\n")
            else {
                for (route in disallow)
                    builder.append("Disallow: $route\n")
            }
            builder.append("Sitemap: $domain/sitemap.xml\n")
            server.addHandler("/robots.txt", RawStaticHttpHandler(builder.toString().toByteArray()))
        }

        private fun isRouteAvailable(method: Method): Boolean {
            return method.isAnnotationPresent(Route::class.java) || method.isAnnotationPresent(Route.File::class.java) || method.isAnnotationPresent(
                Route.Directory::class.java
            )
        }

        private fun getRoutePath(method: Method): String {
            return if (method.isAnnotationPresent(Route::class.java)) {
                method.getAnnotation(Route::class.java).path
            } else {
                if (method.isAnnotationPresent(Route.File::class.java))
                    method.getAnnotation(Route.File::class.java).path
                else
                    method.getAnnotation(Route.Directory::class.java).path
            }
        }

        internal fun clamp(n: Double, min: Double, max: Double): Double {
            var value = n
            if (value > max)
                value = max
            if (value < min)
                value = min
            return value
        }
    }
}
