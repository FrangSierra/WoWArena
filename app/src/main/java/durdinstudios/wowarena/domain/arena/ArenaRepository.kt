package durdinstudios.wowarena.domain.arena

import android.content.Context
import android.content.SharedPreferences
import com.bq.masmov.reflux.dagger.AppScope
import com.crashlytics.android.Crashlytics
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.Module
import dagger.Provides
import durdinstudios.wowarena.data.models.warcraft.pvp.ArenaBracket
import durdinstudios.wowarena.domain.arena.model.ArenaInfo
import durdinstudios.wowarena.domain.arena.model.CharacterArenaStats
import durdinstudios.wowarena.domain.user.SharedPrefsUserPersistence
import durdinstudios.wowarena.profile.Character
import mini.Grove
import javax.inject.Inject


@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
interface ArenaPersistence {
    fun getArenaStats(): List<CharacterArenaStats>
    fun saveArenaStats(stats: CharacterArenaStats)
    fun deleteCharacterArenaInfo(character: Character)
}

@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
class SharedPrefsArenaPersistence @Inject constructor(val context: Context, val moshi: Moshi) : ArenaPersistence {

    companion object {
        const val FILE = "arena_prefs"
        const val ARENA_STATS = "arena_stats"
    }

    private val prefs: SharedPreferences by lazy { context.getSharedPreferences(FILE, Context.MODE_PRIVATE) }
    private val arenaAdapter: JsonAdapter<List<CharacterArenaStats>> =
            moshi.adapter(Types.newParameterizedType(List::class.java, CharacterArenaStats::class.java))

    override fun getArenaStats(): List<CharacterArenaStats> {
        val serializedMap = prefs.getString(ARENA_STATS, null)
        return try {
            if (serializedMap == null) emptyList()
            else {
                arenaAdapter.fromJson(serializedMap) ?: emptyList()
            }
        } catch (ex: Throwable) {
            Grove.e { ex }
            Crashlytics.logException(ex)
            prefs.edit()
                    .remove(ARENA_STATS)
                    .apply()
            emptyList()
        }
    }

    override fun saveArenaStats(stats: CharacterArenaStats) {
        val serializedMap = prefs.getString(ARENA_STATS, null)
        try {
            val list: List<CharacterArenaStats> = if (serializedMap == null) emptyList()
            else arenaAdapter.fromJson(serializedMap) ?: emptyList()

            val playersList = list.plus(stats).distinctBy { it.timestamp }
            prefs.edit()
                    .putString(ARENA_STATS, arenaAdapter.toJson(playersList))
                    .apply()
        } catch (ex: Throwable) {
            Grove.e { ex }
            Crashlytics.logException(ex)
            prefs.edit()
                    .remove(ARENA_STATS)
                    .apply()
        }
    }

    override fun deleteCharacterArenaInfo(character: Character) {
        val serializedMap = prefs.getString(ARENA_STATS, null)
        val arenaMap: List<CharacterArenaStats> = if (serializedMap == null) emptyList()
        else arenaAdapter.fromJson(serializedMap) ?: emptyList()
        val deletedList = arenaMap.filterNot {
            it.character.username == character.username &&
                    it.character.realm == character.realm
        }

        prefs.edit()
                .putString(ARENA_STATS, arenaAdapter.toJson(deletedList))
                .apply()
    }

}


@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
class ArenaRepository(private val arenaPersistence: ArenaPersistence) {

    fun getArenaStats(): List<CharacterArenaStats> {
        return arenaPersistence.getArenaStats()
    }

    fun saveArenaStats(stats: CharacterArenaStats) {
        arenaPersistence.saveArenaStats(stats)
    }

    fun deleteCharacterArenaInfo(character: Character) {
        arenaPersistence.deleteCharacterArenaInfo(character)
    }
}

@Module
@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
class ArenaRepositoryModule {
    @Provides
    @AppScope
    fun provideArenaRepository(arenaPersistence: ArenaPersistence): ArenaRepository {
        return ArenaRepository(arenaPersistence)
    }

    @Provides
    @AppScope
    fun provideArenaPersistence(prefsArenaPersistence: SharedPrefsArenaPersistence): ArenaPersistence {
        return prefsArenaPersistence
    }
}
