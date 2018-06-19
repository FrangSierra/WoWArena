package durdinstudios.wowarena.domain.leaderboard

import durdinstudios.wowarena.data.models.warcraft.pvp.PlayerBracketStats
import durdinstudios.wowarena.misc.Task
import durdinstudios.wowarena.misc.taskIdle

data class LeaderboardState(val playerStats: List<PlayerBracketStats> = emptyList(),
                            val loadStatsTask: Task = taskIdle())