package com.example.myvopiserver.infrastructure.custom.queryDsl.repository

import com.example.myvopiserver.domain.command.InternalUserCommand
import com.example.myvopiserver.domain.command.UpdateClauseCommand
import org.springframework.stereotype.Repository

@Repository
interface UserReaderStoreDsl {

    fun updateUser(command: InternalUserCommand, commandList: List<UpdateClauseCommand>)
}