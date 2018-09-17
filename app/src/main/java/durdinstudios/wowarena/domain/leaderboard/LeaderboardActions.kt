package durdinstudios.wowarena.domain.leaderboard

import durdinstudios.wowarena.data.models.common.Faction
import durdinstudios.wowarena.data.models.common.Region
import durdinstudios.wowarena.data.models.warcraft.pvp.ArenaBracket
import durdinstudios.wowarena.data.models.warcraft.pvp.PlayerBracketStats
import durdinstudios.wowarena.misc.Task
import mini.Action

data class LoadLeaderboardAction(val reload: Boolean = true,
                                 val region: Region,
                                 val bracket: ArenaBracket) : Action

data class LoadLeaderboardCompleteAction(val bracket: ArenaBracket,
                                         val stats: List<PlayerBracketStats>,
                                         val task: Task) : Action{
    override fun toString(): String {
        return task.toString() + bracket.toString()
    }
}