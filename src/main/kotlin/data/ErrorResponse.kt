package org.delcom.data

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val status: Boolean = false,
    val message: String
)
