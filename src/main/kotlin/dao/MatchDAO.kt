package org.delcom.dao

import org.delcom.entities.Match
import org.delcom.tables.MatchTable
import org.jetbrains.exposed.sql.ResultRow

object MatchDAO {
    fun rowToMatch(row: ResultRow): Match = Match(
        id = row[MatchTable.id].toString(),
        userId = row[MatchTable.userId].toString(),
        opponent = row[MatchTable.opponent],
        matchDate = row[MatchTable.matchDate],
        ourScore = row[MatchTable.ourScore],
        opponentScore = row[MatchTable.opponentScore],
        result = row[MatchTable.result],
        notes = row[MatchTable.notes],
        createdAt = row[MatchTable.createdAt],
        updatedAt = row[MatchTable.updatedAt]
    )
}
