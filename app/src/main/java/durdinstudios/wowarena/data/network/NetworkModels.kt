package durdinstudios.wowarena.data.network

import com.google.gson.annotations.SerializedName

data class LeaderboardBody(val locale: String)

data class PlayerPvpBody(val fields: String = "pvp",
                         val locale: String)

data class PlayerInfo(val lastModified: Long,
                         val name: String,
                         val realm: String,
                         val battlegroup: String,
                         @SerializedName("class") val classId: Int,
                         val race: Int,
                         val gender: Int,
                         val achievementPoints: Int,
                         val thumbnail: String,
                         val calcClass: String,
                         val faction: Int,
                         val pvp: PvpInfo,
                         val totalHonorableKills: Int)

data class PvpInfo(val brackets: List<BracketPvpInfo>,
                   val totalHonorableKills: Int)

data class BracketPvpInfo(
    val slug: String,
    val rating: Int,
    val weeklyPlayed: Int,
    val weeklyWon: Int,
    val weeklyLost: Int,
    val seasonPlayed: Int,
    val seasonWon: Int,
    val seasonLost: Int
)

data class PlayerStats(
    val ranking: Int,
    val rating: Int,
    val name: String,
    val realmId: Int,
    val realmName: String,
    val realmSlug: String,
    val raceId: Int,
    val classId: Int,
    val specId: Int,
    val factionId: Int,
    val genderId: Int,
    val seasonWins: Int,
    val seasonLosses: Int,
    val weeklyWins: Int,
    val weeklyLosses: Int)