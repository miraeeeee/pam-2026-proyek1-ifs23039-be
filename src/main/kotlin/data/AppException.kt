package org.delcom.data

// AppException.kt
class AppException(message: String, val statusCode: Int = 400) : Exception(message)
