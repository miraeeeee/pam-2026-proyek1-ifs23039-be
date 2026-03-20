package org.delcom.data

import kotlinx.serialization.Serializable

@Serializable
data class MatchRequest(
    val opponent: String,
    val matchDate: String,       // format: "YYYY-MM-DD"
    val ourScore: Int,
    val opponentScore: Int,
    val notes: String? = null
)

@Serializable
data class PlayerStatRequest(
    val playerName: String,
    val points: Int = 0,
    val assists: Int = 0,
    val fouls: Int = 0
)
