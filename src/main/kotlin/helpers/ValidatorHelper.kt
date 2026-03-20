package org.delcom.helpers

import org.delcom.data.AppException

object ValidatorHelper {

    fun requireNotBlank(value: String, fieldName: String) {
        if (value.isBlank()) throw AppException("$fieldName tidak boleh kosong", 400)
    }

    fun requireNonNegative(value: Int, fieldName: String) {
        if (value < 0) throw AppException("$fieldName tidak boleh negatif", 400)
    }

    fun requireValidDate(dateStr: String) {
        try {
            kotlinx.datetime.LocalDate.parse(dateStr)
        } catch (e: Exception) {
            throw AppException("Format tanggal tidak valid. Gunakan YYYY-MM-DD", 400)
        }
    }

    fun validateMatchRequest(opponent: String, matchDate: String, ourScore: Int, opponentScore: Int) {
        requireNotBlank(opponent, "Nama lawan")
        requireValidDate(matchDate)
        requireNonNegative(ourScore, "Skor tim kita")
        requireNonNegative(opponentScore, "Skor lawan")
    }

    fun validatePlayerStatRequest(playerName: String, points: Int, assists: Int, fouls: Int) {
        requireNotBlank(playerName, "Nama pemain")
        requireNonNegative(points, "Points")
        requireNonNegative(assists, "Assists")
        requireNonNegative(fouls, "Fouls")
    }
}
