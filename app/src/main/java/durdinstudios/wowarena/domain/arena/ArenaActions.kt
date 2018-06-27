package durdinstudios.wowarena.domain.arena

import durdinstudios.wowarena.misc.Task
import durdinstudios.wowarena.profile.Character
import mini.Action

data class DownloadArenaStats(val currentCharacters: List<Character>) : Action

data class DownloadArenaStatsComplete(val task: Task) : Action