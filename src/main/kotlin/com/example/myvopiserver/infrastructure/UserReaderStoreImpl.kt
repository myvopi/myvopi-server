package com.example.myvopiserver.infrastructure

import com.example.myvopiserver.domain.interfaces.UserReaderStore
import com.example.myvopiserver.domain.role.User
import com.example.myvopiserver.infrastructure.repository.UserRepository
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
    override fun findUserByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }
}