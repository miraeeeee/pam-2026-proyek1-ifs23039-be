package org.delcom.repositories

import org.delcom.entities.Match

interface IMatchRepository {
    suspend fun getAll(userId: String): List<Match>
    suspend fun getById(id: String, userId: String): Match?
    suspend fun create(match: Match): Match
    suspend fun update(match: Match): Match?
    suspend fun delete(id: String, userId: String): Boolean
}
