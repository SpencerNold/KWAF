package me.spencernold.kwaf

import me.spencernold.kwaf.encoding.Encoder
import me.spencernold.kwaf.encoding.JsonEncoder

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Route(
    val method: me.spencernold.kwaf.Http.Method,
    val path: String,
    val input: Boolean = false,
    val encoding: Encoding = Encoding.JSON
) {

    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.FUNCTION)
    annotation class File(
        val path: String,
        val contentType: ContentType = ContentType.X_WWW_FORM_URLENCODED,
        val cacheControl: String = "",
        val immutable: Boolean = true
    )

    enum class ContentType(val text: String) {
        NONE("null"),
        PLAIN("text/plain"),
        HTML("text/html"),
        CSS("text/css"),
        JAVASCRIPT("application/javascript"),
        PNG("image/png"),
        JPEG("image/jpeg"),
        GIF("image/gif"),
        WEBP("image/webp"),
        SVG_XML("image/svg+xml"),
        MPEG("audio/mpeg"),
        OGG_AUDIO("audio/ogg"),
        WAV("audio/wav"),
        MP4("video/mp4"),
        WEBM("video/webm"),
        OGG_VIDEO("video/ogg"),
        JSON("application/json"),
        XML("application/xml"),
        PDF("application/pdf"),
        ZIP("application/zip"),
        OCTET_STEAM("application/octet-stream"),
        X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded"),
        FORM_DATA("multipart/form-data")
    }

    class CacheControl {
        companion object {
            const val PUBLIC = "public"
            const val PRIVATE = "private"
            const val NO_STORE = "no-store"
            const val NO_CACHE = "no-cache"
        }
    }

    enum class Encoding(private val encoder: Class<out Encoder>?) {

        RAW(null), JSON(JsonEncoder::class.java);

        fun getEncoder(): Encoder? {
            return encoder?.getDeclaredConstructor()?.newInstance()
        }
    }
}
