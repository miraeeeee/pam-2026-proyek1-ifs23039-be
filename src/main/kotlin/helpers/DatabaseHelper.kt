package org.delcom.helpers

import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.application.*
import org.delcom.tables.MatchTable
import org.delcom.tables.PlayerStatTable
import org.delcom.tables.RefreshTokenTable
import org.delcom.tables.UserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseHelper {
    fun init(application: Application) {
        val dotenv = dotenv { ignoreIfMissing = true }
        val host = dotenv["DB_HOST"]
        val port = dotenv["DB_PORT"]
        val name = dotenv["DB_NAME"]
        val user = dotenv["DB_USER"]
        val password = dotenv["DB_PASSWORD"]

        Database.connect(
            url = "jdbc:postgresql://$host:$port/$name",
            driver = "org.postgresql.Driver",
            user = user,
            password = password
        )

        transaction {
            SchemaUtils.create(UserTable, RefreshTokenTable, MatchTable, PlayerStatTable)
        }

        application.log.info("Database connected: $name @ $host:$port")
    }
}

// Tambahan ini yang dipanggil di Application.kt
fun Application.configureDatabases() {
    DatabaseHelper.init(this)
}