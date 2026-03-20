package org.delcom.repositories

import kotlinx.coroutines.Dispatchers
import org.delcom.dao.RefreshTokenDAO
import org.delcom.entities.RefreshToken
import org.delcom.tables.RefreshTokenTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.UUID

class RefreshTokenRepository : IRefreshTokenRepository {

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    override suspend fun save(refreshToken: RefreshToken): RefreshToken = dbQuery {
        val now = kotlinx.datetime.Clock.System.now()
        RefreshTokenTable.insert {
            it[id] = UUID.fromString(refreshToken.id)
            it[userId] = UUID.fromString(refreshToken.userId)
            it[RefreshTokenTable.refreshToken] = refreshToken.refreshToken
            it[authToken] = refreshToken.authToken
            it[createdAt] = now
        }
        refreshToken.copy(createdAt = now)
    }

    override suspend fun findByRefreshToken(token: String): RefreshToken? = dbQuery {
        RefreshTokenTable.selectAll()
            .where { RefreshTokenTable.refreshToken eq token }
            .singleOrNull()
            ?.let { RefreshTokenDAO.rowToRefreshToken(it) }
    }

    override suspend fun deleteByUserId(userId: String): Unit = dbQuery {
        RefreshTokenTable.deleteWhere { RefreshTokenTable.userId eq UUID.fromString(userId) }
    }
}
