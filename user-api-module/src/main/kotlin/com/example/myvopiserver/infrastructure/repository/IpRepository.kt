package com.example.myvopiserver.infrastructure.repository

import com.example.myvopiserver.domain.Ip
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface IpRepository: JpaRepository<Ip, String> {
}