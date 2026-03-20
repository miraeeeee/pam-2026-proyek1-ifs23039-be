package org.delcom.services

import kotlinx.datetime.LocalDate
import org.delcom.data.AppException
import org.delcom.data.MatchRequest
import org.delcom.entities.Match
import org.delcom.helpers.ToolsHelper
import org.delcom.helpers.ValidatorHelper
import org.delcom.repositories.IMatchRepository

class MatchService(private val matchRepository: IMatchRepository) {

    suspend fun getAll(userId: String): List<Match> = matchRepository.getAll(userId)

    suspend fun getById(id: String, userId: String): Match =
        matchRepository.getById(id, userId)
            ?: throw AppException("Pertandingan tidak ditemukan", 404)

    suspend fun create(req: MatchRequest, userId: String): Match {
        ValidatorHelper.validateMatchRequest(req.opponent, req.matchDate, req.ourScore, req.opponentScore)

        val result = ToolsHelper.calculateResult(req.ourScore, req.opponentScore)

        val match = Match(
            userId = userId,
            opponent = req.opponent,
            matchDate = LocalDate.parse(req.matchDate),
            ourScore = req.ourScore,
            opponentScore = req.opponentScore,
            result = result,
            notes = req.notes
        )
        return matchRepository.create(match)
    }

    suspend fun update(id: String, req: MatchRequest, userId: String): Match {
        ValidatorHelper.validateMatchRequest(req.opponent, req.matchDate, req.ourScore, req.opponentScore)

        val existing = matchRepository.getById(id, userId)
            ?: throw AppException("Pertandingan tidak ditemukan", 404)

        val result = ToolsHelper.calculateResult(req.ourScore, req.opponentScore)

        val updated = existing.copy(
            opponent = req.opponent,
            matchDate = LocalDate.parse(req.matchDate),
            ourScore = req.ourScore,
            opponentScore = req.opponentScore,
            result = result,
            notes = req.notes
        )
        return matchRepository.update(updated)
            ?: throw AppException("Gagal mengupdate pertandingan", 500)
    }

    suspend fun delete(id: String, userId: String) {
        val deleted = matchRepository.delete(id, userId)
        if (!deleted) throw AppException("Pertandingan tidak ditemukan", 404)
    }
}
