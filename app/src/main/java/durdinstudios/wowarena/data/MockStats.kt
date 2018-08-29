package durdinstudios.wowarena.data

import durdinstudios.wowarena.domain.arena.model.ArenaInfo
import durdinstudios.wowarena.domain.arena.model.ArenaStats
import durdinstudios.wowarena.profile.Character

object MockData{
    fun mockStats(character: Character) = listOf<ArenaStats>(
        ArenaStats(character,
            vs2 = ArenaInfo(2000, 30, 20, 10, 100, 60, 40),
            vs3 = ArenaInfo(2500, 30, 20, 10, 100, 60, 40),
            rbg = ArenaInfo(300, 30, 20, 10, 100, 60, 40),
            timestamp = 1535542772259),
        ArenaStats(character,
            vs2 = ArenaInfo(1500, 30, 20, 10, 100, 60, 40),
            vs3 = ArenaInfo(2000, 30, 20, 10, 100, 60, 40),
            rbg = ArenaInfo(200, 30, 20, 10, 100, 60, 40),
            timestamp = 1515532172259),
        ArenaStats(character,
            vs2 = ArenaInfo(1000, 30, 20, 10, 100, 60, 40),
            vs3 = ArenaInfo(1500, 30, 20, 10, 100, 60, 40),
            rbg = ArenaInfo(100, 30, 20, 10, 100, 60, 40),
            timestamp = 1435532772259),
        ArenaStats(character,
            vs2 = ArenaInfo(2500, 30, 20, 10, 100, 60, 40),
            vs3 = ArenaInfo(3000, 30, 20, 10, 100, 60, 40),
            rbg = ArenaInfo(400, 30, 20, 10, 100, 60, 40),
            timestamp = 1335522772259)
    )
}