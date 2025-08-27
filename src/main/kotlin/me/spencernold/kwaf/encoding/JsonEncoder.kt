package me.spencernold.kwaf.encoding

import com.google.gson.GsonBuilder

class JsonEncoder : Encoder {

    private val gson = GsonBuilder().create()

    override fun encode(obj: Any): String {
        return gson.toJson(obj)
    }

    override fun <T> decode(str: String, type: Class<T>): T {
        return gson.fromJson(str, type)
    }
}