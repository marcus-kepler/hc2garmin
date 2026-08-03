package de.sawtschuk.hc2garmin.data.remote

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GarminTokensTest {

    @Test
    fun `expired access token remains a usable session while refresh token is valid`() {
        val tokens = tokens(
            accessTokenExpiresAt = System.currentTimeMillis() - 1,
            refreshTokenExpiresAt = System.currentTimeMillis() + 120_000
        )

        assertTrue(tokens.hasUsableSession())
    }

    @Test
    fun `valid access token remains usable if refresh token has expired`() {
        val tokens = tokens(
            accessTokenExpiresAt = System.currentTimeMillis() + 120_000,
            refreshTokenExpiresAt = System.currentTimeMillis() - 1
        )

        assertTrue(tokens.hasUsableSession())
    }

    @Test
    fun `session is unusable when both tokens have expired`() {
        val tokens = tokens(
            accessTokenExpiresAt = System.currentTimeMillis() - 1,
            refreshTokenExpiresAt = System.currentTimeMillis() - 1
        )

        assertFalse(tokens.hasUsableSession())
    }

    private fun tokens(accessTokenExpiresAt: Long, refreshTokenExpiresAt: Long) = GarminTokens(
        accessToken = "access",
        refreshToken = "refresh",
        accessTokenExpiresAt = accessTokenExpiresAt,
        refreshTokenExpiresAt = refreshTokenExpiresAt,
        workingClientId = "client"
    )
}
