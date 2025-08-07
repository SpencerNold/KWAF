package me.spencernold.kwaf.http

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets

open class HttpResponse(val code: Int, val body: ByteArray) {

    private val gson: Gson = GsonBuilder().create()

    fun getBodyAsString(): String {
        return String(body, StandardCharsets.UTF_8)
    }

    fun getBodyFromJson(type: Type): JsonObject {
        return gson.fromJson(getBodyAsString(), type)
    }
}