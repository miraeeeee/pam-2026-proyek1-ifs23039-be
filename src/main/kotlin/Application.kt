package org.delcom

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.github.cdimascio.dotenv.dotenv
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.json.Json
import org.delcom.data.AppException
import org.delcom.data.ErrorResponse
import org.delcom.helpers.DatabaseHelper
import org.delcom.helpers.ToolsHelper
import org.delcom.module.appModule
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    val dotenv = dotenv { ignoreIfMissing = true }
    val jwtSecret = dotenv["JWT_SECRET"]

    ToolsHelper.init(jwtSecret)

    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }

    DatabaseHelper.init(this)

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }

    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Options)
    }

    install(Authentication) {
        jwt("auth-jwt") {
            verifier(JWT.require(Algorithm.HMAC256(jwtSecret)).build())
            validate { credential ->
                if (credential.payload.subject != null) JWTPrincipal(credential.payload) else null
            }
            challenge { _, _ ->
                call.respond(
                    HttpStatusCode.Unauthorized,
                    ErrorResponse(message = "Token tidak valid atau sudah kadaluarsa")
                )
            }
        }
    }

    install(StatusPages) {
        exception<AppException> { call, cause ->
            call.respond(
                HttpStatusCode.fromValue(cause.statusCode),
                ErrorResponse(message = cause.message ?: "Terjadi kesalahan")
            )
        }
        exception<Throwable> { call, cause ->
            call.application.log.error("Unhandled exception", cause)
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(message = "Internal server error")
            )
        }
    }

    configureRouting()
}