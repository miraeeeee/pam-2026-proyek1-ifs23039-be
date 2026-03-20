package org.delcom.helpers

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.delcom.data.AppException

object ServiceHelper {
    fun getUserId(call: ApplicationCall): String {
        val principal = call.principal<JWTPrincipal>()
            ?: throw AppException("Unauthorized", 401)
        return principal.payload.subject
            ?: throw AppException("Token tidak valid", 401)
    }
}
