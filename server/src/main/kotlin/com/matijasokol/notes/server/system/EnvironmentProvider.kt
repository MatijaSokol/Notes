package com.matijasokol.notes.server.system

fun interface EnvironmentProvider {

    fun get(key: String): String?
}
