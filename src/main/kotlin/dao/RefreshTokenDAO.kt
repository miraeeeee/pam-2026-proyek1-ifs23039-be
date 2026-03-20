package org.delcom.dao

import org.delcom.entities.RefreshToken
import org.delcom.tables.RefreshTokenTable
import org.jetbrains.exposed.sql.ResultRow

object RefreshTokenDAO {
    fun rowToRefreshToken(row: ResultRow): RefreshToken = RefreshToken(
        id = row[RefreshTokenTable.id].toString(),
        userId = row[RefreshTokenTable.userId].toString(),
        refreshToken = row[RefreshTokenTable.refreshToken],
        authToken = row[RefreshTokenTable.authToken],
        createdAt = row[RefreshTokenTable.createdAt]
    )
}
