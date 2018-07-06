package durdinstudios.wowarena.domain.user.repository

import android.content.Context
import android.content.SharedPreferences
import com.bq.masmov.reflux.dagger.AppScope
import com.crashlytics.android.Crashlytics
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.Module
import dagger.Provides
import durdinstudios.wowarena.profile.Character
import javax.inject.Inject


@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
interface UserPersistence {
    fun saveUser(user: Character)
    fun getUsers(): List<Character>
    fun getCurrentUser(): Character?
    fun deleteUser(user: Character)
    fun shouldSetupArenaJob(): Boolean
    fun shouldShowTutorial(): Boolean
}

@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
class SharedPrefsUserPersistence @Inject constructor(val context: Context, val moshi: Moshi) : UserPersistence {
    companion object {
        const val FILE = "user_prefs"
        const val USER_LIST_KEY = "users"
        const val JOB_SERVICE_ENABLE = "job_enabled"
        const val TUTORIAL_SHOWN = "tutorial_shown"
        const val CURRENT_USER_KEY = "current_user"
    }

    private val prefs: SharedPreferences by lazy { context.getSharedPreferences(FILE, Context.MODE_PRIVATE) }
    private val characterAdapter: JsonAdapter<Character> = moshi.adapter(Character::class.java)
    private val characterListAdapter: JsonAdapter<List<Character>> =
            moshi.adapter(Types.newParameterizedType(List::class.java, Character::class.java))

    override fun saveUser(user: Character) {
        val serializedList = prefs.getString(USER_LIST_KEY, null)
        val list: List<Character> = if (serializedList == null) emptyList()
        else characterListAdapter.fromJson(serializedList) ?: emptyList()


        if (!list.contains(user)) {
            val playersList = list.plus(user).distinctBy { it.username to it.realm }
            prefs.edit()
                    .putString(USER_LIST_KEY, characterListAdapter.toJson(playersList))
                    .apply()

        }
        prefs.edit()
                .putString(CURRENT_USER_KEY, characterAdapter.toJson(user))
                .apply()
    }

    override fun getUsers(): List<Character> {
        val serialized = prefs.getString(USER_LIST_KEY, null) ?: return emptyList()
        return try {
            characterListAdapter.fromJson(serialized) ?: emptyList()
        } catch (ex: Throwable) {
            Crashlytics.logException(ex)
            //Remove if parsing fails (essentially forced logout)
            prefs.edit().remove(USER_LIST_KEY).apply()
            emptyList()
        }
    }

    override fun getCurrentUser(): Character? {
        val serialized = prefs.getString(CURRENT_USER_KEY, null) ?: return null
        return try {
            characterAdapter.fromJson(serialized)
        } catch (ex: Throwable) {
            Crashlytics.logException(ex)
            //Remove if parsing fails (essentially forced logout)
            prefs.edit().remove(CURRENT_USER_KEY).apply()
            null
        }
    }

    override fun deleteUser(user: Character) {
        val serializedList = prefs.getString(USER_LIST_KEY, null)
        val list: List<Character> = if (serializedList == null) emptyList()
        else characterListAdapter.fromJson(serializedList) ?: emptyList()

        val playersList = list.minus(user).distinctBy { it.username to it.realm }
        val currentUser = getCurrentUser()

        if (currentUser == user) {
            if (playersList.isEmpty()) {
                prefs.edit()
                        .putString(CURRENT_USER_KEY, null)
                        .apply()
            } else {
                prefs.edit()
                        .putString(CURRENT_USER_KEY, characterAdapter.toJson(playersList.first()))
                        .apply()
            }
        }
        prefs.edit()
                .putString(USER_LIST_KEY, characterListAdapter.toJson(playersList))
                .apply()
    }

    override fun shouldSetupArenaJob(): Boolean {
        val jobScheduled = prefs.getBoolean(JOB_SERVICE_ENABLE, true)
        if (!jobScheduled) {
            prefs.edit().putBoolean(JOB_SERVICE_ENABLE, false).apply()
        }
        return jobScheduled
    }

    override fun shouldShowTutorial(): Boolean {
        val showTutorial = prefs.getBoolean(TUTORIAL_SHOWN, true)
        if (showTutorial)
            prefs.edit().putBoolean(TUTORIAL_SHOWN, false).apply()
        return showTutorial
    }
}

/**
 * Abstraction over user persistence.
 */
@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
class UserRepository(private val userPersistence: UserPersistence) {
    fun saveUser(user: Character) {
        userPersistence.saveUser(user)
    }

    fun removeUser(user: Character) {
        userPersistence.deleteUser(user)
    }

    fun getUsers(): List<Character> {
        return userPersistence.getUsers()
    }

    fun getCurrentUser(): Character? {
        return userPersistence.getCurrentUser()
    }

    fun shouldSetupArenaJob(): Boolean {
        return userPersistence.shouldSetupArenaJob()
    }

    fun shouldShowTutorial(): Boolean {
        return userPersistence.shouldShowTutorial()
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
