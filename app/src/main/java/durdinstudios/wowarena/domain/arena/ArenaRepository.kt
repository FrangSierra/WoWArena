package durdinstudios.wowarena.domain.arena

import android.content.Context
import android.content.SharedPreferences
import com.bq.masmov.reflux.dagger.AppScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.Module
import dagger.Provides
import durdinstudios.wowarena.data.models.warcraft.pvp.ArenaBracket
import durdinstudios.wowarena.domain.arena.model.ArenaInfo
import durdinstudios.wowarena.profile.Character
import mini.Grove
import javax.inject.Inject


@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
interface ArenaPersistence {
    fun getArenaStats(): Map<Character, Map<ArenaBracket, List<ArenaInfo>>>
    fun saveArenaStats(character: Character, bracket: ArenaBracket, info: ArenaInfo)
    fun deleteCharacterArenaInfo(character: Character)
}

@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
class SharedPrefsArenaPersistence @Inject constructor(val context: Context, val gson: Gson) : ArenaPersistence {

    companion object {
        const val FILE = "arena_prefs"
        const val ARENA_STATS = "arena_stats"
    }

    private val prefs: SharedPreferences by lazy { context.getSharedPreferences(FILE, Context.MODE_PRIVATE) }

    override fun getArenaStats(): Map<Character, Map<ArenaBracket, List<ArenaInfo>>> {
        val type = object : TypeToken<Map<Character, Map<ArenaBracket, List<ArenaInfo>>>>() {}.type
        val serializedMap = prefs.getString(ARENA_STATS, null)
        return try {
            if (serializedMap == null) emptyMap()
            else gson.fromJson(serializedMap, type)
        } catch (ex: Throwable) {
            Grove.e { ex }
            prefs.edit()
                    .remove(ARENA_STATS)
                    .apply()
            emptyMap()
        }
    }

    override fun saveArenaStats(character: Character, bracket: ArenaBracket, info: ArenaInfo) {
        val type = object : TypeToken<Map<Character, Map<ArenaBracket, List<ArenaInfo>>>>() {}.type
        val serializedMap = prefs.getString(ARENA_STATS, null)
        try {
            val arenaMap: Map<Character, Map<ArenaBracket, List<ArenaInfo>>> = if (serializedMap == null) emptyMap()
            else gson.fromJson(serializedMap, type)
            val characterMap = arenaMap.getOrElse(character) { emptyMap() }
            val bracketList: List<ArenaInfo> = characterMap.getOrElse(bracket) { emptyList() }
            val newMap = arenaMap.plus(character to mapOf(bracket to bracketList.plus(info)))

            prefs.edit()
                    .putString(ARENA_STATS, gson.toJson(newMap, type))
                    .apply()
        } catch (ex: Throwable) {
            Grove.e { ex }
            prefs.edit()
                    .remove(ARENA_STATS)
                    .apply()
        }
    }

    override fun deleteCharacterArenaInfo(character: Character) {
        val type = object : TypeToken<Map<Character, Map<ArenaBracket, List<ArenaInfo>>>>() {}.type
        val serializedMap = prefs.getString(ARENA_STATS, null)
        val arenaMap: Map<Character, Map<ArenaBracket, List<ArenaInfo>>> = if (serializedMap == null) emptyMap()
        else gson.fromJson(serializedMap, type)
        val deletedMap = arenaMap.minus(character)

        prefs.edit()
                .putString(ARENA_STATS, gson.toJson(deletedMap, type))
                .apply()
    }

}


@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
class ArenaRepository(private val arenaPersistence: ArenaPersistence) {

    fun getArenaStats(): Map<Character, Map<ArenaBracket, List<ArenaInfo>>> {
        return arenaPersistence.getArenaStats()
    }

    fun saveArenaStats(character: Character, bracket: ArenaBracket, info: ArenaInfo) {
        arenaPersistence.saveArenaStats(character, bracket, info)
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
