package durdinstudios.wowarena.domain.arena

import durdinstudios.wowarena.data.models.warcraft.pvp.ArenaBracket
import durdinstudios.wowarena.domain.arena.model.ArenaInfo
import durdinstudios.wowarena.misc.Task
import durdinstudios.wowarena.misc.taskIdle
import durdinstudios.wowarena.profile.Character

data class ArenaState(val arenaData: Map<Character, Map<ArenaBracket, List<ArenaInfo>>> = emptyMap(),
                      val loadArenaInfoTask: Task = taskIdle())