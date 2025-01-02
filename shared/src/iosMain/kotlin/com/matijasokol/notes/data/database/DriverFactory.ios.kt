package com.matijasokol.notes.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.matijasokol.notes.client.ClientDatabase

actual class DriverFactory {

    actual fun createDriver(): SqlDriver = NativeSqliteDriver(
        schema = ClientDatabase.Schema,
        name = "client_database.db",
    )
}
