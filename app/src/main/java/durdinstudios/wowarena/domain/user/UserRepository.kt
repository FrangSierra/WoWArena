package durdinstudios.wowarena.domain.user

import android.content.Context
import android.content.SharedPreferences
import com.bq.masmov.reflux.dagger.AppScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.Module
import dagger.Provides
import durdinstudios.wowarena.data.models.warcraft.pvp.PlayerInfo
import javax.inject.Inject


@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
interface UserPersistence {
    fun saveUser(user: PlayerInfo)
    fun getUsers(): List<PlayerInfo>
    fun getCurrentUser(): PlayerInfo?
    fun deleteUser(user: PlayerInfo)
}

@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
class SharedPrefsUserPersistence @Inject constructor(val context: Context, val gson: Gson) : UserPersistence {
    companion object {
        const val FILE = "user_prefs"
        const val USER_LIST_KEY = "users"
        const val CURRENT_USER_KEY = "current_user"
    }

    private val prefs: SharedPreferences by lazy { context.getSharedPreferences(FILE, Context.MODE_PRIVATE) }

    override fun saveUser(user: PlayerInfo) {
        val listType = object : TypeToken<List<PlayerInfo>>() {}.type
        val serializedList = prefs.getString(USER_LIST_KEY, null)
        val list: List<PlayerInfo> = if (serializedList == null) emptyList()
        else gson.fromJson(serializedList, listType)

        if (!list.contains(user)) {
            val playersList = list.plus(user).distinctBy { it.name to it.realm }
            prefs.edit()
                    .putString(USER_LIST_KEY, gson.toJson(playersList, listType))
                    .apply()

        }
        prefs.edit()
                .putString(CURRENT_USER_KEY, gson.toJson(user))
                .apply()
    }

    override fun getUsers(): List<PlayerInfo> {
        val serialized = prefs.getString(USER_LIST_KEY, null) ?: return emptyList()
        val listType = object : TypeToken<List<PlayerInfo>>() {}.type
        return try {
            gson.fromJson(serialized, listType)
        } catch (ex: Throwable) {
            //Remove if parsing fails (essentially forced logout)
            prefs.edit().remove(USER_LIST_KEY).apply()
            emptyList()
        }
    }

    override fun getCurrentUser(): PlayerInfo? {
        val serialized = prefs.getString(CURRENT_USER_KEY, null) ?: return null
        return try {
            gson.fromJson(serialized, PlayerInfo::class.java)
        } catch (ex: Throwable) {
            //Remove if parsing fails (essentially forced logout)
            prefs.edit().remove(CURRENT_USER_KEY).apply()
            null
        }
    }

    override fun deleteUser(user: PlayerInfo) {
        val listType = object : TypeToken<List<PlayerInfo>>() {}.type
        val serializedList = prefs.getString(USER_LIST_KEY, null)
        val list: List<PlayerInfo> = if (serializedList == null) emptyList()
        else gson.fromJson(serializedList, listType)

        val playersList = list.minus(user).distinctBy { it.name to it.realm }
        val currentUser = getCurrentUser()

        if (currentUser == user) {
            if (playersList.isEmpty()) {
                prefs.edit()
                        .putString(CURRENT_USER_KEY, null)
                        .apply()
            } else {
                prefs.edit()
                        .putString(CURRENT_USER_KEY, gson.toJson(playersList.first(), PlayerInfo::class.java))
                        .apply()
            }
        }
        prefs.edit()
                .putString(USER_LIST_KEY, gson.toJson(playersList, listType))
                .apply()
    }
}

/**
 * Abstraction over user persistence.
 */
@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
class UserRepository(private val userPersistence: UserPersistence) {
    fun saveUser(user: PlayerInfo) {
        userPersistence.saveUser(user)
    }

    fun removeUser(user: PlayerInfo) {
        userPersistence.deleteUser(user)
    }

    fun getUsers(): List<PlayerInfo> {
        return userPersistence.getUsers()
    }

    fun getCurrentUser(): PlayerInfo? {
        return userPersistence.getCurrentUser()
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
