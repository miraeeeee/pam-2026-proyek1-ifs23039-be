package org.delcom.data

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val username: String,
    val password: String
)

@Serializable
data class RefreshTokenRequest(
    val refreshToken: String
)
