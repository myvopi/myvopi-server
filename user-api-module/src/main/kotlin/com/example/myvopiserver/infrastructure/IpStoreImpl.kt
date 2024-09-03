package com.example.myvopiserver.infrastructure

import com.example.myvopiserver.domain.interfaces.IpStore
import com.example.myvopiserver.infrastructure.custom.jpql.repository.IpStoreJpql
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class IpStoreImpl(
    private val ipStoreJpql: IpStoreJpql,
): IpStore {

    @Transactional
    override fun saveJpqlRequest(host: String, port: Int, url: String) {
        ipStoreJpql.saveIp(host, port, url)
    }
}