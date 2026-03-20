package org.delcom.services

import org.delcom.data.AppException
import org.delcom.data.PlayerStatRequest
import org.delcom.entities.PlayerStat
import org.delcom.helpers.ValidatorHelper
import org.delcom.repositories.IPlayerStatRepository

class PlayerStatService(private val playerStatRepository: IPlayerStatRepository) {

    suspend fun getByMatchId(matchId: String): List<PlayerStat> =
        playerStatRepository.getByMatchId(matchId)

    suspend fun create(matchId: String, req: PlayerStatRequest): PlayerStat {
        ValidatorHelper.validatePlayerStatRequest(req.playerName, req.points, req.assists, req.fouls)

        val stat = PlayerStat(
            matchId = matchId,
            playerName = req.playerName,
            points = req.points,
            assists = req.assists,
            fouls = req.fouls
        )
        return playerStatRepository.create(stat)
    }

    suspend fun update(id: String, req: PlayerStatRequest): PlayerStat {
        ValidatorHelper.validatePlayerStatRequest(req.playerName, req.points, req.assists, req.fouls)

        val existing = playerStatRepository.getById(id)
            ?: throw AppException("Statistik pemain tidak ditemukan", 404)

        val updated = existing.copy(
            playerName = req.playerName,
            points = req.points,
            assists = req.assists,
            fouls = req.fouls
        )
        return playerStatRepository.update(updated)
            ?: throw AppException("Gagal mengupdate statistik", 500)
    }

    suspend fun delete(id: String) {
        val deleted = playerStatRepository.delete(id)
        if (!deleted) throw AppException("Statistik pemain tidak ditemukan", 404)
    }
}
