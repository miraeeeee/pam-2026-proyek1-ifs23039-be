package org.delcom.repositories

import org.delcom.entities.User

interface IUserRepository {
    suspend fun getUserByUsername(username: String): User?
    suspend fun getUserById(id: String): User?
    suspend fun createUser(user: User): User
    suspend fun isUsernameExists(username: String): Boolean
}
