package com.matijasokol.notes.server

import com.matijasokol.notes.server.plugins.configureHTTP
import com.matijasokol.notes.server.plugins.configureMonitoring
import com.matijasokol.notes.server.plugins.configureRouting
import com.matijasokol.notes.server.plugins.configureSerialization
import io.ktor.server.application.Application

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureMonitoring()
    configureHTTP()
    configureRouting()
}
