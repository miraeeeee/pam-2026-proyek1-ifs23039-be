package org.delcom.entities

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Match(
    var id: String = UUID.randomUUID().toString(),
    var userId: String,
    var opponent: String,
    @Contextual var matchDate: LocalDate,
    var ourScore: Int = 0,
    var opponentScore: Int = 0,
    var result: String = "D",   // "W", "L", "D"
    var notes: String? = null,
    @Contextual var createdAt: Instant = Clock.System.now(),
    @Contextual var updatedAt: Instant = Clock.System.now()
)
