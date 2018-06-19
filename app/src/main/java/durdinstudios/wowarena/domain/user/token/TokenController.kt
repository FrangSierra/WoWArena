package durdinstudios.wowarena.domain.user.token

/**
 * Token controller.
 */
class TokenController(private val tokenPersistence: TokenPersistence) {

    /**
     * Updates the current token information.
     */
    fun updateTokenFromPersistence(token: String) {
        synchronized(this) {
            tokenPersistence.updateToken(token)
        }
    }

    /**
     * Removes the current token information.
     */
    fun removeTokenFromPersistence() {
        synchronized(this) {
            tokenPersistence.deleteToken()
        }
    }

    /**
     * Check if the token information is available.
     *
     * This method is thread safe.
     *
     * @return true if there is a client token stored.
     */
    fun readTokenFromPersistence(): String? {
        synchronized(this) {
            return tokenPersistence.getToken()
        }
    }
}