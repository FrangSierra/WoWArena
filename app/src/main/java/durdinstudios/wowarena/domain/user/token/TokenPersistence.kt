package durdinstudios.wowarena.domain.user.token

/**
 * Store and retrieve tokens fetched by [TokenController].
 */
interface TokenPersistence {

    /**
     * Return the stored token.
     */
    fun getToken(): String?

    // ##########################
    // Update
    // ##########################

    /**
     * @param token Token object to persist
     */
    fun updateToken(token: String)

    // ##########################
    // Delete
    // ##########################

    /**
     * Delete token.
     */
    fun deleteToken()
}
