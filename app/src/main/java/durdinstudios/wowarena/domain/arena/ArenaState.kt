package durdinstudios.wowarena.domain.arena

import durdinstudios.wowarena.domain.arena.model.ArenaStats
import durdinstudios.wowarena.misc.Task
import durdinstudios.wowarena.misc.taskIdle
import durdinstudios.wowarena.profile.CharacterInfo

data class ArenaState(val arenaData: Map<CharacterInfo, List<ArenaStats>> = emptyMap(),
                      val downloadArenaStatsTask: Task = taskIdle(),
                      val loadArenaDataTasks: Map<CharacterInfo, Task> = emptyMap())