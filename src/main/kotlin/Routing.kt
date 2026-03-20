package org.delcom

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.delcom.data.*
import org.delcom.helpers.ServiceHelper
import org.delcom.services.AuthService
import org.delcom.services.MatchService
import org.delcom.services.PlayerStatService
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    val authService: AuthService by inject()
    val matchService: MatchService by inject()
    val playerStatService: PlayerStatService by inject()

    routing {

        get("/") {
            call.respond(HttpStatusCode.OK, DataResponse<String>(true, "ScoreLog API is running 🏀"))
        }

        route("/auth") {
            post("/register") {
                val body = call.receive<Map<String, String>>()
                val name = body["name"] ?: throw AppException("name wajib diisi", 400)
                val username = body["username"] ?: throw AppException("username wajib diisi", 400)
                val password = body["password"] ?: throw AppException("password wajib diisi", 400)
                val result = authService.register(name, username, password)
                call.respond(HttpStatusCode.Created, DataResponse(true, "Registrasi berhasil", result))
            }

            post("/login") {
                val body = call.receive<AuthRequest>()
                val result = authService.login(body.username, body.password)
                call.respond(HttpStatusCode.OK, DataResponse(true, "Login berhasil", result))
            }

            post("/refresh") {
                val body = call.receive<RefreshTokenRequest>()
                val result = authService.refresh(body.refreshToken)
                call.respond(HttpStatusCode.OK, DataResponse(true, "Token diperbarui", result))
            }

            authenticate("auth-jwt") {
                post("/logout") {
                    val userId = ServiceHelper.getUserId(call)
                    authService.logout(userId)
                    call.respond(HttpStatusCode.OK, DataResponse<Unit>(true, "Logout berhasil"))
                }
            }
        }

        authenticate("auth-jwt") {

            route("/matches") {
                get {
                    val userId = ServiceHelper.getUserId(call)
                    val matches = matchService.getAll(userId)
                    call.respond(HttpStatusCode.OK, DataResponse(true, "Berhasil", matches))
                }

                get("/{id}") {
                    val userId = ServiceHelper.getUserId(call)
                    val id = call.parameters["id"] ?: throw AppException("ID tidak valid", 400)
                    val match = matchService.getById(id, userId)
                    call.respond(HttpStatusCode.OK, DataResponse(true, "Berhasil", match))
                }

                post {
                    val userId = ServiceHelper.getUserId(call)
                    val req = call.receive<MatchRequest>()
                    val match = matchService.create(req, userId)
                    call.respond(HttpStatusCode.Created, DataResponse(true, "Pertandingan berhasil ditambahkan", match))
                }

                put("/{id}") {
                    val userId = ServiceHelper.getUserId(call)
                    val id = call.parameters["id"] ?: throw AppException("ID tidak valid", 400)
                    val req = call.receive<MatchRequest>()
                    val match = matchService.update(id, req, userId)
                    call.respond(HttpStatusCode.OK, DataResponse(true, "Pertandingan berhasil diperbarui", match))
                }

                delete("/{id}") {
                    val userId = ServiceHelper.getUserId(call)
                    val id = call.parameters["id"] ?: throw AppException("ID tidak valid", 400)
                    matchService.delete(id, userId)
                    call.respond(HttpStatusCode.OK, DataResponse<Unit>(true, "Pertandingan berhasil dihapus"))
                }

                get("/{matchId}/stats") {
                    val matchId = call.parameters["matchId"] ?: throw AppException("matchId tidak valid", 400)
                    val stats = playerStatService.getByMatchId(matchId)
                    call.respond(HttpStatusCode.OK, DataResponse(true, "Berhasil", stats))
                }

                post("/{matchId}/stats") {
                    val matchId = call.parameters["matchId"] ?: throw AppException("matchId tidak valid", 400)
                    val req = call.receive<PlayerStatRequest>()
                    val stat = playerStatService.create(matchId, req)
                    call.respond(HttpStatusCode.Created, DataResponse(true, "Statistik berhasil ditambahkan", stat))
                }
            }

            route("/stats") {
                put("/{id}") {
                    val id = call.parameters["id"] ?: throw AppException("ID tidak valid", 400)
                    val req = call.receive<PlayerStatRequest>()
                    val stat = playerStatService.update(id, req)
                    call.respond(HttpStatusCode.OK, DataResponse(true, "Statistik berhasil diperbarui", stat))
                }

                delete("/{id}") {
                    val id = call.parameters["id"] ?: throw AppException("ID tidak valid", 400)
                    playerStatService.delete(id)
                    call.respond(HttpStatusCode.OK, DataResponse<Unit>(true, "Statistik berhasil dihapus"))
                }
            }
        }
    }
}