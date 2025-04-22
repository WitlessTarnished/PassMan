package com.example.passman.security

import dev.turingcomplete.kotlinonetimepassword.HmacAlgorithm
import dev.turingcomplete.kotlinonetimepassword.TimeBasedOneTimePasswordConfig
import dev.turingcomplete.kotlinonetimepassword.TimeBasedOneTimePasswordGenerator
import java.util.concurrent.TimeUnit

class TotpManager {

    // Function to generate a TOTP (Time-Based One-Time Password) using a secret key
    fun generateTotp(secret: String): String {
        // Configure the TOTP settings
        val config = TimeBasedOneTimePasswordConfig(
            timeStep = 30, // 30 seconds interval
            timeStepUnit = TimeUnit.SECONDS, // The unit for timeStep is in seconds
            codeDigits = 6, // The OTP will have 6 digits
            hmacAlgorithm = HmacAlgorithm.SHA1 // Using HMAC-SHA1 as the hashing algorithm
        )

        // Create the TOTP generator using the secret key and configuration
        val generator = TimeBasedOneTimePasswordGenerator(secret.toByteArray(), config)

        // Generate the TOTP based on the current timestamp
        return generator.generate(System.currentTimeMillis())
    }

    // Function to get the remaining time (in seconds) until the next TOTP
    fun getTotpRemainingSeconds(): Long {
        // Get the current time in seconds
        val currentSeconds = System.currentTimeMillis() / 1000

        // Calculate the remaining time to the next 30-second step
        return 30 - (currentSeconds % 30)
    }
}
