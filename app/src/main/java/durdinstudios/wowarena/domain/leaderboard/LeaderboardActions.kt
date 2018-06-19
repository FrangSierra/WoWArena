package durdinstudios.wowarena.domain.leaderboard

import durdinstudios.wowarena.data.models.warcraft.pvp.ArenaBracket
import durdinstudios.wowarena.data.models.warcraft.pvp.PlayerBracketStats
import durdinstudios.wowarena.misc.Task
import mini.Action

data class LoadLeaderboardAction(val bracket: ArenaBracket) : Action

data class LoadLeaderboardCompleteAction(val stats: List<PlayerBracketStats>, val task: Task) : Action