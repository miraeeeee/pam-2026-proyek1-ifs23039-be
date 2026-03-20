package org.delcom.dao

import org.delcom.entities.PlayerStat
import org.delcom.tables.PlayerStatTable
import org.jetbrains.exposed.sql.ResultRow

object PlayerStatDAO {
    fun rowToPlayerStat(row: ResultRow): PlayerStat = PlayerStat(
        id = row[PlayerStatTable.id].toString(),
        matchId = row[PlayerStatTable.matchId].toString(),
        playerName = row[PlayerStatTable.playerName],
        points = row[PlayerStatTable.points],
        assists = row[PlayerStatTable.assists],
        fouls = row[PlayerStatTable.fouls],
        createdAt = row[PlayerStatTable.createdAt],
        updatedAt = row[PlayerStatTable.updatedAt]
    )
}
