package org.delcom.dao

import org.delcom.entities.User
import org.delcom.tables.UserTable
import org.jetbrains.exposed.sql.ResultRow

object UserDAO {
    fun rowToUser(row: ResultRow): User = User(
        id = row[UserTable.id].toString(),
        name = row[UserTable.name],
        username = row[UserTable.username],
        password = row[UserTable.password],
        createdAt = row[UserTable.createdAt],
        updatedAt = row[UserTable.updatedAt]
    )
}
