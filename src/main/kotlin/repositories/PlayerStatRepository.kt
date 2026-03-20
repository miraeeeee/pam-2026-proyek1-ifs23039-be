package org.delcom.repositories

import kotlinx.coroutines.Dispatchers
import org.delcom.dao.PlayerStatDAO
import org.delcom.entities.PlayerStat
import org.delcom.tables.PlayerStatTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.UUID

class PlayerStatRepository : IPlayerStatRepository {

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    override suspend fun getByMatchId(matchId: String): List<PlayerStat> = dbQuery {
        PlayerStatTable.selectAll()
            .where { PlayerStatTable.matchId eq UUID.fromString(matchId) }
            .map { PlayerStatDAO.rowToPlayerStat(it) }
    }

    override suspend fun getById(id: String): PlayerStat? = dbQuery {
        PlayerStatTable.selectAll()
            .where { PlayerStatTable.id eq UUID.fromString(id) }
            .singleOrNull()
            ?.let { PlayerStatDAO.rowToPlayerStat(it) }
    }

    override suspend fun create(stat: PlayerStat): PlayerStat = dbQuery {
        val now = kotlinx.datetime.Clock.System.now()
        PlayerStatTable.insert {
            it[id] = UUID.fromString(stat.id)
            it[matchId] = UUID.fromString(stat.matchId)
            it[playerName] = stat.playerName
            it[points] = stat.points
            it[assists] = stat.assists
            it[fouls] = stat.fouls
            it[createdAt] = now
            it[updatedAt] = now
        }
        stat.copy(createdAt = now, updatedAt = now)
    }

    override suspend fun update(stat: PlayerStat): PlayerStat? = dbQuery {
        val now = kotlinx.datetime.Clock.System.now()
        val rows = PlayerStatTable.update({ PlayerStatTable.id eq UUID.fromString(stat.id) }) {
            it[playerName] = stat.playerName
            it[points] = stat.points
            it[assists] = stat.assists
            it[fouls] = stat.fouls
            it[updatedAt] = now
        }
        if (rows > 0) stat.copy(updatedAt = now) else null
    }

    override suspend fun delete(id: String): Boolean = dbQuery {
        val rows = PlayerStatTable.deleteWhere { PlayerStatTable.id eq UUID.fromString(id) }
        rows > 0
    }
}
