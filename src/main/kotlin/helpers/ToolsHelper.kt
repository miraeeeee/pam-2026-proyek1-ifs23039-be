package org.delcom.helpers

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.github.cdimascio.dotenv.dotenv
import org.mindrot.jbcrypt.BCrypt
import java.util.*

object ToolsHelper {

    private val dotenv = dotenv { ignoreIfMissing = true }
    private val jwtSecret = dotenv["JWT_SECRET"]

    // ─── Password ────────────────────────────────────────────────
    fun hashPassword(plain: String): String = BCrypt.hashpw(plain, BCrypt.gensalt())
    fun verifyPassword(plain: String, hashed: String): Boolean = BCrypt.checkpw(plain, hashed)

    // ─── JWT ─────────────────────────────────────────────────────
    fun generateAccessToken(userId: String): String {
        return JWT.create()
            .withSubject(userId)
            .withExpiresAt(Date(System.currentTimeMillis() + 60 * 60 * 1000)) // 1 hour
            .sign(Algorithm.HMAC256(jwtSecret))
    }

    fun generateRefreshToken(userId: String): String {
        return JWT.create()
            .withSubject(userId)
            .withExpiresAt(Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000)) // 30 days
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

    // ─── Match Result Logic ───────────────────────────────────────
    /**
     * Automatically determine match result based on scores.
     * Returns "W" if ourScore > opponentScore,
     *         "L" if ourScore < opponentScore,
     *         "D" if equal (draw — rare in basketball but supported)
     */
    fun calculateResult(ourScore: Int, opponentScore: Int): String {
        return when {
            ourScore > opponentScore -> "W"
            ourScore < opponentScore -> "L"
            else -> "D"
        }
    }
}
