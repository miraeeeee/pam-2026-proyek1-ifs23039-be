package org.delcom.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object UserTable : Table("users") {
    val id = uuid("id").autoGenerate()
    val name = varchar("name", 100)
    val username = varchar("username", 50)
    val password = varchar("password", 255)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")

    override val primaryKey = PrimaryKey(id)
}
