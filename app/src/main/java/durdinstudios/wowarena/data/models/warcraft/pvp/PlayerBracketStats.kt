package durdinstudios.wowarena.data.models.warcraft.pvp

import com.squareup.moshi.Json
import durdinstudios.wowarena.data.models.common.Faction
import durdinstudios.wowarena.data.models.common.Gender
import durdinstudios.wowarena.data.models.common.Race
import durdinstudios.wowarena.data.models.common.WoWClass

data class Ranking(@Json(name = "rows") val ranking: List<PlayerBracketStats>)

data class PlayerBracketStats(
        val ranking: Int,
        val rating: Int,
        val name: String,
        val realmId: Int,
        val realmName: String,
        val realmSlug: String,
        @Json(name = "raceId") val race: Race,
        @Json(name = "classId") val gameClass: WoWClass,
        val specId: Int,
        @Json(name = "factionId") val faction: Faction,
        @Json(name = "genderId") val gender: Gender,
        val seasonWins: Int,
        val seasonLosses: Int,
        val weeklyWins: Int,
        val weeklyLosses: Int)