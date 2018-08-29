package durdinstudios.wowarena.domain.arena.model

import durdinstudios.wowarena.data.models.warcraft.pvp.ArenaBracket
import durdinstudios.wowarena.data.models.warcraft.pvp.ArenaBracket.*
import durdinstudios.wowarena.data.models.warcraft.pvp.BracketInfo
import durdinstudios.wowarena.profile.Character

data class ArenaStats(val character: Character,
                      val vs2: ArenaInfo?,
                      val vs3: ArenaInfo?,
                      val rbg: ArenaInfo?,
                      val timestamp: Long){
    fun getBracket(bracket : ArenaBracket) : ArenaInfo?{
        return when(bracket){
            BRACKET_2_VS_2 -> vs2
            BRACKET_3_VS_3 -> vs3
            RBG -> rbg
        }
    }
}

data class ArenaInfo(val rating: Int,
                     val weeklyPlayed: Int,
                     val weeklyWon: Int,
                     val weeklyLost: Int,
                     val seasonPlayed: Int,
                     val seasonWon: Int,
                     val seasonLost: Int)

fun BracketInfo.toArenaInfo() = ArenaInfo(rating = rating,
        weeklyPlayed = weeklyPlayed,
        weeklyWon = weeklyWon,
        weeklyLost = weeklyLost,
        seasonPlayed = seasonPlayed,
        seasonLost = seasonLost,
        seasonWon = seasonWon)

fun ArenaStats.isEmpty(): Boolean {
    return vs2 == null
            && vs3 == null
            && rbg == null
}

fun ArenaStats.hasData(): Boolean {
    return vs2!!.rating > 0 || vs3!!.rating > 0 || rbg!!.rating > 0
}

fun List<ArenaStats>.filterData(name: String, realm: String): List<ArenaStats> =
        this.filter { it.character.characterEqualsTo(name, realm) }
                .filterNot { it.isEmpty() }
                .filter { it.hasData() }