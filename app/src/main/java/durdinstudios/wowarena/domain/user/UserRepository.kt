package durdinstudios.wowarena.domain.user

import android.content.Context
import android.content.SharedPreferences
import com.bq.masmov.reflux.dagger.AppScope
import dagger.Module
import dagger.Provides
import durdinstudios.wowarena.core.gson
import durdinstudios.wowarena.data.models.warcraft.pvp.PlayerInfo
import javax.inject.Inject

@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
interface UserPersistence {
    fun saveUser(user: PlayerInfo?)
    fun getUser(): PlayerInfo?
}

@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
class SharedPrefsUserPersistence @Inject constructor(val context: Context) : UserPersistence {
    companion object {
        const val FILE = "user_prefs"
        const val USER_KEY = "user"
    }

    private val prefs: SharedPreferences by lazy { context.getSharedPreferences(FILE, Context.MODE_PRIVATE) }

    override fun saveUser(user: PlayerInfo?) {
        if (user == null) {
            prefs.edit()
                    .remove(USER_KEY)
                    .apply()
        } else {
            prefs.edit()
                    .putString(USER_KEY, gson.toJson(user))
                    .apply()
        }
    }

    override fun getUser(): PlayerInfo? {
        val serialized = prefs.getString(USER_KEY, null) ?: return null
        return try {
            gson.fromJson(serialized, PlayerInfo::class.java)
        } catch (ex: Throwable) {
            //Remove if parsing fails (essentially forced logout)
            prefs.edit().remove(USER_KEY).apply()
            null
        }
    }
}

/**
 * Abstraction over user persistence.
 */
@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
class UserRepository(private val userPersistence: UserPersistence) {
    fun saveUser(user: PlayerInfo?) {
        userPersistence.saveUser(user)
    }

    fun removeUser() {
        saveUser(null)
    }

    fun getUser(): PlayerInfo? {
        return userPersistence.getUser()
    }

}

@Module
@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
class UserRepositoryModule {
    @Provides
    @AppScope
    fun provideUserRepository(userPersistence: UserPersistence): UserRepository {
        return UserRepository(userPersistence)
    }

    @Provides
    @AppScope
    fun provideUserPersistence(prefsUserPersistence: SharedPrefsUserPersistence): UserPersistence {
        return prefsUserPersistence
    }
}
