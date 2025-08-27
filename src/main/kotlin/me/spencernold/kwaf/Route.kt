package me.spencernold.kwaf

import me.spencernold.kwaf.encoding.Encoder
import me.spencernold.kwaf.encoding.JsonEncoder

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Route(val method: me.spencernold.kwaf.Http.Method, val path: String, val input: Boolean = false, val encoding: Encoding = Encoding.JSON) {

    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.FUNCTION)
    annotation class File(val path: String)

    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.FUNCTION)
    annotation class Directory(val path: String)

    enum class Encoding(private val encoder: Class<out Encoder>?) {

        RAW(null), JSON(JsonEncoder::class.java);

        fun getEncoder(): Encoder? {
            return encoder?.getDeclaredConstructor()?.newInstance()
        }
    }
}
