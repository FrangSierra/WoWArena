package durdinstudios.wowarena.data.models.warcraft.pvp

import com.squareup.moshi.Json
import durdinstudios.wowarena.data.models.common.Faction
import durdinstudios.wowarena.data.models.common.Gender
import durdinstudios.wowarena.data.models.common.Race
import durdinstudios.wowarena.data.models.common.WoWClass

data class PlayerInfo(val lastModified: Long,
                      val name: String,
                      val realm: String,
                      val battlegroup: String,
                      @Json(name = "class") val gameClass: WoWClass,
                      val race: Race,
                      val level: Int,
                      val gender: Gender,
                      val achievementPoints: Int,
                      val thumbnail: String,
                      val calcClass: String,
                      val faction: Faction,
                      val pvp: PvpInfo) {
    fun hasPvpInfo() = pvp.brackets.arena2v2?.hasValidData() ?: false
            || pvp.brackets.arena3v3?.hasValidData() ?: false
            || pvp.brackets.arenaRbg?.hasValidData() ?: false
}

data class PvpInfo(val brackets: BracketsPvp,
                   val totalHonorableKills: Int?)

data class BracketsPvp(
        @Json(name = "ARENA_BRACKET_2v2") val arena2v2: BracketInfo?,
        @Json(name = "ARENA_BRACKET_3v3") val arena3v3: BracketInfo?,
        @Json(name = "ARENA_BRACKET_RBG") val arenaRbg: BracketInfo?,
        @Json(name = "ARENA_BRACKET_2v2_SKIRMISH") val arena2vs2Skirmish: BracketInfo?,
        @Json(name = "ARENA_BRACKET_3v3_SKIRMISH") val arena3vs3Skirmish: BracketInfo?,
        @Json(name = "UNKNOWN") val unknown: BracketInfo?
)

data class BracketInfo(
        val slug: String,
        val rating: Int,
        val weeklyPlayed: Int,
        val weeklyWon: Int,
        val weeklyLost: Int,
        val seasonPlayed: Int,
        val seasonWon: Int,
        val seasonLost: Int) {
    fun hasValidData() = rating > 0
}