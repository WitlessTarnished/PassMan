package com.example.passman.security

import java.security.SecureRandom

class PasswordGenerator {

    private val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=[]{}|;:,.<>?"
    private val random = SecureRandom()

    fun generatePassword(length: Int = 16): String {
        return (1..length)
            .map { chars[random.nextInt(chars.length)] }
            .joinToString("")
    }
}
