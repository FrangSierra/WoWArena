package durdinstudios.wowarena.domain.arena.model

import durdinstudios.wowarena.data.models.warcraft.pvp.ArenaBracket
import durdinstudios.wowarena.data.models.warcraft.pvp.BracketInfo

data class ArenaInfo(val bracket: ArenaBracket,
                     val rating: Int,
                     val weeklyPlayed: Int,
                     val weeklyWon: Int,
                     val weeklyLost: Int,
                     val seasonPlayed: Int,
                     val seasonWon: Int,
                     val seasonLost: Int,
                     val timestamp: Long)

fun BracketInfo.toArenaInfo() = ArenaInfo(bracket = ArenaBracket.values().toList().first { it.value == slug },
        rating = rating,
        weeklyPlayed = weeklyPlayed,
        weeklyWon = weeklyWon,
        weeklyLost = weeklyLost,
        seasonPlayed = seasonPlayed,
        seasonLost = seasonLost,
        seasonWon = seasonWon,
        timestamp = System.currentTimeMillis())