package durdinstudios.wowarena.domain.user.token

import com.bq.masmov.reflux.dagger.AppScope

/**
 * Save in memory token logic.
 */
@AppScope
class InMemoryTokenPersistenceImpl : TokenPersistence {
    private var inMemoryToken: String? = null

    override fun getToken(): String? = inMemoryToken

    override fun updateToken(token: String) {
        this.inMemoryToken = token
    }

    override fun deleteToken() {
        inMemoryToken = null
    }
}