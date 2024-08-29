package com.example.myvopiserver.infrastructure

import com.example.myvopiserver.domain.User
import com.example.myvopiserver.domain.command.InternalUserCommand
import com.example.myvopiserver.domain.command.UpdateClauseCommand
import com.example.myvopiserver.domain.interfaces.UserReaderStore
import com.example.myvopiserver.infrastructure.custom.queryDsl.repository.UserReaderStoreDsl
import com.example.myvopiserver.infrastructure.repository.UserRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class UserReaderStoreImpl(
    private val userRepository: UserRepository,
    private val userReaderStoreDsl: UserReaderStoreDsl,
): UserReaderStore {

    @Transactional
    override fun saveUser(user: User): User {
        return userRepository.save(user)
    }

    @Transactional(readOnly = true)
    override fun findUserByUuid(uuid: String): User? {
        return userRepository.findByUuid(uuid)
    }

    @Transactional(readOnly = true)
    override fun findUserByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    @Transactional(readOnly = true)
    override fun userExistsByEmail(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }

    @Transactional(readOnly = true)
    override fun userExistsByUserIdOrEmail(userId: String, email: String): Boolean {
        return userRepository.existsByUserIdOrEmail(userId, email)
    }

    @Transactional
    override fun updateUserDslRequest(command: InternalUserCommand, commandList: List<UpdateClauseCommand>) {
        userReaderStoreDsl.updateUser(command, commandList)
    }
}