package com.example.adminmyvopiserver.common.config

import com.commoncoremodule.util.Cipher
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CipherConfig(
    @Value("\${CIPHER_SECRET}")
    private val aesSecretKey: String,
    @Value("\${CIPHER_IV}")
    private val iv: String,
) {

    private lateinit var cipher: Cipher

    @PostConstruct
    fun cipherInit() {
        this.cipher = Cipher(aesSecretKey, iv)
    }

    @Bean
    fun cipher(): Cipher {
        return this.cipher
    }
}