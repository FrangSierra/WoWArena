package durdinstudios.wowarena.data.models.warcraft.pvp

import com.google.gson.annotations.SerializedName

data class Ranking(@SerializedName("rows") val ranking: List<PlayerBracketStats>)

data class PlayerBracketStats(
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