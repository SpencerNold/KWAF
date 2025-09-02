package me.spencernold.kwaf.encoding

interface Encoder {
    fun encode(obj: Any): String
    fun <T> decode(str: String, type: Class<T>): T
    fun getContentType(): String
}