package org.delcom.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.LocalDate
import org.delcom.dao.MatchDAO
import org.delcom.entities.Match
import org.delcom.tables.MatchTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.UUID

class MatchRepository : IMatchRepository {

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    override suspend fun getAll(userId: String): List<Match> = dbQuery {
        MatchTable.selectAll()
            .where { MatchTable.userId eq UUID.fromString(userId) }
            .orderBy(MatchTable.matchDate, SortOrder.DESC)
            .map { MatchDAO.rowToMatch(it) }
    }

    override suspend fun getById(id: String, userId: String): Match? = dbQuery {
        MatchTable.selectAll()
            .where {
                (MatchTable.id eq UUID.fromString(id)) and
                (MatchTable.userId eq UUID.fromString(userId))
            }
            .singleOrNull()
            ?.let { MatchDAO.rowToMatch(it) }
    }

    override suspend fun create(match: Match): Match = dbQuery {
        val now = kotlinx.datetime.Clock.System.now()
        MatchTable.insert {
            it[id] = UUID.fromString(match.id)
            it[userId] = UUID.fromString(match.userId)
            it[opponent] = match.opponent
            it[matchDate] = match.matchDate
            it[ourScore] = match.ourScore
            it[opponentScore] = match.opponentScore
            it[result] = match.result
            it[notes] = match.notes
            it[createdAt] = now
            it[updatedAt] = now
        }
        match.copy(createdAt = now, updatedAt = now)
    }

    override suspend fun update(match: Match): Match? = dbQuery {
        val now = kotlinx.datetime.Clock.System.now()
        val rows = MatchTable.update({
            (MatchTable.id eq UUID.fromString(match.id)) and
            (MatchTable.userId eq UUID.fromString(match.userId))
        }) {
            it[opponent] = match.opponent
            it[matchDate] = match.matchDate
            it[ourScore] = match.ourScore
            it[opponentScore] = match.opponentScore
            it[result] = match.result
            it[notes] = match.notes
            it[updatedAt] = now
        }
        if (rows > 0) match.copy(updatedAt = now) else null
    }

    override suspend fun delete(id: String, userId: String): Boolean = dbQuery {
        val rows = MatchTable.deleteWhere {
            (MatchTable.id eq UUID.fromString(id)) and
            (MatchTable.userId eq UUID.fromString(userId))
        }
        rows > 0
    }
}
