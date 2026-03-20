package org.delcom.repositories

import org.delcom.entities.PlayerStat

interface IPlayerStatRepository {
    suspend fun getByMatchId(matchId: String): List<PlayerStat>
    suspend fun getById(id: String): PlayerStat?
    suspend fun create(stat: PlayerStat): PlayerStat
    suspend fun update(stat: PlayerStat): PlayerStat?
    suspend fun delete(id: String): Boolean
}
