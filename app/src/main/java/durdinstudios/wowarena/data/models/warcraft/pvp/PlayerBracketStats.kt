package durdinstudios.wowarena.data.models.warcraft.pvp

import com.google.gson.annotations.SerializedName
import durdinstudios.wowarena.data.models.common.Faction
import durdinstudios.wowarena.data.models.common.Gender
import durdinstudios.wowarena.data.models.common.Race
import durdinstudios.wowarena.data.models.common.WoWClass

data class Ranking(@SerializedName("rows") val ranking: List<PlayerBracketStats>)

data class PlayerBracketStats(
        val ranking: Int,
        val rating: Int,
        val name: String,
        val realmId: Int,
        val realmName: String,
        val realmSlug: String,
        @SerializedName("raceId") val race: Race,
        @SerializedName("classId") val gameClass: WoWClass,
        val specId: Int,
        @SerializedName("factionId") val faction: Faction,
        @SerializedName("genderId") val gender: Gender,
        val seasonWins: Int,
        val seasonLosses: Int,
        val weeklyWins: Int,
        val weeklyLosses: Int)