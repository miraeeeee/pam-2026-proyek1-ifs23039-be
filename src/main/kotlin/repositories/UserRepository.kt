package org.delcom.repositories

import kotlinx.coroutines.Dispatchers
import org.delcom.dao.UserDAO
import org.delcom.entities.User
import org.delcom.tables.UserTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.UUID

class UserRepository : IUserRepository {

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    override suspend fun getUserByUsername(username: String): User? = dbQuery {
        UserTable.selectAll()
            .where { UserTable.username eq username }
            .singleOrNull()
            ?.let { UserDAO.rowToUser(it) }
    }

    override suspend fun getUserById(id: String): User? = dbQuery {
        UserTable.selectAll()
            .where { UserTable.id eq UUID.fromString(id) }
            .singleOrNull()
            ?.let { UserDAO.rowToUser(it) }
    }

    override suspend fun createUser(user: User): User = dbQuery {
        val now = kotlinx.datetime.Clock.System.now()
        UserTable.insert {
            it[id] = UUID.fromString(user.id)
            it[name] = user.name
            it[username] = user.username
            it[password] = user.password
            it[createdAt] = now
            it[updatedAt] = now
        }
        user.copy(createdAt = now, updatedAt = now)
    }

    override suspend fun isUsernameExists(username: String): Boolean = dbQuery {
        UserTable.selectAll()
            .where { UserTable.username eq username }
            .count() > 0
    }
}
