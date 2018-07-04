package durdinstudios.wowarena.core.firebase

import durdinstudios.wowarena.data.models.common.Race
import durdinstudios.wowarena.data.models.common.Region
import durdinstudios.wowarena.data.models.common.WoWClass
import durdinstudios.wowarena.data.models.warcraft.pvp.ArenaBracket
import durdinstudios.wowarena.domain.arena.model.ArenaInfo
import durdinstudios.wowarena.domain.arena.model.ArenaStats
import durdinstudios.wowarena.profile.Character

data class FirebaseCharacter(val username: String = "",
                             val realm: String = "",
                             val region: String = "",
                             val classId: String = "",
                             val raceId: String = "",
                             val level: Int = 0,
                             val thumbnail: String = "")

fun Character.toFirebaseCharacter() = FirebaseCharacter(username = username,
        realm = realm,
        region = region.name,
        classId = klass.name,
        raceId = race.name,
        level = level,
        thumbnail = thumbnail)

fun FirebaseCharacter.toCharacter() = Character(username = username,
        realm = realm,
        region = Region.valueOf(region),
        klass = WoWClass.valueOf(classId),
        race = Race.valueOf(raceId),
        level = level,
        thumbnail = thumbnail)

data class FirebaseArenaStats(val character: FirebaseCharacter = FirebaseCharacter(),
                              val vs2: FirebaseArenaInfo? = null,
                              val vs3: FirebaseArenaInfo? = null,
                              val rbg: FirebaseArenaInfo? = null,
                              val timestamp: Long = 0L)

fun ArenaStats.toFirebaseArenaStats() = FirebaseArenaStats(character = character.toFirebaseCharacter(),
        vs2 = vs2?.toFirebaseArenaInfo(),
        vs3 = vs3?.toFirebaseArenaInfo(),
        rbg = rbg?.toFirebaseArenaInfo(),
        timestamp = timestamp)

fun FirebaseArenaStats.toArenaStats() = ArenaStats(character = character.toCharacter(),
        vs2 = vs2?.toArenaInfo(),
        vs3 = vs3?.toArenaInfo(),
        rbg = rbg?.toArenaInfo(),
        timestamp = timestamp)

data class FirebaseArenaInfo(val bracket: String = "",
                             val rating: Int = 0,
                             val weeklyPlayed: Int = 0,
                             val weeklyWon: Int = 0,
                             val weeklyLost: Int = 0,
                             val seasonPlayed: Int = 0,
                             val seasonWon: Int = 0,
                             val seasonLost: Int = 0)

fun ArenaInfo.toFirebaseArenaInfo() = FirebaseArenaInfo(bracket = bracket.name,
        rating = rating,
        weeklyPlayed = weeklyPlayed,
        weeklyLost = weeklyLost,
        weeklyWon = weeklyWon,
        seasonWon = seasonWon,
        seasonLost = seasonLost,
        seasonPlayed = seasonPlayed)

fun FirebaseArenaInfo.toArenaInfo() = ArenaInfo(bracket = ArenaBracket.valueOf(bracket),
        rating = rating,
        weeklyPlayed = weeklyPlayed,
        weeklyLost = weeklyLost,
        weeklyWon = weeklyWon,
        seasonWon = seasonWon,
        seasonLost = seasonLost,
        seasonPlayed = seasonPlayed)

