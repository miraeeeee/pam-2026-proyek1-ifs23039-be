package org.delcom.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.date
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object MatchTable : Table("matches") {
    val id = uuid("id").autoGenerate()
    val userId = uuid("user_id")
    val opponent = varchar("opponent", 100)
    val matchDate = date("match_date")
    val ourScore = integer("our_score").default(0)
    val opponentScore = integer("opponent_score").default(0)
    val result = varchar("result", 10)
    val notes = text("notes").nullable()
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")

    override val primaryKey = PrimaryKey(id)
}
