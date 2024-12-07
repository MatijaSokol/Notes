package com.matijasokol.notes.server.database

import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import com.matijasokol.notes.server.ServerDatabase
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopped

fun provideDatabase(
    app: Application,
): ServerDatabase {
    val dbConfig = app.environment.config.config(PATH_DATABASE)

    val datasourceConfig = HikariConfig().apply {
        dbConfig.propertyOrNull(PATH_CONNECTION)?.getString()?.let(this::setJdbcUrl)
        dbConfig.propertyOrNull(PATH_USERNAME)?.getString()?.let(this::setUsername)
        dbConfig.propertyOrNull(PATH_PASSWORD)?.getString()?.let(this::setPassword)
        dbConfig.propertyOrNull(PATH_POOL_SIZE)?.getString()?.toInt()?.let(this::setMaximumPoolSize)
    }

    val driver = HikariDataSource(datasourceConfig).asJdbcDriver()

    ServerDatabase.Schema.create(driver)

    return ServerDatabase(driver = driver).also {
        app.environment.monitor.subscribe(ApplicationStopped) { driver.close() }
    }
}

private const val PATH_DATABASE = "database"
private const val PATH_CONNECTION = "connection"
private const val PATH_USERNAME = "username"
private const val PATH_PASSWORD = "password"
private const val PATH_POOL_SIZE = "poolSize"
