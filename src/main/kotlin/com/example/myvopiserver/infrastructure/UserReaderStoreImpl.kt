package com.example.myvopiserver.infrastructure

import com.example.myvopiserver.domain.interfaces.UserReaderStore
import com.example.myvopiserver.domain.role.User
import com.example.myvopiserver.infrastructure.repository.UserRepository
import org.springframework.stereotype.Repository

@Repository
class UserReaderStoreImpl(
    private val userRepository: UserRepository,
): UserReaderStore {

    override fun findUserByUserId(userId: String): User? {
        return userRepository.findByUserId(userId)
    }

    override fun findUserByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }
}