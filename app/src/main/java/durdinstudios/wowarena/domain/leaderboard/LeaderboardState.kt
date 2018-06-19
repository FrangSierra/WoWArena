package durdinstudios.wowarena.domain.leaderboard

import durdinstudios.wowarena.data.network.PlayerStats
import durdinstudios.wowarena.misc.Task
import durdinstudios.wowarena.misc.taskIdle

data class LeaderboardState(val playerStats: List<PlayerStats> = emptyList(),
                            val loadStatsTask : Task = taskIdle())