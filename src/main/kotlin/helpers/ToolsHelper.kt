package org.delcom.helpers

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.mindrot.jbcrypt.BCrypt
import java.util.*

object ToolsHelper {

    private var jwtSecret: String = "VrrQISTjQiDFsQ2MF794w4cFaQo2daqzGLuMpA+eWng="

    fun init(secret: String) {
        jwtSecret = secret
    }

    fun hashPassword(plain: String): String = BCrypt.hashpw(plain, BCrypt.gensalt())

    fun verifyPassword(plain: String, hashed: String): Boolean = BCrypt.checkpw(plain, hashed)

    fun generateAccessToken(userId: String): String {
        return JWT.create()
            .withSubject(userId)
            .withExpiresAt(Date(System.currentTimeMillis() + 60 * 60 * 1000))
            .sign(Algorithm.HMAC256(jwtSecret))
    }

    fun generateRefreshToken(userId: String): String {
        return JWT.create()
            .withSubject(userId)
            .withExpiresAt(Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000))
            .sign(Algorithm.HMAC256(jwtSecret))
    }

    fun verifyToken(token: String): String? {
        return try {
            val verifier = JWT.require(Algorithm.HMAC256(jwtSecret)).build()
            verifier.verify(token).subject
        } catch (e: Exception) {
            null
        }
    }

    fun calculateResult(ourScore: Int, opponentScore: Int): String {
        return when {
            ourScore > opponentScore -> "W"
            ourScore < opponentScore -> "L"
            else -> "D"
        }
    }
}