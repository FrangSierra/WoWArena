package durdinstudios.wowarena.domain.user.token

import android.content.Context
import android.content.SharedPreferences
import com.bq.masmov.reflux.dagger.AppScope

private const val DEFAULT_PREFERENCES_FILE = "battle_net_token_store"
private const val BATTLE_NET_TOKEN = "battle_net_token"

/**
 * Basic implementation of {@link TokenPersistence} using {@link SharedPreferences}.
 */
@AppScope
class PreferencesTokenPersistenceImpl(val context: Context,
                                      private val filename: String = DEFAULT_PREFERENCES_FILE) : TokenPersistence {

    private val inMemoryDelegate = InMemoryTokenPersistenceImpl()

    private val sharedPrefs: SharedPreferences by lazy {
        context.getSharedPreferences(filename, Context.MODE_PRIVATE)
    }

    override fun getToken(): String? {
        val authorizationToken = inMemoryDelegate.getToken()
        if (authorizationToken == null) {
            refreshInMemoryToken()
        }
        return inMemoryDelegate.getToken()
    }

    override fun updateToken(token: String) {
        inMemoryDelegate.updateToken(token)
        saveToken(token = token)
    }

    override fun deleteToken() {
        inMemoryDelegate.deleteToken()
        sharedPrefs.edit().clear().apply()
    }

    private fun readToken(key: String = BATTLE_NET_TOKEN): String? =
        sharedPrefs.getString(key, null)

    private fun saveToken(key: String = BATTLE_NET_TOKEN, token: String) =
        sharedPrefs.edit().putString(key, token).apply()

    private fun refreshInMemoryToken() {
        readToken(BATTLE_NET_TOKEN)?.let {
            inMemoryDelegate.updateToken(it)
        }
    }
}