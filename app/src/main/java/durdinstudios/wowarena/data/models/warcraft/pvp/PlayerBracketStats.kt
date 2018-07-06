package durdinstudios.wowarena.data.models.warcraft.pvp

import com.squareup.moshi.Json
import durdinstudios.wowarena.data.models.common.Faction
import durdinstudios.wowarena.data.models.common.Gender
import durdinstudios.wowarena.data.models.common.Race
import durdinstudios.wowarena.data.models.common.WoWClass
import durdinstudios.wowarena.misc.ArenaUtils

data class Ranking(@Json(name = "rows") val ranking: List<PlayerBracketStats>)

data class PlayerBracketStats(
        val ranking: Int,
        val rating: Int,
        val name: String,
        val realmId: Int,
        val realmName: String,
        val realmSlug: String,
        val race: Race?,
        @Json(name = "classId") val wowClass: WoWClass?,
        val specId: Int,
        @Json(name = "factionId") val faction: Faction,
        @Json(name = "genderId") val gender: Gender,
        val seasonWins: Int,
        val seasonLosses: Int,
        val weeklyWins: Int,
        val weeklyLosses: Int)

fun Ranking.getArenaCutoffs(): Map<Faction, ArenaCutoffs> {
    return Faction.values().map { faction ->
        faction to ArenaCutoffs(seasonGladiator = ArenaUtils.getSeasonGladiatorArenaCutoff(this, faction),
                gladiator = ArenaUtils.getGladiatorArenaCutoff(this, faction),
                duelist = ArenaUtils.getDuelistArenaCutoff(this, faction),
                rival = ArenaUtils.getRivalArenaCutoff(this, faction),
                challenger = ArenaUtils.getChallengerArenaCutoff(this, faction))
    }.toMap()
}