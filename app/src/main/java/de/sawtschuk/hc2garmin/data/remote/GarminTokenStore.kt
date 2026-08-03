package de.sawtschuk.hc2garmin.data.remote

data class GarminTokens(
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpiresAt: Long,
    val refreshTokenExpiresAt: Long,
    val workingClientId: String
) {
    fun isAccessTokenExpired() = System.currentTimeMillis() >= accessTokenExpiresAt - 60_000L
    fun isRefreshTokenExpired() = System.currentTimeMillis() >= refreshTokenExpiresAt - 60_000L

    /** True when API access can continue without starting a new Garmin SSO login. */
    fun hasUsableSession() = !isAccessTokenExpired() || !isRefreshTokenExpired()
}
