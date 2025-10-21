package me.spencernold.kwaf.database

abstract class Driver {

    abstract fun open(credentials: Pair<Credential, Credential>)
    abstract fun loadCredentials(): Pair<Credential, Credential>
}