package me.spencernold.kwaf.translator

fun interface Translator<A, B> {
    fun translate(input: A): B
}