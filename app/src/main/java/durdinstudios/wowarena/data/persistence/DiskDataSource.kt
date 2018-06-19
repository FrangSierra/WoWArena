package durdinstudios.wowarena.data.persistence

import android.content.Context
import android.content.SharedPreferences
import com.bq.masmov.reflux.dagger.AppScope
import com.google.gson.Gson
import durdinstudios.wowarena.data.network.PlayerInfo
import javax.inject.Inject

private const val DEFAULT_PREFERENCES_FILE = "arena_persistence"
private const val PLAYER_INFO = "player_info"

/**
 * Data source implementation to persist the current data.
 */
@AppScope
class DiskDataSource @Inject constructor(private val context: Context,
                                         private val gson: Gson) {

    private val sharedPrefs: SharedPreferences by lazy {
        context.getSharedPreferences(DEFAULT_PREFERENCES_FILE, Context.MODE_PRIVATE)
    }

    fun getPlayerInfo(): PlayerInfo? =
        gson.fromJson<PlayerInfo>(sharedPrefs.getString(PLAYER_INFO, null), PlayerInfo::class.java)

    fun savePlayerInfo(customer: PlayerInfo) {
        sharedPrefs.edit().putString(PLAYER_INFO, gson.toJson(customer)).apply()
    }
}