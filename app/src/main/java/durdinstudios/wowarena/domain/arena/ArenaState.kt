package durdinstudios.wowarena.domain.arena

import durdinstudios.wowarena.domain.arena.model.CharacterArenaStats
import durdinstudios.wowarena.misc.Task
import durdinstudios.wowarena.misc.taskIdle

data class ArenaState(val arenaData: List<CharacterArenaStats>,
                      val downloadArenaStatsTask: Task = taskIdle())