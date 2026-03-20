package org.delcom.data

import kotlinx.serialization.Serializable

@Serializable
data class DataResponse<T>(
    val status: Boolean,
    val message: String,
    val data: T? = null
)
