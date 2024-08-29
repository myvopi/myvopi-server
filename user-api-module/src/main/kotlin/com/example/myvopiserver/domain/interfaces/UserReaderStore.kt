package com.example.myvopiserver.domain.interfaces

import com.example.myvopiserver.domain.User
import com.example.myvopiserver.domain.command.InternalUserCommand
import com.example.myvopiserver.domain.command.UpdateClauseCommand

interface UserReaderStore {

    fun saveUser(user: User): User

    fun findUserByUuid(uuid: String): User?

    fun findUserByEmail(email: String): User?

    fun userExistsByEmail(email: String): Boolean

    fun userExistsByUserIdOrEmail(userId: String, email: String): Boolean

    fun updateUserDslRequest(command: InternalUserCommand, commandList: List<UpdateClauseCommand>)
}