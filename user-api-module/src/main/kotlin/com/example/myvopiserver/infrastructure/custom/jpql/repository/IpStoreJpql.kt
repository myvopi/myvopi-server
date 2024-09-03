package com.example.myvopiserver.infrastructure.custom.jpql.repository

import org.springframework.stereotype.Repository

@Repository
interface IpStoreJpql {

    fun saveIp(host: String, port: Int, url: String)
}