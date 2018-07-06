package durdinstudios.wowarena.domain.leaderboard

import durdinstudios.wowarena.data.models.common.Faction
import durdinstudios.wowarena.data.models.warcraft.pvp.ArenaBracket
import durdinstudios.wowarena.data.models.warcraft.pvp.ArenaCutoffs
import durdinstudios.wowarena.data.models.warcraft.pvp.PlayerBracketStats
import durdinstudios.wowarena.misc.Task

data class LeaderboardState(val rankingStats: Map<ArenaBracket, List<PlayerBracketStats>> = emptyMap(),
                            val arenaCutoffs: Map<ArenaBracket, Map<Faction, ArenaCutoffs>> = emptyMap(),
                            val loadRankingTask: Map<ArenaBracket, Task> = emptyMap())