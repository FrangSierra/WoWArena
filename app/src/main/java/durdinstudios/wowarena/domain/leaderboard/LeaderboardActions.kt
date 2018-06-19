package durdinstudios.wowarena.domain.leaderboard

import durdinstudios.wowarena.data.models.ArenaBracket
import durdinstudios.wowarena.data.network.PlayerStats
import durdinstudios.wowarena.misc.Task
import mini.Action

data class LoadLeaderboardAction(val bracket: ArenaBracket) : Action

data class LoadLeaderboardCompleteAction(val stats: List<PlayerStats>, val task: Task) : Action