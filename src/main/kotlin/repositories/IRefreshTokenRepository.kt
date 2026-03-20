package org.delcom.repositories

import org.delcom.entities.RefreshToken

interface IRefreshTokenRepository {
    suspend fun save(refreshToken: RefreshToken): RefreshToken
    suspend fun findByRefreshToken(token: String): RefreshToken?
    suspend fun deleteByUserId(userId: String)
}
