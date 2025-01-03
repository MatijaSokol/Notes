package com.matijasokol.notes.data.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.matijasokol.notes.client.ClientDatabase

actual class DriverFactory(
    private val context: Context,
) {

    actual fun createDriver(): SqlDriver = AndroidSqliteDriver(
        schema = ClientDatabase.Schema,
        context = context,
        name = "client_database.db",
    )
}
