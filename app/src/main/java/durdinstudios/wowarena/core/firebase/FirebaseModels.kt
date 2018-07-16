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
                             val classId: Int = 0,
                             val raceId: Int = 0,
                             val level: Int = 0,
                             val thumbnail: String = "")

fun Character.toFirebaseCharacter() = FirebaseCharacter(username = username,
        realm = realm,
        region = region.name,
        classId = klass.value,
        raceId = race.value,
        level = level,
        thumbnail = thumbnail)

fun FirebaseCharacter.toCharacter() = Character(username = username,
        realm = realm,
        region = Region.valueOf(region.toUpperCase()),
        klass = WoWClass.fromInt(classId),
        race = Race.fromInt(raceId),
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

fun ArenaInfo.toFirebaseArenaInfo() = FirebaseArenaInfo(rating = rating,
        weeklyPlayed = weeklyPlayed,
        weeklyLost = weeklyLost,
        weeklyWon = weeklyWon,
        seasonWon = seasonWon,
        seasonLost = seasonLost,
        seasonPlayed = seasonPlayed)

fun FirebaseArenaInfo.toArenaInfo() = ArenaInfo(rating = rating,
        weeklyPlayed = weeklyPlayed,
        weeklyLost = weeklyLost,
        weeklyWon = weeklyWon,
        seasonWon = seasonWon,
        seasonLost = seasonLost,
        seasonPlayed = seasonPlayed)

