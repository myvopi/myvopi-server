package com.example.myvopiserver.common.util

import com.commoncoremodule.extension.getCurrentDateTime
import java.security.SecureRandom

class CodeGenerator {

    companion object {

        private const val CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        private const val CODE_LENGTH = 6
        private val secureRandom = SecureRandom()

        fun sixDigitCode(): String {
            val code = StringBuilder(CODE_LENGTH)

            for (i in 0 until CODE_LENGTH) {
                val index = secureRandom.nextInt(CHARACTERS.length)
                code.append(CHARACTERS[index])
            }

            return code.toString()
        }

        fun ipIdGenerator(): String {
            val currentDateTime = getCurrentDateTime()
            return "ip_$currentDateTime"
        }
    }
}