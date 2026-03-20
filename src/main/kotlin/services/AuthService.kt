package org.delcom.services

import org.delcom.data.*
import org.delcom.entities.RefreshToken
import org.delcom.entities.User
import org.delcom.helpers.ToolsHelper
import org.delcom.repositories.IRefreshTokenRepository
import org.delcom.repositories.IUserRepository

class AuthService(
    private val userRepository: IUserRepository,
    private val refreshTokenRepository: IRefreshTokenRepository
) {
    suspend fun register(name: String, username: String, password: String): AuthResponse {
        if (userRepository.isUsernameExists(username))
            throw AppException("Username sudah digunakan", 409)

        val user = User(
            name = name,
            username = username,
            password = ToolsHelper.hashPassword(password)
        )
        val saved = userRepository.createUser(user)
        return buildAuthResponse(saved)
    }

    suspend fun login(username: String, password: String): AuthResponse {
        val user = userRepository.getUserByUsername(username)
            ?: throw AppException("Username atau password salah", 401)

        if (!ToolsHelper.verifyPassword(password, user.password))
            throw AppException("Username atau password salah", 401)

        return buildAuthResponse(user)
    }

    suspend fun refresh(token: String): AuthResponse {
        val stored = refreshTokenRepository.findByRefreshToken(token)
            ?: throw AppException("Refresh token tidak valid", 401)

        val userId = ToolsHelper.verifyToken(token)
            ?: throw AppException("Refresh token kadaluarsa", 401)

        val user = userRepository.getUserById(userId)
            ?: throw AppException("User tidak ditemukan", 404)

        refreshTokenRepository.deleteByUserId(userId)
        return buildAuthResponse(user)
    }

    suspend fun logout(userId: String) {
        refreshTokenRepository.deleteByUserId(userId)
    }

    private suspend fun buildAuthResponse(user: User): AuthResponse {
        val accessToken = ToolsHelper.generateAccessToken(user.id)
        val refreshToken = ToolsHelper.generateRefreshToken(user.id)

        refreshTokenRepository.save(
            RefreshToken(
                userId = user.id,
                refreshToken = refreshToken,
                authToken = accessToken
            )
        )

        return AuthResponse(
            user = UserResponse(id = user.id, name = user.name, username = user.username),
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
}
