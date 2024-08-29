package com.example.adminmyvopiserver.infrastructure

import com.example.adminmyvopiserver.domain.User
import com.example.adminmyvopiserver.domain.command.UserAdminSearchCommand
import com.example.adminmyvopiserver.domain.interfaces.UserReaderStore
import com.example.adminmyvopiserver.infrastructure.custom.repository.CustomUserReaderStore
import com.example.adminmyvopiserver.infrastructure.repository.UserRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class UserReaderStoreImpl(
    private val userRepository: UserRepository,
    private val customUserReaderStore: CustomUserReaderStore,
): UserReaderStore {

    @Transactional(readOnly = true)
    override fun searchUsersRequest(command: UserAdminSearchCommand): List<User> {
        return customUserReaderStore.searchUsersRequest(command)
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
    override fun findUserByUserIdAndUuid(userId: String, uuid: String): User? {
        return userRepository.findByUserIdAndUuid(userId, uuid)
    }

    @Transactional
    override fun saveUser(user: User): User {
        return userRepository.save(user)
    }

}