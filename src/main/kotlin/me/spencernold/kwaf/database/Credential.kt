package me.spencernold.kwaf.database

import java.nio.charset.StandardCharsets

class Credential(val bytes: ByteArray) {
    fun wipe() {
        bytes.fill(0)
    }

    @Deprecated("puts the bytes into a string that who knows what will happen to, not recommended, not secure")
    override fun toString(): String {
        return String(bytes, StandardCharsets.UTF_8)
    }
}