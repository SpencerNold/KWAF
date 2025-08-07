package me.spencernold.kwaf

import java.io.InputStream

class Resource {
    companion object {
        fun get(name: String): InputStream? {
            return Resource::class.java.getResourceAsStream("/$name")
        }
    }
}