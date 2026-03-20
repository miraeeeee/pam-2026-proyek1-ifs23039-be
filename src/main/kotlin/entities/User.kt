package org.delcom.entities

import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class User(
    var id: String = UUID.randomUUID().toString(),
    var name: String,
    var username: String,
    var password: String,
    @Contextual var createdAt: Instant = kotlinx.datetime.Clock.System.now(),
    @Contextual var updatedAt: Instant = kotlinx.datetime.Clock.System.now()
)
