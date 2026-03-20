package org.delcom.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object PlayerStatTable : Table("player_stats") {
    val id = uuid("id").autoGenerate()
    val matchId = uuid("match_id")
    val playerName = varchar("player_name", 100)
    val points = integer("points").default(0)
    val assists = integer("assists").default(0)
    val fouls = integer("fouls").default(0)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")

    override val primaryKey = PrimaryKey(id)
}
