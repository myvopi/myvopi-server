package com.entitycoremodule.infrastructure.users

import com.entitycoremodule.domain.user.User
import com.entitycoremodule.domain.interfaces.users.UserReaderStore
import com.entitycoremodule.infrastructure.repository.UserRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class UserReaderStoreImpl(
    private val userRepository: UserRepository,
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
    override fun findUserByUserId(userId: String): User? {
        return userRepository.findByUserId(userId)
    }

    @Transactional(readOnly = true)
    override fun userExistsByUserId(userId: String): Boolean {
        return userRepository.existsUserByUuid(userId)
    }

    @Transactional(readOnly = true)
    override fun userExistsByEmail(email: String): Boolean {
        return userRepository.existsUserByEmail(email)
    }
}