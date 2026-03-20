package org.delcom.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object RefreshTokenTable : Table("refresh_tokens") {
    val id = uuid("id").autoGenerate()
    val userId = uuid("user_id")
    val refreshToken = text("refresh_token")
    val authToken = text("auth_token")
    val createdAt = timestamp("created_at")

    override val primaryKey = PrimaryKey(id)
}
