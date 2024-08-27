package com.example.myvopiserver.infrastructure.custom.repository

import com.example.myvopiserver.domain.command.InternalUserCommand
import com.example.myvopiserver.domain.command.UpdateClauseCommand
import org.springframework.stereotype.Repository

@Repository
interface CustomUserReaderStore {

    fun updateUserRequest(command: InternalUserCommand, commandList: List<UpdateClauseCommand>)
}