package com.example.myvopiserver.domain.interfaces

interface IpStore {

    fun saveJpqlRequest(host: String, port: Int, url: String)
}